package com.varunlegend.vplayer

import android.net.Uri

/** Holds single media metadata */
data class MediaItemModel(
    val title: String,
    val duration: String,
    val uri: Uri
)
