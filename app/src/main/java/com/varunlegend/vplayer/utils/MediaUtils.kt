/*
 MIT License
 
 Copyright (c) 2026 Varun Prasath
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

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
