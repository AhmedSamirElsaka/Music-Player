package com.example.musicplayer.ui.albumFragment

import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.source.AlbumRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : BaseViewModel() {
    private var _albumList: MutableStateFlow<UiState<List<AlbumModel>>> =
        MutableStateFlow(UiState.Loading)

    val albumList = _albumList.asStateFlow()

    private var _albumArts: MutableStateFlow<UiState<HashMap<String, String>>> =
        MutableStateFlow(UiState.Loading)



    fun loadAlbumsFiles() {
        viewModelScope.launch {
            _albumList.value = UiState.Loading
            val musicFlow = albumRepository.getAlbums()
            musicFlow.collect { resource ->
                _albumList.value = resource
            }
        }
        }
}





