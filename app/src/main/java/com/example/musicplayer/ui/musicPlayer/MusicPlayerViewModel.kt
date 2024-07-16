package com.example.musicplayer.ui.musicPlayer

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.player.player.PlayerController
import com.example.musicplayer.utilities.PlayerEvents
import com.example.musicplayer.utilities.SongItemWithInitialValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    player: ExoPlayer,
    @ApplicationContext context: Context,
    private var sharedPreferences: SharedPreferences
) : ViewModel() {

    private var _currentSong = MutableStateFlow(SongItemWithInitialValue)
    val currentSongFlow = _currentSong.asStateFlow()

    private var _isViewModelStart = MutableStateFlow(false)
    val isViewModelStart = _currentSong.asStateFlow()

    private var _currentMediaPosition = MutableStateFlow(0f)
    val currentMediaPosition = _currentMediaPosition.asStateFlow()

    private var _isPlayPlaying = MutableStateFlow(false)
    val isPlayPlaying = _isPlayPlaying.asStateFlow()

    private var _isPlayerBuffering = MutableStateFlow(false)
    val isPlayerBuffering = _isPlayerBuffering.asStateFlow()

    private var _isShuffleClicked = MutableStateFlow(false)
    val isShuffleClicked = _isShuffleClicked.asStateFlow()

    private var _isRepeatClick = MutableStateFlow(false)
    val isRepeatClick = _isRepeatClick.asStateFlow()

    private var _currentSongDurationInMinutes = MutableStateFlow(0L)
    val currentSongDurationInMinutes = _currentSongDurationInMinutes.asStateFlow()

    private var _currentSongProgressInMinutes = MutableStateFlow(0L)
    val currentSongProgressInMinutes = _currentSongProgressInMinutes.asStateFlow()


    private val playerController =
        PlayerController(
            player, _currentSong, _currentMediaPosition,
            _currentSongDurationInMinutes, _currentSongProgressInMinutes, _isPlayPlaying,
            _isPlayerBuffering, _isShuffleClicked, _isRepeatClick, viewModelScope, sharedPreferences
        )

    init {
        player.addListener(playerController)
        playerController.setupMediaNotification(context)
        Log.i("test", "test")
    }

    fun onPlayerEvents(event: PlayerEvents) {
        when (event) {
            is PlayerEvents.PausePlay -> playerController.pauseOrPlay()
            is PlayerEvents.GoToSpecificItem -> playerController.goToSpecificItem(event.index)
            is PlayerEvents.Next -> playerController.nextItem()
            is PlayerEvents.Previous -> playerController.previousItem()
            is PlayerEvents.Shuffle -> playerController.shuffleClick()
            is PlayerEvents.Repeat -> playerController.repeatClick()
            is PlayerEvents.AddPlaylist -> playerController.addPlaylist(event.songs)
            is PlayerEvents.SeekForward -> playerController.seekForward()
            is PlayerEvents.SeekBack -> playerController.seekBack()
            is PlayerEvents.MoveToSpecificPosition -> playerController.moveToSpecificPosition(event.position)
            is PlayerEvents.ClearMediaItems -> playerController.clearPlayer()
            is PlayerEvents.GetThePositionOfSpecificSongInsideThePlaylist -> playerController.findTrackIndexById(
                event.id
            )

            else -> {}
        }
    }
}

