package com.haman.core.datastore.memory.image

import android.graphics.Bitmap
import android.util.LruCache
import com.haman.core.common.di.IODispatcher
import com.haman.core.datastore.source.ImageCacheDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import javax.inject.Inject

/**
 * 이미지를 메모리에 캐싱해두어, 이후 동일한 id 의 이미지를 요청하는 경우 메모리에서 가져갑니다.
 * LruCache 사용
 */
@OptIn(ObsoleteCoroutinesApi::class)
class ImageCacheInMemoryDataSource @Inject constructor(
    @IODispatcher val ioDispatcher: CoroutineDispatcher
) : ImageCacheDataSource {
    private val maxMemorySize = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemorySize / 8

    /**
     * 한 번에 한 코루틴이 Lru 를 쓸 수 있도록 actor 처리
     */
    private fun CoroutineScope.cacheActor() = actor<ActorMessage> {
        val cache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return super.sizeOf(key, value)
            }
        }

        for (msg in channel) {
            when (msg) {
                is ActorMessage.GetImage -> msg.response.complete(cache.get(msg.id))
                is ActorMessage.PutImage -> cache.put(msg.id, msg.image)
            }
        }
    }

    private lateinit var cacheActor: SendChannel<ActorMessage>

    init {
        CoroutineScope(ioDispatcher).launch {
            cacheActor = cacheActor()
        }
    }

    /**
     * 해당 id 와 가로 길이의 Bitmap 이미지가 없는 경우 Null 반환
     */
    override suspend fun getImage(id: String, reqWidth: Int): Bitmap? {
        val response = CompletableDeferred<Bitmap?>()
        cacheActor.send(ActorMessage.GetImage("${id}_$reqWidth", response))
        return response.await()
    }

    /**
     * (image_id)_(image 가로 길이) 가 key 로 사용
     */
    override suspend fun addImage(id: String, width: Int, bitmap: Bitmap) {
        cacheActor.send(ActorMessage.PutImage("${id}_$width", bitmap))
    }
}

/**
 * LruCache 에서 수행할 있는 작업 모음
 */
private sealed interface ActorMessage {
    // 이미지 Bitmap 요청
    data class GetImage(
        val id: String,
        val response: CompletableDeferred<Bitmap?>
    ) : ActorMessage

    // 이미지 Bitmap 저장
    data class PutImage(
        val id: String,
        val image: Bitmap
    ) : ActorMessage
}