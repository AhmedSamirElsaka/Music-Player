package com.example.musicplayer.data.source


import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.local.MusicDao
import com.example.musicplayer.utilities.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


class MusicRepository @Inject constructor(
    private val musicDao: MusicDao,
    private var appContext: Context
) {


    private var _audioList: MutableStateFlow<UiState<List<SongModel>>> =
        MutableStateFlow(UiState.Loading)


    // caching
    fun getAudios(): Flow<UiState<List<SongModel>>> {
        return flow {
            emit(UiState.Loading)
            val cachedMusic = musicDao.getAllMusic()

            Log.i("hello", "getAudios: " + cachedMusic)
            if (cachedMusic.isNotEmpty()) {
                emit(UiState.Success(cachedMusic))
            }
            try {
                fetchAudioFilesFromDevice()
                coroutineScope {
                    _audioList.collect {
                        if (it is UiState.Success && it.data.isNotEmpty()) {
                            musicDao.insertAllMusic(it.data)
                            emit(it)
                        }
                    }
                }

            } catch (e: Exception) {
                emit(UiState.Error("Error fetching Musics"))
            }

        }.flowOn(Dispatchers.IO)
    }

    private fun fetchAudioFilesFromDevice() {
        val files = mutableListOf<SongModel>()

        CoroutineScope(Dispatchers.IO).launch {
            _audioList.value = UiState.Loading
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.ALBUM_ID,

                )

            // Specify the selection criteria
            val selection =
                "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND (" + "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/mpeg' OR " +  // MP3
                        "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/mp4' OR " +   // M4A
                        "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/aac' OR " +   // AAC
                        "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/ogg')"
            val selectionArgs = null

            val contentResolver: ContentResolver = appContext.contentResolver
            val cursor: Cursor? = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null
            )
            cursor?.use {
                while (it.moveToNext()) {
//                    Log.i("hello", "fetchAudioFilesFromDevice: " + it.position)
                    val songPath =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val songId = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val songName =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val songArtist =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val songDuration =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val songAlbum =
                        it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val songDateAdded =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
                    val songMimeType =
                        it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
                    val albumId =
                        cursor.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val songArt = getAlbumArtUri(albumId.toLong())
                    val song = SongModel(
                        songName,
                        songPath,
                        songId,
                        songArtist,
                        songDuration,
                        songAlbum,
                        songDateAdded,
                        songMimeType.toString(),
                        songArt.toString()
                    )
                    if (!song.songPath.contains("opus") && !song.songName.contains("AUD")) {
                        files.add(song)
                    }
                }
            }

            _audioList.value = UiState.Success(files)

        }
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        return Uri.parse("content://media/external/audio/albumart").buildUpon()
            .appendPath(albumId.toString()).build()
    }


    fun getSpecificArtistSongs(artistName: String): Flow<UiState<ArtistModel>> {
        return flow {
            emit(UiState.Loading)
            val artist = musicDao.getArtistByName(artistName)
            emit(UiState.Success(artist))
        }.flowOn(Dispatchers.IO)
    }



    fun getSpecificAlbumSongs(albumId: String): Flow<UiState<AlbumModel>> {
        return flow {
            emit(UiState.Loading)
            val album = musicDao.getAlbumById(albumId)
            emit(UiState.Success(album))
        }.flowOn(Dispatchers.IO)
    }

    fun getSpecificPlaylistsSongs(playlistName:String):Flow<UiState<PlaylistModel>>{
        return flow {
            emit(UiState.Loading)
            val album = musicDao.getPlaylistByName(playlistName)
            emit(UiState.Success(album))
        }.flowOn(Dispatchers.IO)
    }
}


