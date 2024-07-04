package com.example.musicplayer.ui.artistFragment

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtistViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : BaseViewModel() {
    private var _audioListGroupedByArtist: MutableStateFlow<UiState<HashMap<String, ArtistModel>>> =
        MutableStateFlow(UiState.Loading)

    val audioListGroupedByArtist = _audioListGroupedByArtist.asStateFlow()

    fun loadArtistsFiles() {
        val audioFilesGroupedByArtist = hashMapOf<String, ArtistModel>()

        viewModelScope.launch {
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
                    val song = SongModel(
                        songName,
                        songPath,
                        songId,
                        songArtist,
                        songDuration,
                        songAlbum,
                        songDateAdded,
                        songMimeType.toString()
                    )
                    if (!song.songPath.contains("opus") && !song.songName.contains("AUD")) {
                        if (!audioFilesGroupedByArtist.containsKey(songArtist)) {
                            audioFilesGroupedByArtist[songArtist] =
                                ArtistModel(songArtist, mutableListOf())
                        }
                        audioFilesGroupedByArtist[songArtist]?.artistSongs?.add(song)
//                        audioFilesGroupedByArtist[songArtist]?.add(ArtistModel(songArtist,
//                            audioFilesGroupedByArtist[songArtist]?.artistSongs.add(song)))
                    }
                }
            }

            _audioListGroupedByArtist.value = UiState.Success(audioFilesGroupedByArtist)

        }
    }
}

