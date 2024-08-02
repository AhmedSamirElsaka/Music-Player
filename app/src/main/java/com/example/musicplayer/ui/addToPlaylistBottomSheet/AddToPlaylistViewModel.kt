package com.example.musicplayer.ui.addToPlaylistBottomSheet

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.source.MusicRepository
import com.example.musicplayer.data.source.PlaylistRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToPlaylistViewModel  @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : BaseViewModel() {
    private var _playlists: MutableStateFlow<UiState<List<PlaylistModel>>> =
        MutableStateFlow(UiState.Loading)

    val playlists = _playlists.asStateFlow()

    fun getAllPlaylists() {
        viewModelScope.launch {
            _playlists.value = UiState.Loading
            val musicFlow = playlistRepository.getPlaylists()
            musicFlow.collect { resource ->
                _playlists.value = resource
                Log.i("ahmed", "getAllPlaylists: " + resource)
            }
        }
    }

}