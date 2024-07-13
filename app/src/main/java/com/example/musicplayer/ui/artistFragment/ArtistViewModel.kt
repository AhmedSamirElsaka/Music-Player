package com.example.musicplayer.ui.artistFragment

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.source.ArtistRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : BaseViewModel() {
    private var _audioListGroupedByArtist: MutableStateFlow<UiState<List< ArtistModel>>> =
        MutableStateFlow(UiState.Loading)

    val audioListGroupedByArtist = _audioListGroupedByArtist.asStateFlow()

    fun loadArtistsFiles() {
            viewModelScope.launch {
                _audioListGroupedByArtist.value = UiState.Loading
                val musicFlow = artistRepository.getArtists()
//                Log.i("hello", "fetchAllMusics: $musicFlow")
                musicFlow.collect { resource ->
                    _audioListGroupedByArtist.value = resource
                }

        }
    }


}

