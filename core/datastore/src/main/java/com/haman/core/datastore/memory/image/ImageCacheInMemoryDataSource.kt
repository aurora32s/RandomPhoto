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
    /**
     * TODO OutOfMemory 가 발생하지 않도록 추가적인 작업 필요
     */
    private val maxMemorySize = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemorySize / 8
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

    override suspend fun getImage(id: String): Bitmap? {
        val response = CompletableDeferred<Bitmap?>()
        cacheActor.send(ActorMessage.GetImage(id, response))
        return response.await()
    }

    override suspend fun addImage(id: String, bitmap: Bitmap) {
        cacheActor.send(ActorMessage.PutImage(id, bitmap))
    }
}

private sealed interface ActorMessage {
    data class GetImage(
        val id: String,
        val response: CompletableDeferred<Bitmap?>
    ) : ActorMessage

    data class PutImage(
        val id: String,
        val image: Bitmap
    ) : ActorMessage
}