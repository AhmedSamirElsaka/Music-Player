
package com.example.musicplayer.data.source


import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
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


class AlbumRepository @Inject constructor(
    private val musicDao: MusicDao ,
    private  var appContext: Context
) {



    private var _albumList: MutableStateFlow<UiState<List<AlbumModel>>> =
        MutableStateFlow(UiState.Loading)


    // caching
    fun getAlbums(): Flow<UiState<List<AlbumModel>>> {
//        Log.i("hello", "getArtists: ")
        return flow {
            emit(UiState.Loading)
            val cachedAlbums = musicDao.getAllAlbums()
            if (cachedAlbums.isNotEmpty()) {
                emit(UiState.Success(cachedAlbums))
                return@flow
            }
            try {
                fetchAlbumFilesFromDevice()
                coroutineScope {
                    _albumList.collect {
                        if (it is UiState.Success && it.data.isNotEmpty()) {
                            musicDao.insertAllAlbums(it.data)
                            emit(it)
                        }
                    }
                }

            } catch (e: Exception) {
                emit(UiState.Error("Error fetching Musics"))
            }

        }.flowOn(Dispatchers.IO)
    }

    private fun fetchAlbumFilesFromDevice() {
        val albumFilesHashMap = hashMapOf<String, AlbumModel>()
        val albumFilesList = mutableListOf<AlbumModel>()

        CoroutineScope(Dispatchers.IO).launch {
            _albumList.value = UiState.Loading
            val projection = arrayOf(
                MediaStore.Audio.Media.TITLE,
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
            val sortOrder = MediaStore.Audio.Media.ALBUM + " DESC"

            val contentResolver: ContentResolver = appContext.contentResolver
            val cursor: Cursor? = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, sortOrder
            )

            cursor?.use {
                while (it.moveToNext()) {
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
                    val songArt = getSongArtUri(songId.toLong())

                    val song = SongModel(
                        songName,
                        songPath,
                        songId,
                        songArtist,
                        songDuration,
                        songAlbum,
                        songDateAdded,
                        songMimeType.toString() ,
                        songArt.toString()
                    )
                    val albumId =
                        cursor.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val album =
                        cursor.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))

                    if (!song.songPath.contains("opus") && !song.songName.contains("AUD")) {
                        if (!albumFilesHashMap.containsKey(albumId)) {
                            val albumArt = getAlbumArtUri(albumId.toLong())
                            albumFilesHashMap[album] =
                                AlbumModel(album, albumId, songArtist, albumArt.toString(), mutableListOf())
                        }
                        albumFilesHashMap[album]?.albumSongs?.add(song)
                    }
                }
            }

            albumFilesHashMap.forEach { (t, u) ->
                albumFilesList.add(u)
            }

            _albumList.value = UiState.Success(albumFilesList.toSet().toList())
        }
    }

    private fun getAlbumArtUri(albumId: Long): Uri? {
        return Uri.parse("content://media/external/audio/albumart").buildUpon()
            .appendPath(albumId.toString()).build()
    }

    private fun getSongArtUri(songId: Long): Uri? {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/media"), songId)
    }


    fun searchAlbum(text: String): Flow<UiState<List<AlbumModel>>> {
        return flow {
            emit(UiState.Loading)
            val cachedArtists = musicDao.searchAlbumsByName(text)
            if (cachedArtists.isNotEmpty()) {
                emit(UiState.Success(cachedArtists))
                return@flow
            }
        }
    }
}
