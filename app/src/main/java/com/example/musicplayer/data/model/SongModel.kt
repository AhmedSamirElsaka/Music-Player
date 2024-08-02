package com.example.musicplayer.data.model

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class SongModel(
    val songName: String,
    val songPath: String,
    @PrimaryKey
    val songId: String,
    val songArtist: String ,
    val songDuration :Long ,
    val songAlbum:String ,
    val songDateAdded :Long ,
    val songMimeType:String ,
    val songArt: String?
):Parcelable
