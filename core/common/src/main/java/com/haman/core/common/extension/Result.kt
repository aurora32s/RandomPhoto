package com.haman.core.common.extension

inline fun <T, R> T.tryCatching(
    tag: String,
    methodName: String,
    block: T.() -> R
): Result<R> = runCatching {
    return@runCatching block()
}.onFailure {
    println("$tag > $methodName ${it.message ?: "Occur Unknown Error"}")
}