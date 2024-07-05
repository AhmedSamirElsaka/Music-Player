package com.example.musicplayer.data.model

import android.net.Uri

data class AlbumModel(
    val albumName:String ,
    val albumID :String ,
    val albumCreator:String ,
    val albumArt:Uri? ,
    val albumSongs:MutableList<SongModel>
)
