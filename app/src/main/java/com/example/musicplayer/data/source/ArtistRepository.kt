package com.example.musicplayer.data.source


import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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


class ArtistRepository @Inject constructor(
    private val musicDao: MusicDao,
    private var appContext: Context
) {
    private var _audioListGroupedByArtist: MutableStateFlow<UiState<List<ArtistModel>>> =
        MutableStateFlow(UiState.Loading)


    // caching
    fun getArtists(): Flow<UiState<List<ArtistModel>>> {
        return flow {
            emit(UiState.Loading)
            val cachedArtists = musicDao.getAllArtists()
            if (cachedArtists.isNotEmpty()) {
                emit(UiState.Success(cachedArtists))
            }
            try {
                fetchArtistsFromDevice()
                coroutineScope {
                    _audioListGroupedByArtist.collect {
                        if (it is UiState.Success && it.data.isNotEmpty()) {
                            musicDao.insertAllArtists(it.data)
                            Log.i("hello", "getArtists: $it")
                            emit(it)
                        }
                    }
                }

            } catch (e: Exception) {
                emit(UiState.Error("Error fetching Musics"))
            }

        }.flowOn(Dispatchers.IO)
    }

    private fun fetchArtistsFromDevice() {

        val audioFilesGroupedByArtistHashMap = hashMapOf<String, ArtistModel>()
        val audioFilesGroupedByArtistList = mutableListOf<ArtistModel>()

        CoroutineScope(Dispatchers.IO).launch {
            _audioListGroupedByArtist.value = UiState.Loading
            val projection = arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.MIME_TYPE
            )

            // Specify the selection criteria
            val selection =
                "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND (" + "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/mpeg' OR " +  // MP3
                        "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/mp4' OR " +   // M4A
                        "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/aac' OR " +   // AAC
                        "${MediaStore.Audio.Media.MIME_TYPE} = 'audio/ogg')"
            val selectionArgs = null
            val sortOrder = MediaStore.Audio.Media.ARTIST + " DESC"

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
                        songMimeType.toString(),
                        songArt.toString()
                    )
                    if (!song.songPath.contains("opus") && !song.songName.contains("AUD")) {
                        if (!audioFilesGroupedByArtistHashMap.containsKey(songArtist)) {
                            audioFilesGroupedByArtistHashMap[songArtist] =
                                ArtistModel(songArtist, mutableListOf())
                        }
                        audioFilesGroupedByArtistHashMap[songArtist]?.artistSongs?.add(song)
                    }
                }
            }

            Log.i("hello", "getArtists: $audioFilesGroupedByArtistHashMap")

            audioFilesGroupedByArtistHashMap.forEach { (t, u) ->
                audioFilesGroupedByArtistList.add(u)
            }

            _audioListGroupedByArtist.value = UiState.Success(audioFilesGroupedByArtistList.toSet().toList())

        }
    }

    private fun getSongArtUri(songId: Long): Uri? {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/media"), songId)
    }
}

