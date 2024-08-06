package com.example.musicplayer.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ArtistModel(
    @PrimaryKey
    val artistName:String ,
    val artistSongs :MutableList<SongModel> ,
): Parcelable
