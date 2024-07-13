package com.example.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomArtistModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0 ,
    val artistsHashMap:HashMap<String,ArtistModel>
)