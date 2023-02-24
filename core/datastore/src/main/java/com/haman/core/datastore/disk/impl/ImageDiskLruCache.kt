package com.haman.core.datastore.disk.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.haman.core.datastore.disk.DiskCache
import java.io.*

/**
 * 이미지를 Disk 에 저장
 * 여러 Coroutine 에서 접근할 수 있기 때문에 동기화 필수
 */
class DiskLruCache private constructor(
    private val directory: File,
    private val maxSize: Long
) : DiskCache {

    private val historyFile = File(directory, HISTORY_FILE)
    private val historyFileTmp = File(directory, HISTORY_FILE_TEMP)
    private val historyFileBackup = File(directory, HISTORY_FILE_BACKUP)

    private val lruEntries = LinkedHashMap<String, Entry>(0, 0.75f, true)
    private var historyWriter: Writer? = null
    private var totalSize = 0L
    private var redundantOpCount = 0

    override suspend fun getBitmapFromDisk(id: String): Bitmap? {
        val entry = lruEntries[id]
        if (entry == null || entry.readable.not()) return null

        val file = entry.getCleanFile()
        return if (file.exists()) BitmapFactory.decodeStream(file.inputStream()) else null
    }

    override suspend fun putBitmapInDisk(id: String, bitmap: Bitmap) {
        checkNotClosed()
        val entry = lruEntries[id] ?: Entry(id).also { lruEntries[id] = it }
        if (entry.currentEditor != null) return

        val editor = Editor(entry)
        entry.currentEditor = editor

        historyWriter?.let {
            it.write("${HistoryType.DIRTY.ordinal} $id")
            it.flush()
        }
        editor.newOutputStream()?.let {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            editor.commit()
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
        val key = data[1]
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

    private fun completeEdit(editor: Editor, success: Boolean) {
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
        if (entry.readable || success) {
            entry.readable = true
            historyWriter?.write("${HistoryType.CLEAN.ordinal} ${entry.key} ${entry.size}")
        } else {
            lruEntries.remove(entry.key)
            historyWriter?.write("${HistoryType.REMOVE.ordinal} ${entry.key}")
        }
        historyWriter?.flush()
    }

    private fun delete() {
        close()
    }

    private fun close() {
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

    private fun trimToSize() {
        while (totalSize > maxSize) {
            val firstKey = lruEntries.keys.first()
            remove(firstKey)
        }
    }

    private fun remove(key: String) {
        val entry = lruEntries[key]
        if (entry == null || entry.currentEditor != null) return

        val file = entry.getCleanFile()
        deleteIfExist(file)
        totalSize -= entry.size

        redundantOpCount++
        historyWriter?.write("${HistoryType.REMOVE.ordinal} $key")
        lruEntries.remove(key)
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
        fun newOutputStream(): OutputStream? {
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

        fun commit() {
            if (isError) {
                completeEdit(this, false)
                remove(entry.key)
            } else {
                completeEdit(this, true)
            }
        }

        fun abort() {
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

        fun open(directory: File, maxSize: Long): DiskLruCache {
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

enum class HistoryType {
    CLEAN, DIRTY, REMOVE, READ, NONE;

    companion object {
        fun getHistoryType(type: Int) = values().first { it.ordinal == type }
    }
}