package com.example.musicplayer.ui.artistsAndAlbumsSongs

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.MusicRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsAndAlbumsSongsViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : BaseViewModel() {

    private var _artistAudioList: MutableStateFlow<UiState<ArtistModel>> =
        MutableStateFlow(UiState.Loading)

    val artistAudioList = _artistAudioList.asStateFlow()


    private var _albumAudioList: MutableStateFlow<UiState<AlbumModel>> =
        MutableStateFlow(UiState.Loading)

    val albumAudioList = _albumAudioList.asStateFlow()


    fun getSpecificSongsByAlbumId(albumId: String) {
        viewModelScope.launch {
            _albumAudioList.value = UiState.Loading
            val musicFlow = musicRepository.getSpecificAlbumSongs(albumId)
            musicFlow.collect { resource ->
                _albumAudioList.value = resource
                Log.i("hello", "getSpecificSongsByAlbumId: " + resource)
            }
        }
    }

    fun getSpecificSongsByArtistId(artistName: String) {
        viewModelScope.launch {
            _artistAudioList.value = UiState.Loading
            val musicFlow = musicRepository.getSpecifiArtistSongs(artistName)
            musicFlow.collect { resource ->
                _artistAudioList.value = resource
                Log.i("hello", "getSpecificSongsByAlbumId: " + resource)
            }
        }
    }

}