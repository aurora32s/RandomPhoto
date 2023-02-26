package com.haman.core.ui.item

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.haman.core.designsystem.R
import com.haman.core.designsystem.component.ContentText

/**
 * 리스트 아이템 자체를 호출하는데 문제가 발생하였을 경우
 * 화면에 보여주는 Item
 */
@Composable
fun BoxScope.ErrorListItem(
    @StringRes
    message: Int
) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.clip(RoundedCornerShape(99)),
            painter = painterResource(id = R.drawable.profile_sample),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(8.dp))
        ContentText(
            text = stringResource(id = message),
            align = TextAlign.Center
        )
    }
}