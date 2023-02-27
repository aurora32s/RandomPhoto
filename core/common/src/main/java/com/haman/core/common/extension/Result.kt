package com.haman.core.common.extension

import android.util.Log

inline fun <T, R> T.tryCatching(
    tag: String,
    methodName: String,
    block: T.() -> R
): Result<R> = runCatching {
    return@runCatching block()
}.onFailure {
    Log.e(tag, "> $methodName ${it.message ?: "Occur Unknown Error"}")
}