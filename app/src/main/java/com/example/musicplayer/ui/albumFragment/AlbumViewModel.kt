package com.example.musicplayer.ui.albumFragment

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.AlbumModel
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
class AlbumViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : BaseViewModel() {
    private var _albumList: MutableStateFlow<UiState<HashMap<String, AlbumModel>>> =
        MutableStateFlow(UiState.Loading)

    val albumList = _albumList.asStateFlow()

    private var _albumArts: MutableStateFlow<UiState<HashMap<String, String>>> =
        MutableStateFlow(UiState.Loading)

    val albumArts = _albumArts.asStateFlow()


    fun loadAlbumsFiles() {
        val albumFiles = hashMapOf<String, AlbumModel>()

        viewModelScope.launch {
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
                    val albumId =
                        cursor.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val album =
                        cursor.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))

                    if (!song.songPath.contains("opus") && !song.songName.contains("AUD")) {
                        if (!albumFiles.containsKey(albumId)) {
//                            val albumArt: String? = getAlbumArt(albumId)
                            albumFiles[album] =
                                AlbumModel(album, albumId, songArtist, null, mutableListOf())
                        }
                        albumFiles[album]?.albumSongs?.add(song)
                    }
                }
            }
            _albumList.value = UiState.Success(albumFiles)
        }
    }

    fun getAlbumArts() {
        val albumArts = hashMapOf<String, String>()
        viewModelScope.launch {

            val contentResolver: ContentResolver = appContext.contentResolver
            val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ALBUM_ID,
            )
            val selection = MediaStore.Audio.Albums._ID + " = ?"
//        val selectionArgs = arrayOf(albumId)
            val cursor = contentResolver.query(uri, projection, selection, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val albumArt =
                    cursor.getString(cursor.run { getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART) })
                Log.i("hello", "getAlbumArts: $albumArt")
                val albumId =
                    cursor.getString(cursor.run { getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID) })

                if (!albumArts.containsKey(albumId)) {
                    albumArts[albumId] = albumArt
                }
                cursor.close()
                _albumArts.value = UiState.Success(albumArts)
            }
        }
    }

}



