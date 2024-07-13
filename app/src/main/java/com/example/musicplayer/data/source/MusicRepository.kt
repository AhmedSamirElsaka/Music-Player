package com.example.musicplayer.data.source


import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.local.MusicDao
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val musicDao: MusicDao ,
     private  var appContext: Context
) {



    private var _audioList: MutableStateFlow<UiState<List<SongModel>>> =
        MutableStateFlow(UiState.Loading)


    // caching
    fun getMusics(): Flow<UiState<List<SongModel>>> {
        return flow {
            emit(UiState.Loading)
            val cachedMusic = musicDao.getAllMusic()
            if (cachedMusic.isNotEmpty()) {
                emit(UiState.Success(cachedMusic))
            }
            try {
                fetchAudioFiles()
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

    private fun fetchAudioFiles() {
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
                MediaStore.Audio.Media.MIME_TYPE
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
                        files.add(song)
                    }
                }
            }

            _audioList.value = UiState.Success(files)

        }
    }

    private fun getSongArtUri(songId: Long): Uri? {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/media"), songId)
    }
}


