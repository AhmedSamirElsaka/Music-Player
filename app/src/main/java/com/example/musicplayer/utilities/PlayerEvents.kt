package com.example.musicplayer.utilities

import com.example.musicplayer.data.model.SongModel

sealed class PlayerEvents {
    data class  AddPlaylist(val songs: List<SongModel> , val isUpdatePlaylistRequired:Boolean): PlayerEvents()
 //   data class  SeekProgress(val value: Long): PlayerEvents()

    data class GoToSpecificItem(val index:Int): PlayerEvents()

    object  PausePlay: PlayerEvents()
    object  Previous : PlayerEvents()
    object  Next : PlayerEvents()
    object  Shuffle : PlayerEvents()
    object  Repeat : PlayerEvents()
    object  SeekForward : PlayerEvents()
    object  SeekBack : PlayerEvents()

    data class  MoveToSpecificPosition(val position:Long) : PlayerEvents()

    object ClearMediaItems:PlayerEvents()

    data class GetThePositionOfSpecificSongInsideThePlaylist(val id:String) : PlayerEvents()
    data class AddSongToPlayNext(val id:String):PlayerEvents()
}
