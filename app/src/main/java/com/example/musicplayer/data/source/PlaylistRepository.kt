package com.example.musicplayer.data.source

import android.content.Context
import android.util.Log
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.local.MusicDao
import com.example.musicplayer.utilities.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val musicDao: MusicDao,
    private var appContext: Context
) {
    private var _playlists: MutableStateFlow<UiState<List<PlaylistModel>>> =
        MutableStateFlow(UiState.Loading)


    // caching
    fun getPlaylists(): Flow<UiState<List<PlaylistModel>>> {
        return flow {
            emit(UiState.Loading)
            val cachedPlaylists = musicDao.getAllPlaylists()
            if (cachedPlaylists.isNotEmpty()) {
                emit(UiState.Success(cachedPlaylists))
                Log.i("hello", "getPlaylists: " + cachedPlaylists)
            } else {
                musicDao.insertPlaylist(PlaylistModel("Liked", mutableListOf()))
                musicDao.insertPlaylist(PlaylistModel("Recently Played", mutableListOf()))
                emit(
                    UiState.Success(
                        listOf(
                            PlaylistModel("Liked", mutableListOf()),
                            PlaylistModel("Recently Played", mutableListOf())
                        )
                    )
                )

            }
        }.flowOn(Dispatchers.IO)
    }

    fun addNewPlaylist(playlistName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            musicDao.insertPlaylist(PlaylistModel(playlistName, mutableListOf()))
        }
    }

    fun addSongToPlaylist(song:SongModel ,  playList: PlaylistModel) {
        CoroutineScope(Dispatchers.IO).launch {
           playList.playlistSongs.add(song)
            musicDao.insertPlaylist(playList)
        }
    }


}