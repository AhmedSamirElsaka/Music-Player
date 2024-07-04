package com.example.musicplayer.data.model

data class AlbumModel(
    val albumName:String ,
    val albumID :String ,
    val albumCreator:String ,
    val albumArt:String? ,
    val albumSongs:MutableList<SongModel>
)
