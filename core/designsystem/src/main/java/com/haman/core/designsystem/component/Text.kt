package com.haman.core.designsystem.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.haman.core.designsystem.theme.RandomPhotoTheme

/**
 * Custom 한 Text Component 생성
 * @param modifier Modifier
 * @param text 보여줄 문자
 * @param style Text Style(MaterialTheme 로 전달 권장)
 * @param align Text 정렬(기본은 가운데 정렬)
 * @param bold true 일 경우 Bold 처리
 * @param color Text 색(기본은 Unspecified)
 */
@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    align: TextAlign = TextAlign.Center,
    bold: Boolean = false,
    color: Color? = null
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        textAlign = align,
        fontWeight = if (bold) FontWeight.Bold else null,
        color = color ?: RandomPhotoTheme.colors.text
    )
}

/**
 * Header5 스타일의 Text Component 생성
 * @param modifier Modifier
 * @param text 보여줄 문자
 * @param align Text 정렬(기본은 시작 정렬)
 * @param color Text 색(기본은 Unspecified)
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Start,
    color: Color? = null
) {
    CustomText(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.h5,
        align = align,
        color = color
    )
}

/**
 * Subtitle1 스타일의 Text Component 생성
 * @param modifier Modifier
 * @param text 보여줄 문자
 * @param align Text 정렬(기본은 가운데 정렬)
 * @param color Text 색(기본은 Unspecified)
 */
@Composable
fun SubTitle(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Center,
    color: Color? = null
) {
    CustomText(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.subtitle1,
        align = align,
        color = color
    )
}

/**
 * Caption 스타일의 Text Component 생성
 * @param modifier Modifier
 * @param text 보여줄 문자
 * @param align Text 정렬(기본은 시작 정렬)
 * @param color Text 색(기본은 Unspecified)
 */
@Composable
fun ContentText(
    modifier: Modifier = Modifier,
    text: String,
    align: TextAlign = TextAlign.Start,
    color: Color? = null
) {
    CustomText(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.caption,
        align = align,
        color = color
    )
}