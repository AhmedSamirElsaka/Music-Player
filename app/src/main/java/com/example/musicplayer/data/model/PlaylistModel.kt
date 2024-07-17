package com.example.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistModel(
    @PrimaryKey
    val playlistName:String ,
    val playlistSongs :MutableList<SongModel> ,
)
