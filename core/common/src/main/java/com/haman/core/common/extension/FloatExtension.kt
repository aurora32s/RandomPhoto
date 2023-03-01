package com.haman.core.common.extension

import android.content.res.Resources

fun Float.fromDpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()