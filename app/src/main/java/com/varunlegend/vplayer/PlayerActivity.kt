package com.varunlegend.vplayer

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.varunlegend.vplayer.databinding.ActivityPlayerBinding
import com.varunlegend.vplayer.utils.MediaUtils
import com.varunlegend.vplayer.utils.SubtitleGestureHelper
import com.varunlegend.vplayer.utils.ZoomPanListener

class PlayerActivity : AppCompatActivity() {
    private lateinit var b: ActivityPlayerBinding
    private lateinit var player: ExoPlayer
    private lateinit var zoomHelper: ZoomPanListener
    private lateinit var subHelper: SubtitleGestureHelper

    // Gesture & system controls
    private lateinit var audioManager: AudioManager
    private var initialBrightness = 0f
    private var initialVolume = 0
    private var maxVolume = 0
    private var screenWidth = 0
    private var screenHeight = 0
    private var initialX = 0f
    private var initialY = 0f
    private var initialSeek = 0L
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Build ExoPlayer
        player = ExoPlayer.Builder(this).build()
        b.playerView.player = player

        intent.getStringExtra("uri")?.let { uriString ->
            val uri = Uri.parse(uriString)
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            player.playWhenReady = true
        }

        // Initialize helpers
        zoomHelper = ZoomPanListener(b.playerView)
        subHelper = SubtitleGestureHelper(this, player)

        // System metrics & volume
        val dm = resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        // Double-tap for speed toggle
        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val currentSpeed = player.playbackParameters.speed
                val newSpeed = if (currentSpeed == 1f) 1.5f else 1f
                player.setPlaybackSpeed(newSpeed)
                Toast.makeText(this@PlayerActivity, "Speed: ${'$'}newSpeed x", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        // Combined touch listener
        b.playerView.setOnTouchListener { v, e ->
            gestureDetector.onTouchEvent(e)

            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = e.x
                    initialY = e.y
                    initialSeek = player.currentPosition
                    initialBrightness = Settings.System.getInt(
                        contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS
                    ).toFloat()
                    initialVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = e.x - initialX
                    val dy = e.y - initialY
                    if (kotlin.math.abs(dx) > kotlin.math.abs(dy)) {
                        // Horizontal swipe: seek
                        val offset = (dx / screenWidth * player.duration).toLong()
                        player.seekTo((initialSeek + offset).coerceIn(0L, player.duration))
                    } else {
                        // Vertical swipe: brightness (left) or volume (right)
                        if (initialX < screenWidth / 2) {
                            val newB = (initialBrightness - (dy / screenHeight * 255)).toInt().coerceIn(0, 255)
                            Settings.System.putInt(
                                contentResolver,
                                Settings.System.SCREEN_BRIGHTNESS,
                                newB
                            )
                        } else {
                            val volOffset = ((-dy / screenHeight) * maxVolume).toInt()
                            val newV = (initialVolume + volOffset).coerceIn(0, maxVolume)
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newV, 0)
                        }
                    }
                }
            }
            // Chain other gesture helpers
            zoomHelper.onTouch(v, e) || subHelper.onTouch(b.playerView, e)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.player_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_speed -> {
            MediaUtils.showSpeedDialog(this, player)
            true
        }
        R.id.action_zoom_reset -> {
            zoomHelper.resetZoom()
            true
        }
        R.id.action_sub_timing -> {
            subHelper.showSubtitleTimingDialog()
            true
        }
        R.id.action_sub_size -> {
            subHelper.showSubtitleSizeDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= 26 &&
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        ) {
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
