package com.example.musicplayer.ui.newPlaylistFragment

import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.MusicRepository
import com.example.musicplayer.data.source.PlaylistRepository
import com.example.musicplayer.ui.base.BaseViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddNewPlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : BaseViewModel() {

     var playListName: MutableStateFlow<String> =
        MutableStateFlow("test")



    fun addNewPlaylist(){
        if(playListName.value.isNotEmpty()){
            playlistRepository.addNewPlaylist(playListName.value)
        }
    }
}