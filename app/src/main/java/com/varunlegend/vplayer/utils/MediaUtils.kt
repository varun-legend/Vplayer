package com.varunlegend.vplayer.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import java.util.concurrent.TimeUnit

/** Utility for building players and extracting metadata */
object MediaUtils {
    fun buildPlayer(context: Context): Player {
        return ExoPlayer.Builder(context).build()
    }

    fun getDuration(ctx: Context, uri: Uri): String {
        val mmr = MediaMetadataRetriever().apply { setDataSource(ctx, uri) }
        val durMs = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
        mmr.release()
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(durMs),
            TimeUnit.MILLISECONDS.toSeconds(durMs) % 60
        )
    }
}
