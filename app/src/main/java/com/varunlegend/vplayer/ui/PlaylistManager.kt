package com.varunlegend.vplayer.ui
import android.content.Context
import android.net.Uri
object PlaylistManager {
    fun savePlaylist(ctx: Context, name: String, items: List<Uri>) { /* TODO */ }
    fun loadPlaylists(ctx: Context): Map<String, List<Uri>> = emptyMap()
}
