package me.rerere.xland.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun Context.toast(
    text: String,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, text, length).show()
}