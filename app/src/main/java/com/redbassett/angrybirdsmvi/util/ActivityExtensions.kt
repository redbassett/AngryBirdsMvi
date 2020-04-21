package com.redbassett.angrybirdsmvi.util

import android.app.Activity
import android.widget.Toast

fun Activity.quickToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
