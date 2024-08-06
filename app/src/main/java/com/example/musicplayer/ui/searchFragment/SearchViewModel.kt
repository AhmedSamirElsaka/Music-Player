package com.example.musicplayer.ui.searchFragment

import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.AlbumRepository
import com.example.musicplayer.data.source.ArtistRepository
import com.example.musicplayer.data.source.MusicRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) : BaseViewModel() {

    private var _audioListGroupedByArtist: MutableStateFlow<UiState<List<ArtistModel>>> =
        MutableStateFlow(UiState.Loading)

    val audioListGroupedByArtist = _audioListGroupedByArtist.asStateFlow()

    private var _audioList: MutableStateFlow<UiState<List<SongModel>>> =
        MutableStateFlow(UiState.Loading)

    val audioList = _audioList.asStateFlow()

    private var _albumList: MutableStateFlow<UiState<List<AlbumModel>>> =
        MutableStateFlow(UiState.Loading)

    val albumList = _albumList.asStateFlow()

    fun searchArtist(text: String) {
        viewModelScope.launch {
            _audioListGroupedByArtist.value = UiState.Loading
            val musicFlow = artistRepository.searchArtist(text)
            musicFlow.collect { resource ->
                _audioListGroupedByArtist.value = resource
            }
        }
    }

    fun searchAlbum(text: String) {
        viewModelScope.launch {
            _albumList.value = UiState.Loading
            val musicFlow = albumRepository.searchAlbum(text)
            musicFlow.collect { resource ->
                _albumList.value = resource
            }
        }
    }

    fun searchSong(text: String) {
        viewModelScope.launch {
            _audioList.value = UiState.Loading
            val musicFlow = musicRepository.searchSongs(text)
            musicFlow.collect { resource ->
                _audioList.value = resource
            }
        }
    }

    fun clearData() {
        _audioListGroupedByArtist.value = UiState.Loading
        _audioList.value = UiState.Loading
        _albumList.value = UiState.Loading
    }
}