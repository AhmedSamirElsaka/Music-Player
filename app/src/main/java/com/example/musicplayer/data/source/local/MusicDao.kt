package com.example.musicplayer.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.SongModel


@Dao
interface MusicDao {

    @Query("SELECT * FROM SongModel")
    suspend fun getAllMusic(): List<SongModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMusic(musics: List<SongModel>)



    @Query("SELECT * FROM SongModel WHERE songId = :songId")
    suspend fun getSpecificSong(songId:String): SongModel


    @Query("SELECT * FROM ArtistModel")
    suspend fun getAllArtists(): List<ArtistModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArtists(artists: List<ArtistModel>)

    @Query("SELECT * FROM ArtistModel WHERE artistName = :artistName")
    suspend fun getArtistByName(artistName: String): ArtistModel


    @Query("SELECT * FROM AlbumModel")
    suspend fun getAllAlbums(): List<AlbumModel>

    @Query("SELECT * FROM AlbumModel WHERE albumID = :albumId")
    suspend fun getAlbumById(albumId: String): AlbumModel

    @Query("SELECT * FROM AlbumModel WHERE albumName = :albumName")
    suspend fun getAlbumByName(albumName: String): AlbumModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlbums(albums: List<AlbumModel>)


    @Query("SELECT * FROM PlaylistModel")
    suspend fun getAllPlaylists(): List<PlaylistModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistModel)

    @Query("SELECT * FROM PlaylistModel WHERE playlistName = :name ")
    fun getPlaylistByName(name: String): PlaylistModel
}
