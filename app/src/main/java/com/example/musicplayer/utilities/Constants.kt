package com.example.musicplayer.utilities

import android.net.Uri
import com.example.musicplayer.data.model.SongModel

val SongItemWithInitialValue = SongModel("" , "" , ""  , ""  , 0 ,"" +
        "" ,0 , "" , null )

const val SHARED_PREFERENCES_NAME = "MySharedPreferences"
const val IS_THE_FIRST_TIME = "first_time_open_the_app"
const val LAST_PLAYED_SONG = "last_played_song"
