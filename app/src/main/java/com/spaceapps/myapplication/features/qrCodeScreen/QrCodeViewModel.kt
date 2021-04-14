package com.spaceapps.myapplication.features.qrCodeScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.spaceapps.myapplication.network.ToolsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val toolsApi: ToolsApi
) : ViewModel() {

    val userInput = MutableLiveData<String>()
    private val qrCodeColor = MutableLiveData(Color.BLACK)
    val image = userInput.asFlow().filter { !it.isNullOrBlank() }
        .map { toolsApi.generateQrCode(data = it, width = 400, height = 400) }
        .map { mapStringToBitmap(it.encodedImage, it.width, it.height) }
        .combine(qrCodeColor.asFlow()) { bitmap, color -> colorizeBitmap(bitmap, color) }
        .catch { Timber.e(it) }
        .asLiveData()

    fun onUserInput(input: String) = userInput.postValue(input)

    fun setColor(newColor: Int) = qrCodeColor.postValue(newColor)

    private fun mapStringToBitmap(data: String, width: Int, height: Int): Bitmap {
        val bytes = Base64.decode(data, Base64.DEFAULT)
        val options = BitmapFactory.Options().apply {
            outHeight = height
            outWidth = width
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            .copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun colorizeBitmap(bitmap: Bitmap, color: Int): Bitmap {
        if (color == Color.BLACK) return bitmap
        for (r in 0 until bitmap.height) {
            for (c in 0 until bitmap.width) {
                if (bitmap.getPixel(c, r) == Color.BLACK) bitmap.setPixel(c, r, color)
                if (bitmap.getPixel(c, r) == Color.WHITE) bitmap.setPixel(c, r, Color.TRANSPARENT)
            }
        }
        return bitmap
    }
}
