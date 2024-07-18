package com.example.musicplayer.ui.artistsAndAlbumsAndPlaylistsSongs

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.source.MusicRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
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

    private var _playlistsList: MutableStateFlow<UiState<PlaylistModel>> =
        MutableStateFlow(UiState.Loading)

    val playlistsList = _playlistsList.asStateFlow()


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
            val musicFlow = musicRepository.getSpecificArtistSongs(artistName)
            musicFlow.collect { resource ->
                _artistAudioList.value = resource
                Log.i("hello", "getSpecificSongsByAlbumId: " + resource)
            }
        }
    }

    fun getSpecificPlaylistSongsByName(playlistName: String) {
        viewModelScope.launch {
            _playlistsList.value = UiState.Loading
            val musicFlow = musicRepository.getSpecificPlaylistsSongs(playlistName)
            musicFlow.collect { resource ->
                _playlistsList.value = resource
            }
        }
    }

}