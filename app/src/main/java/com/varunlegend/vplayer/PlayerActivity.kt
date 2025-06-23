package com.varunlegend.vplayer

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.varunlegend.vplayer.databinding.ActivityPlayerBinding
import com.varunlegend.vplayer.utils.MediaUtils
import com.varunlegend.vplayer.utils.SubtitleGestureHelper
import com.varunlegend.vplayer.utils.ZoomPanListener
import androidx.media3.common.MediaItem
import androidx.media3.common.Player

class PlayerActivity : AppCompatActivity() {
    private lateinit var b: ActivityPlayerBinding
    private lateinit var player: Player
    private lateinit var subHelper: SubtitleGestureHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(b.root)

        val uri = Uri.parse(intent.getStringExtra("uri"))
        player = MediaUtils.buildPlayer(this)
        b.playerView.player = player
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare(); player.playWhenReady = true

        // Gestures: zoom & subtitles
        val zoomListener = ZoomPanListener(b.playerView)
        subHelper = SubtitleGestureHelper(this, player)
        b.playerView.setOnTouchListener { v, e ->
            zoomListener.onTouch(v, e) || subHelper.onTouch(b.playerView, e)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) = true.also {
        menuInflater.inflate(R.menu.player_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.action_speed -> {
            MediaUtils.showSpeedDialog(this, player)
            true
        }
        R.id.action_zoom_reset -> {
            // Reset zoom by resetting matrix
            ZoomPanListener(b.playerView).apply { /* logic */ }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= 26 && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
