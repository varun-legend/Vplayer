package com.varunlegend.vplayer

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.varunlegend.vplayer.databinding.ActivityPlayerBinding
import com.varunlegend.vplayer.utils.HardwareDecoderManager
import com.varunlegend.vplayer.utils.SubtitleGestureHelper
import com.varunlegend.vplayer.utils.ZoomPanListener
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.PlayerNotificationManager
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerView

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayer()
        setupGestures()
        setupHardwareDecoderToggle()
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this).build().apply {
            val uri = Uri.parse(intent.getStringExtra("uri"))
            setMediaItem(MediaItem.fromUri(uri))
            playWhenReady = true
            prepare()

            // Notification + lock-screen
            PlayerNotificationManager.Builder(
                this@PlayerActivity, 1, "channel"
            ).setMediaDescriptionAdapter(MediaNotificationAdapter(this@PlayerActivity, this))
             .build().setPlayer(this)

            MediaSession.Builder(this@PlayerActivity, this).build()
        }
        binding.playerView.player = player
    }

    private fun setupGestures() {
        // Pinch zoom + pan
        binding.playerView.useController = false
        binding.playerView.setOnTouchListener(ZoomPanListener())
        // Subtitle gestures
        SubtitleGestureHelper(binding.subtitleView, player)
    }

    private fun setupHardwareDecoderToggle() {
        HardwareDecoderManager(this, player!!).attachToggle(binding.btnHwToggle)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
        }
    }

    override fun onDestroy() {
        player?.release()
        super.onDestroy()
    }
}
