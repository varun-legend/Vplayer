package com.varunlegend.vplayer.utils
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import java.util.concurrent.TimeUnit

object MediaUtils {
    fun buildMedia3Player(context: Context): Player {
        val player = ExoPlayer.Builder(context).build()
        MediaSession.Builder(context, player).build()
        return player
    }
    fun getDuration(context: Context, uri: Uri): String { /* ... */ }
    fun showSpeedDialog(context: Context) { /* ... */ }
    fun showSubtitleSelector(context: Context, player: Player) { /* ... */ }
    fun showEqualizerDialog(context: Context) { /* ... */ }
    fun extractAudio(context: Context) { /* ... */ }
    fun showVideoFiltersDialog(context: Context) { /* ... */ }
}
