package com.varunlegend.vplayer

import android.content.Context
import androidx.media3.common.DefaultTrackSelector
import androidx.media3.common.TrackSelectionParameters

object HardwareDecoderManager {
    fun getTrackSelector(context: Context): DefaultTrackSelector {
        val selector = DefaultTrackSelector(context).apply {
            parameters = TrackSelectionParameters.Builder(context)
                .setForceDisableAdaptive(true)
                .setPreferredAudioMimeType(null)
                .setPreferredVideoMimeType(null)
                .build()
        }
        return selector
    }
    fun toggleHardwareAcceleration(selector: DefaultTrackSelector, useHw: Boolean) {
        selector.parameters = selector.parameters.buildUpon()
            .setForceDisableH264HwAccelerator(!useHw)
            .build()
    }
}
