package com.varunlegend.vplayer.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import java.util.concurrent.TimeUnit

object MediaUtils {
    fun buildPlayer(context: Context): Player =
        ExoPlayer.Builder(context).build()

    fun getDuration(ctx: Context, uri: Uri): String {
        val mmr = MediaMetadataRetriever().apply { setDataSource(ctx, uri) }
        val ms = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            ?.toLongOrNull() ?: 0L
        mmr.release()
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(ms),
            TimeUnit.MILLISECONDS.toSeconds(ms) % 60
        )
    }

    fun showSpeedDialog(context: Context, player: Player) {
        val speeds = arrayOf("0.5x","1.0x","1.5x","2.0x")
        val values = floatArrayOf(0.5f,1f,1.5f,2f)
        AlertDialog.Builder(context)
            .setTitle("Playback Speed")
            .setItems(speeds) { _, which ->
                player.setPlaybackSpeed(values[which])
            }
            .show()
    }
}
