package com.test.redditapplication.ui

import android.net.Uri
import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.io.File
import java.util.*


@BindingAdapter("file")
fun setImageFromFile(view: ImageView, filePath: String?) {
    if (filePath.isNullOrBlank()) {
        view.setImageDrawable(null)
    } else {
        view.setImageURI(Uri.fromFile(File(filePath)))
    }
}

@BindingAdapter("date")
fun setDate(view: TextView, date: Date?) {
    if (date == null) {
        view.text = ""
        return
    }

    val currentDate = Date()

    val timeDelta = currentDate.time - date.time

    val timeUnit: Long = when {
        timeDelta > DateUtils.YEAR_IN_MILLIS -> DateUtils.YEAR_IN_MILLIS
        timeDelta > DateUtils.WEEK_IN_MILLIS -> DateUtils.WEEK_IN_MILLIS
        timeDelta > DateUtils.DAY_IN_MILLIS -> DateUtils.DAY_IN_MILLIS
        timeDelta > DateUtils.HOUR_IN_MILLIS -> DateUtils.HOUR_IN_MILLIS
        else -> DateUtils.MINUTE_IN_MILLIS
    }
    view.text = DateUtils.getRelativeTimeSpanString(date.time, currentDate.time, timeUnit)
}