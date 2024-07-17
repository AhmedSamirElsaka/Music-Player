package com.example.musicplayer.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.RoomArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.local.MusicDao


@Database(entities = [SongModel::class , ArtistModel::class , AlbumModel::class , PlaylistModel::class],version = 2)
@TypeConverters(value = [RoomTypeConverters::class])
abstract class MusicDataBase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}