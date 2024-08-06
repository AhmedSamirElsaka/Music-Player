package com.example.musicplayer.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class AlbumModel(
    val albumName:String ,
    @PrimaryKey
    val albumID :String ,
    val albumCreator:String ,
    val albumArt:String? ,
    val albumSongs:MutableList<SongModel>
): Parcelable
