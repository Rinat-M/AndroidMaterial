package com.rino.nasaapp.providers

import android.content.Context
import com.rino.nasaapp.R

class StringsProvider(
    context: Context
) {
    val noImageForSelectedDayMsg = context.getString(R.string.no_image_for_selected_day)
    val noPhotosFromCurrentCameraMsg = context.getString(R.string.no_photos_from_current_camera)
}
