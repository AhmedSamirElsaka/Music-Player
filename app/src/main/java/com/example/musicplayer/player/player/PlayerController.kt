package com.example.musicplayer.player.player

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.utilities.LAST_PLAYED_SONG
import com.example.musicplayer.utilities.toMusicItem
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlayerController(
    private val player: ExoPlayer,
    private var currentSong: MutableStateFlow<SongModel>,
    private var currentMediaPosition: MutableStateFlow<Float>,
    private var currentMediaDurationInMinutes: MutableStateFlow<Long>,
    private var currentMediaProgressInMinutes: MutableStateFlow<Long>,
    private var isPausePlayClicked: MutableStateFlow<Boolean>,
    private var isPlayerBuffering: MutableStateFlow<Boolean>,
    private var isShuffleClicked: MutableStateFlow<Boolean>,
    private var isRepeatClicked: MutableStateFlow<Boolean>,
    private val viewModelScope: CoroutineScope,
    private var sharedPreferences: SharedPreferences
) : Player.Listener {

    var duration: Long = 0

    private lateinit var controller: ListenableFuture<MediaController>


    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        currentMediaPosition.value = 0f

        if (mediaItem != null) {
            currentSong.value = toMusicItem(mediaItem)
            saveFloatValue(player.currentMediaItemIndex.toFloat())
            Log.i("hello", "onMediaItemTransition: " + player.currentMediaItemIndex.toFloat())
        }

    }

    // Function to find the index of the track by ID or name
    fun findTrackIndexById(trackId: String): Int {

        for (i in 0 until player.mediaItemCount) {
            val mediaItem = player.getMediaItemAt(i)
            if (mediaItem.mediaId.equals(trackId)) {
                goToSpecificItem(i)
                return i
            }
        }
        return -1 // Track not found
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        isPausePlayClicked.value = isPlaying
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when (playbackState) {
            Player.STATE_ENDED -> {
                if (player.hasNextMediaItem()) {
                    if (player.hasNextMediaItem()) nextItem()
                    saveFloatValue(player.currentMediaItemIndex.toFloat())
                }
            }

            Player.STATE_BUFFERING -> {
                isPlayerBuffering.value = true
            }

            Player.STATE_IDLE -> {
                currentMediaProgressInMinutes.value = 0L
                currentMediaDurationInMinutes.value = 0L
                isPlayerBuffering.value = false
            }

            Player.STATE_READY -> {
                isPlayerBuffering.value = false
            }
        }
    }


    fun pauseOrPlay() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        currentSong.value = toMusicItem(player.currentMediaItem!!)
    }

    fun shuffleClick() {
        if (isShuffleClicked.value) {
            isShuffleClicked.value = false
            player.shuffleModeEnabled = isShuffleClicked.value
        } else {
            isShuffleClicked.value = true
            player.shuffleModeEnabled = isShuffleClicked.value
        }
    }

    fun repeatClick() {
        if (isRepeatClicked.value) {
            isRepeatClicked.value = false
            player.repeatMode = Player.REPEAT_MODE_OFF
        } else {
            isRepeatClicked.value = true
            player.repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    fun seekForward() {
        player.seekForward()
    }

    fun seekBack() {
        player.seekBack()
    }


    fun clearPlayer() {
        player.stop()
        player.clearMediaItems()
    }

    fun addPlaylist(itemList: List<SongModel>, updatePlaylistRequired: Boolean) {
        if (updatePlaylistRequired) {
            for (item in itemList) {
                val metadata = getMetaDataFromItem(item)
                val mediaItem = MediaItem.Builder().apply {
                    setUri(item.songPath)
                    setMediaId(item.songId)
                    setMediaMetadata(metadata)

                }.build()
                player.addMediaItem(mediaItem)
            }
            player.prepare()
            player.pause()
        } else {
            if (player.mediaItemCount <= 0) {
                for (item in itemList) {
                    val metadata = getMetaDataFromItem(item)
                    val mediaItem = MediaItem.Builder().apply {
                        setUri(item.songPath)
                        setMediaId(item.songId)
                        setMediaMetadata(metadata)

                    }.build()
                    player.addMediaItem(mediaItem)
                }
                player.prepare()
                player.pause()
            }
        }
    }

    fun nextItem() {
        if (player.hasNextMediaItem()) player.seekToNextMediaItem()
    }

    fun goToSpecificItem(index: Int) {
        player.seekTo(index, 0L)
        player.play()
        currentSong.value = toMusicItem(player.currentMediaItem!!)

        saveFloatValue(player.currentMediaItemIndex.toFloat())
//        Log.i("hello", "goToSpecificItem: " + player.currentMediaItemIndex.toFloat())
    }

    fun previousItem() {
        if (player.hasPreviousMediaItem()) player.seekToPreviousMediaItem()
    }


    fun moveToSpecificPosition(position: Long) {
        player.seekTo(position)
    }

    private fun getMetaDataFromItem(item: SongModel): MediaMetadata {
        return MediaMetadata.Builder()
            .setTitle(item.songName)
            .setAlbumTitle(item.songAlbum)
            .setDisplayTitle(item.songName)
            .setArtist(item.songArtist)
            .setAlbumArtist(item.songArtist)
            .setArtworkUri(Uri.parse(item.songArt))
            .build()
    }


    fun updatePlayerSeekProgress(pos: Long) {
        currentMediaProgressInMinutes.value = pos
        val progress = pos.toFloat() / duration.toFloat()
        if (!progress.isNaN()) currentMediaPosition.value = progress
    }


    fun setupMediaNotification(context: Context) {

        val sessionToken = SessionToken(context, ComponentName(context, MediaService::class.java))
        controller = MediaController.Builder(context, sessionToken).buildAsync()
        controller.addListener({

            val mediaController = controller.get()

            mediaController.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    currentSong.value = toMusicItem(player.currentMediaItem!!)
//                    saveFloatValue(player.currentMediaItemIndex.toFloat())

                    isPausePlayClicked.value = isPlaying
                    duration = mediaController.duration
                    if (duration == -9223372036854775807) duration = 0
                    currentMediaDurationInMinutes.value = duration
                    viewModelScope.launch {
                        while (isPausePlayClicked.value) {
                            currentSong.value = toMusicItem(player.currentMediaItem!!)
//                            saveFloatValue(player.currentMediaItemIndex.toFloat())
                            updatePlayerSeekProgress(player.currentPosition)
                            delay(1000)
                        }
                    }
                }


                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    when (reason) {
                        Player.DISCONTINUITY_REASON_SEEK -> {
                            updatePlayerSeekProgress(newPosition.contentPositionMs)
                            player.seekTo(newPosition.contentPositionMs)
                        }

                        Player.DISCONTINUITY_REASON_AUTO_TRANSITION -> Unit
                        Player.DISCONTINUITY_REASON_INTERNAL -> Unit
                        Player.DISCONTINUITY_REASON_REMOVE -> Unit
                        Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT -> Unit
                        Player.DISCONTINUITY_REASON_SKIP -> Unit
                    }
                }
            })
        }, MoreExecutors.directExecutor())
    }

    private fun saveFloatValue(value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(LAST_PLAYED_SONG, value)
        editor.apply()
    }


}