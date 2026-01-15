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

        
        player = ExoPlayer.Builder(this).build()
        b.playerView.player = player

        intent.getStringExtra("uri")?.let { uriString ->
            val uri = Uri.parse(uriString)
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            player.playWhenReady = true
        }

        
        zoomHelper = ZoomPanListener(b.playerView)
        subHelper = SubtitleGestureHelper(this, player)

        
        val dm = resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        
        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val currentSpeed = player.playbackParameters.speed
                val newSpeed = if (currentSpeed == 1f) 1.5f else 1f
                player.setPlaybackSpeed(newSpeed)
                Toast.makeText(this@PlayerActivity, "Speed: ${'$'}newSpeed x", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        
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
                        
                        val offset = (dx / screenWidth * player.duration).toLong()
                        player.seekTo((initialSeek + offset).coerceIn(0L, player.duration))
                    } else {
                        
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
