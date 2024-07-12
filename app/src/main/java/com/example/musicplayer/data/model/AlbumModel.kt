package com.example.musicplayer.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlbumModel(
    val albumName:String ,
    @PrimaryKey
    val albumID :String ,
    val albumCreator:String ,
    val albumArt:String? ,
    val albumSongs:MutableList<SongModel>
)
