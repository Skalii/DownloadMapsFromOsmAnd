package skalii.testjob.osmand


import android.content.Context
import android.view.Gravity.BOTTOM
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import okhttp3.ResponseBody

import skalii.testjob.osmand.ui.activity.MainActivity
import skalii.testjob.osmand.ui.adapter.RegionAdapter


fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, LENGTH_SHORT).also {
        it.setGravity(BOTTOM, 0, 600)
        it.show()
    }
}

fun RecyclerView.setCustomVertical(context: Context, adapter: RegionAdapter) {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    setAdapter(adapter)
    setVerticalDivider()
}

fun RecyclerView.setVerticalDivider(
    divider: Int = R.drawable.sh_divider_accent,
    alpha: Int = 127
) {
    addItemDecoration(
        DividerItemDecoration(context, VERTICAL).apply {
            ResourcesCompat.getDrawable(context.resources, divider, null)?.let {
                it.alpha = alpha
                setDrawable(it)
            }
        }
    )
}


fun ResponseBody.saveFileToExternalStorage(path: String) =
    try {
        val futureFile = File(
            "${
                MainActivity.getActivityComponent().getContext().filesDir?.path
            }${File.separator}$path"
        )
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val buffer = ByteArray(65536)
            val fileSize = contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = byteStream()
            outputStream = FileOutputStream(futureFile)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
                fileSizeDownloaded += read.toLong()
//                Log.d("File Download: ", "$fileSizeDownloaded of $fileSize")
            }
            outputStream.flush()
        } catch (e: IOException) {
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
        futureFile
    } catch (e: IOException) {
        File("")
    }