package com.example.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArtistModel(
    @PrimaryKey
    val artistName:String ,
    val artistSongs :MutableList<SongModel> ,
)
