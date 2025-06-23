package com.varunlegend.vplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.varunlegend.vplayer.adapters.MediaAdapter
import com.varunlegend.vplayer.databinding.ActivityMainBinding
import com.varunlegend.vplayer.utils.MediaUtils
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private val mediaList = mutableListOf<MediaItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        } else scanMedia()

        b.rvMedia.layoutManager = LinearLayoutManager(this)
        b.rvMedia.adapter = MediaAdapter(mediaList) { item ->
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra("uri", item.uri.toString())
            })
        }
    }

    override fun onRequestPermissionsResult(req: Int, perms: Array<String>, res: IntArray) {
        super.onRequestPermissionsResult(req, perms, res)
        if (res.isNotEmpty() && res[0] == PackageManager.PERMISSION_GRANTED) scanMedia()
    }

    private fun scanMedia() {
        mediaList.clear()
        listOf(
            File("/storage/emulated/0/Movies"),
            File("/storage/emulated/0/Music")
        ).filter { it.exists() }.flatMap { it.listFiles()?.toList() ?: emptyList() }
         .filter { it.extension.lowercase() in listOf("mp4","mkv","mp3","wav","avi","flac") }
         .forEach { file ->
            mediaList.add(MediaItemModel(
                file.nameWithoutExtension,
                MediaUtils.getDuration(this, Uri.fromFile(file)),
                Uri.fromFile(file)
            ))
        }
        b.rvMedia.adapter?.notifyDataSetChanged()
    }
}
