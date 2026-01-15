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
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.varunlegend.vplayer.adapters.MediaAdapter
import com.varunlegend.vplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private val mediaList = mutableListOf<MediaItemModel>()
    private val PERM_REQ = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.rvMedia.layoutManager = LinearLayoutManager(this)
        b.rvMedia.adapter = MediaAdapter(mediaList) { item ->
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra("uri", item.uri.toString())
            })
        }

        checkAndRequestPerms()
    }

    private fun checkAndRequestPerms() {
        val perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val missing = perms.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isEmpty()) {
            scanMedia()
        } else {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), PERM_REQ)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, perms: Array<out String>, results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, perms, results)
        if (requestCode == PERM_REQ &&
            results.isNotEmpty() &&
            results.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            scanMedia()
        } else {
            Toast.makeText(this, "Permission required to list media", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scanMedia() {
        mediaList.clear()
        val uriVideos = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val proj = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION
        )
        contentResolver.query(uriVideos, proj, null, null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC")?.use { cursor ->
            val idCol   = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durCol  = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (cursor.moveToNext()) {
                val id   = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val dur  = cursor.getLong(durCol)
                val contentUri = Uri.withAppendedPath(uriVideos, id.toString())
                mediaList.add(MediaItemModel(name, formatDuration(dur), contentUri))
            }
        }
        b.rvMedia.adapter?.notifyDataSetChanged()
    }

    private fun formatDuration(ms: Long): String =
        String.format("%02d:%02d", ms / 60000, (ms / 1000) % 60)
}
