package com.example.musicplayer.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel


@Dao
interface MusicDao {

    @Query("SELECT * FROM SongModel")
    suspend fun getAllMusic(): List<SongModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMusic(musics: List<SongModel>)


    @Query("SELECT * FROM ArtistModel")
     fun getAllArtists(): List<ArtistModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArtists(artists: List<ArtistModel>)

    @Query("SELECT * FROM AlbumModel")
    suspend fun getAllAlbums(): List<AlbumModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlbums(albums: List<AlbumModel>)

}
