package com.haman.core.common.exception

/**
 * Image 와 관련된 로직에서 발생할 수 있는 Custom Exception 들을 관리하는 파일입니다.
 * 어느 부분에서 어떻게 문제가 밣생하였는지 구분하기 위한 용도입니다. :-)
 */

// 이미지 요청에 따른 Response 는 정상적으로 받았지만, bitmap 으로 전환할 수 없는 경우
class NoneImageResponseException(
    msg: String = "None Image Bitmap in Response"
) : Exception(msg)

// 이미지 요청 시, 네트워크 자체에서 에러가 발생한 경우
class ImageRequestNetworkException(
    msg: String = "occur Exception in Retrofit"
) : Exception(msg)