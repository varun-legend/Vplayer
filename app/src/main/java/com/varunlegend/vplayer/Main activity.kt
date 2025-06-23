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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mediaList = mutableListOf<MediaItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        setupRecyclerView()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        } else loadMedia()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array< String>, grantResults: IntArray) {
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMedia()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupRecyclerView() {
        binding.rvMedia.layoutManager = LinearLayoutManager(this)
        binding.rvMedia.adapter = MediaAdapter(mediaList) { uri ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("uri", uri.toString())
            startActivity(intent)
        }
    }

    private fun loadMedia() {
        MediaUtils.fetchLocalMedia(this).forEach { item ->
            mediaList.add(item)
        }
        binding.rvMedia.adapter?.notifyDataSetChanged()
    }
}
