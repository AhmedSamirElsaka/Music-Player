package com.example.musicplayer.ui.songsFragment

import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.MusicRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : BaseViewModel() {
    private var _audioList: MutableStateFlow<UiState<List<SongModel>>> =
        MutableStateFlow(UiState.Loading)

    val audioList = _audioList.asStateFlow()



    fun fetchAllMusics() {
        viewModelScope.launch {
            _audioList.value = UiState.Loading
            val musicFlow = musicRepository.getAudios()
            musicFlow.collect { resource ->
                _audioList.value = resource
            }
        }
    }
}