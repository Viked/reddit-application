package com.test.redditapplication.ui

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.io.File

@BindingAdapter("file")
fun setImageFromFile(view: ImageView, filePath: String?) {
    if (filePath.isNullOrBlank()) {
        view.setImageDrawable(null)
    } else {
        view.setImageURI(Uri.fromFile(File(filePath)))
    }
}