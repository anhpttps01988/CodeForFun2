package vn.tiki.android.androidhometest.util

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
    val assetManager: AssetManager = context.assets

    val istr: InputStream
    var bitmap: Bitmap? = null
    try {
        istr = assetManager.open(filePath)
        bitmap = BitmapFactory.decodeStream(istr)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bitmap
}

fun formatDateTime(timeSeconds: Int): String {
    val miliSeconds = (timeSeconds * 1000).toLong()
    val date = Date(miliSeconds)
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.US)
    return sdf.format(date)
}