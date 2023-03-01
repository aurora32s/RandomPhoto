package com.haman.core.datastore.disk.impl

import android.graphics.Bitmap
import com.haman.core.common.extension.decodeImage
import com.haman.core.common.extension.tryCatching
import com.haman.core.datastore.disk.DiskCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.*

private const val TAG = "com.haman.core.datastore.disk.impl.ImageDiskLruCache"

/**
 * 이미지를 Disk 에 저장
 * 여러 Coroutine 에서 접근할 수 있기 때문에 동기화 필수
 */
internal class DiskLruCache private constructor(
    private val directory: File,
    private val maxSize: Long
) : DiskCache {

    private val mutex = Mutex()

    // 실제 기록 파일
    private val historyFile = File(directory, HISTORY_FILE)
    private val historyFileTmp = File(directory, HISTORY_FILE_TEMP)
    private val historyFileBackup = File(directory, HISTORY_FILE_BACKUP)

    // 현재 캐싱되어 있는 파일 리스트
    private val lruEntries = LinkedHashMap<String, Entry>(0, 0.75f, true)
    private var historyWriter: Writer? = null
    private var totalSize = 0L

    @Volatile
    private var redundantOpCount = 0

    /**
     * 전달된 reqWidth 만큼 Sampling 된 Bitmap 반환
     */
    override suspend fun getBitmapFromDisk(id: String, reqWidth: Int): Bitmap? {
        checkNotClosed()
        val entry = lruEntries[id]
        mutex.withLock(entry) {
            return tryCatching(TAG, "getBitmapFromDisk") {
                if (entry == null || entry.readable.not()) return null

                val file = entry.getCleanFile()
                redundantOpCount++
                historyWriter?.write("${HistoryType.READ.ordinal} $id")

                return if (file.exists()) file.readBytes().decodeImage(reqWidth) else null
            }.getOrNull()
        }
    }

    override suspend fun putBitmapInDisk(id: String, bitmap: Bitmap) {
        checkNotClosed()
        val entry = lruEntries[id]
        mutex.withLock(entry) {
            tryCatching(TAG, "putBitmapInDisk") {
                val currentEntry = entry ?: Entry(id).also { lruEntries[id] = it }
                if (currentEntry.currentEditor != null) return

                val editor = Editor(currentEntry)
                currentEntry.currentEditor = editor

                historyWriter?.let {
                    it.write("${HistoryType.DIRTY.ordinal} $id")
                    it.flush()
                }
                editor.newOutputStream()?.use {
                    bitmap.copy(Bitmap.Config.RGB_565, false)
                        .compress(Bitmap.CompressFormat.JPEG, 100, it)
                    editor.commit()
                }
            }
        }
    }

    private fun readHistory() {
        var totalLine = 0
        historyFile.inputStream().bufferedReader(CHAR_SET).useLines {
            it.forEach { line ->
                readHistoryLine(line)
                totalLine++
            }
        }
        redundantOpCount = totalLine - lruEntries.size
        historyWriter = historyFile.outputStream().bufferedWriter(CHAR_SET)
    }

    private fun readHistoryLine(line: String) {
        // Entry 정보
        val data = line.split(" ")
        if (data.isEmpty()) throw IOException("Unexpected history line: $line")

        val type = HistoryType.getHistoryType(data[0].toInt())
        val key = data[1] // 이미지 key
        // 제거 작업이었을 경우
        if (type == HistoryType.REMOVE) {
            // lru 리스트에서 제거
            lruEntries.remove(key)
            return
        }

        val entry = lruEntries[key] ?: Entry(key).also { lruEntries[key] = it }
        if (type == HistoryType.CLEAN) {
            entry.readable = true
            entry.currentEditor = null
            // 해당 이미지의 size
            entry.size = data[2].toLong()
        } else if (type == HistoryType.DIRTY) {
            entry.currentEditor = Editor(entry)
        }
    }

    private fun processHistory() {
        deleteIfExist(historyFileTmp)
        for (entry in lruEntries.values) {
            // 작성이 완료된 상태인 경우
            if (entry.currentEditor == null) {
                totalSize += entry.size
            } else { // 작성중이었을 경우, 작성중이던 모든 파일 제거
                entry.currentEditor = null
                deleteIfExist(entry.getCleanFile())
                deleteIfExist(entry.getDirtyFile())
            }
        }
    }

    private fun rebuildHistory() {
        if (historyWriter != null) historyWriter = null

        val tmpWriter = historyFileTmp.outputStream().bufferedWriter(CHAR_SET)
        try {
            for (entry in lruEntries.values) {
                if (entry.currentEditor != null) {
                    tmpWriter.write("${HistoryType.DIRTY.ordinal} ${entry.key}")
                } else {
                    tmpWriter.write("${HistoryType.CLEAN.ordinal} ${entry.key} ${entry.size}")
                }
            }
        } finally {
            tmpWriter.close()
        }

        if (historyFile.exists()) renameTo(historyFile, historyFileBackup, true)
        renameTo(historyFileTmp, historyFile, false)
        historyFileBackup.delete()

        historyWriter = historyFile.outputStream().bufferedWriter(CHAR_SET)
    }

    private fun checkNotClosed() {
        historyWriter ?: throw IllegalStateException("cache is closed")
    }

    private suspend fun completeEdit(editor: Editor, success: Boolean) {
        val entry = editor.entry
        if (entry.currentEditor != editor)
            throw IllegalStateException("different editor exception")

        if (success && entry.readable.not()) {
            if (entry.getDirtyFile().exists().not()) {
                editor.abort()
                return
            }
        }

        val dirtyFile = entry.getDirtyFile()
        if (success) {
            if (dirtyFile.exists()) {
                val clean = entry.getCleanFile()
                dirtyFile.renameTo(clean)
                val oldLength = entry.size
                val newLength = clean.length()
                entry.size = newLength
                totalSize -= (oldLength - newLength)
            }
        } else {
            deleteIfExist(dirtyFile)
        }

        redundantOpCount++
        entry.currentEditor = null
        if (entry.readable or success) {
            entry.readable = true
            historyWriter?.write("${HistoryType.CLEAN.ordinal} ${entry.key} ${entry.size}")
        } else {
            lruEntries.remove(entry.key)
            historyWriter?.write("${HistoryType.REMOVE.ordinal} ${entry.key}")
        }
        historyWriter?.flush()
        cleanup()
    }

    private suspend fun delete() {
        close()
    }

    private suspend fun close() {
        if (historyWriter == null) return
        for (entry in lruEntries.values) {
            if (entry.currentEditor != null) {
                entry.currentEditor?.abort()
            }
        }
        trimToSize()
        historyWriter?.close()
        historyWriter = null
    }

    private suspend fun trimToSize() {
        while (totalSize > maxSize) {
            val firstKey = lruEntries.keys.first()
            remove(firstKey)
        }
    }

    private suspend fun remove(key: String) {
        val entry = lruEntries[key]
        if (entry == null || entry.currentEditor != null) return

        val file = entry.getCleanFile()
        deleteIfExist(file)
        totalSize -= entry.size

        redundantOpCount++
        historyWriter?.write("${HistoryType.REMOVE.ordinal} $key")
        lruEntries.remove(key)

        if (historyRebuildRequired()) {
            cleanup()
        }
    }

    /**
     * 보관할 수 있는 명령어(기록)의 개수가 많아지는 경우
     * history 파일을 Rebuild 해야 합니다.
     */
    private fun historyRebuildRequired(): Boolean {
        return redundantOpCount >= RedundantOpCompactThreshold
                && redundantOpCount >= lruEntries.size
    }

    /**
     * 새로운 정보가 추가된 이후, 개수를 유지하기 위해 정리가 필요합니다.
     */
    private suspend fun cleanup() = withContext(Dispatchers.IO) {
        if (historyWriter != null) {
            trimToSize()
            if (historyRebuildRequired()) {
                rebuildHistory()
                redundantOpCount = 0
            }
        }
    }

    private inner class Entry(
        val key: String
    ) {
        var size: Long = 0L
        var readable: Boolean = false
        var currentEditor: Editor? = null

        fun getCleanFile() = File(directory, "file_$key")
        fun getDirtyFile() = File(directory, "file_$key.temp")
    }

    private inner class Editor(
        val entry: Entry
    ) {
        private var isError: Boolean = false
        suspend fun newOutputStream(): OutputStream? {
            if (entry.currentEditor != this)
                throw IllegalStateException("different editor exception")

            val dirtyFile = entry.getDirtyFile()
            val outputStream = try {
                dirtyFile.outputStream()
            } catch (e: FileNotFoundException) {
                directory.mkdirs()
                try {
                    dirtyFile.outputStream()
                } catch (e: FileNotFoundException) {
                    null
                }
            }

            return outputStream?.let { FaultHidingOutputStream(it) }
        }

        suspend fun commit() {
            if (isError) {
                completeEdit(this, false)
                remove(entry.key)
            } else {
                completeEdit(this, true)
            }
        }

        suspend fun abort() {
            completeEdit(this, false)
        }

        private inner class FaultHidingOutputStream(
            private val outputStream: OutputStream
        ) : FilterOutputStream(outputStream) {
            override fun write(value: Int) {
                try {
                    outputStream.write(value)
                } catch (e: IOException) {
                    isError = true
                }
            }

            override fun write(buffer: ByteArray, off: Int, len: Int) {
                try {
                    outputStream.write(buffer, off, len)
                } catch (e: IOException) {
                    isError = true
                }
            }

            override fun close() {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    isError = true
                }
            }

            override fun flush() {
                try {
                    outputStream.flush()
                } catch (e: IOException) {
                    isError = true
                }
            }
        }
    }

    companion object {
        const val HISTORY_FILE = "history_file"
        const val HISTORY_FILE_TEMP = "history_file_temp"
        const val HISTORY_FILE_BACKUP = "history_file_backup"
        val CHAR_SET = Charsets.US_ASCII
        const val RedundantOpCompactThreshold = 2000

        suspend fun open(directory: File, maxSize: Long): DiskLruCache {
            if (maxSize <= 0) throw IllegalStateException("maxSize have to be over 0")

            val backupFile = File(directory, HISTORY_FILE_BACKUP)
            if (backupFile.exists()) {
                val historyFile = File(directory, HISTORY_FILE)
                if (historyFile.exists()) { // 기록 원본 파일이 존재한다면
                    // backup 파일은 제거
                    backupFile.delete()
                } else {
                    // backup 파일 내용을 원본 기록 파일로 이동
                    renameTo(backupFile, historyFile, false)
                }
            }

            val cache = DiskLruCache(directory, maxSize)
            if (cache.historyFile.exists()) {
                try {
                    cache.readHistory()
                    cache.processHistory()
                    return cache
                } catch (e: IOException) {
                    cache.delete()
                }
            }

            /**
             * 상위 디렉토리가 존재하지 않을 경우, 상위 디렉토리까지 생성
             */
            directory.mkdirs()
            return DiskLruCache(directory, maxSize).also {
                it.rebuildHistory()
            }
        }

        /**
         * from > to
         * @param from 옮기 파일
         * @param to 목적지 파일
         * @param deleteDestination true 이고 to file 이 존재한다면 to file 을 제거하고 이동
         */
        private fun renameTo(from: File, to: File, deleteDestination: Boolean) {
            if (deleteDestination) deleteIfExist(to)
            if (from.renameTo(to).not()) {
                throw IOException("fail to rename from $from to $to")
            }
        }

        /**
         * 만약 인자로 전달된 파일이 존재한다면 제거
         */
        private fun deleteIfExist(file: File) {
            if (file.exists() && file.delete().not()) {
                throw IOException("fail to delete $file")
            }
        }
    }
}

/**
 * 기록의 type
 */
enum class HistoryType {
    CLEAN, DIRTY, REMOVE, READ;

    companion object {
        fun getHistoryType(type: Int) = values().first { it.ordinal == type }
    }
}
