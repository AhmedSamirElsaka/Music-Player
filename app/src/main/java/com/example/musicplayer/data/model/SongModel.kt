package com.example.musicplayer.data.model

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts

data class SongModel(
    val songName: String,
    val songPath: String,
    val songId: String,
    val songArtist: String ,
    val songDuration :Long ,
    val songAlbum:String ,
    val songDateAdded :Long ,
    val songMimeType:String ,
    val songArt: Uri?
)
