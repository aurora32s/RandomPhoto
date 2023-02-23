package com.haman.core.common.exception

class NoneImageResponseException(
    msg: String = "None Image Bitmap in Response"
) : Exception(msg)

class NetworkException(
    msg: String = "occur Exception in Retrofit"
) : Exception(msg)