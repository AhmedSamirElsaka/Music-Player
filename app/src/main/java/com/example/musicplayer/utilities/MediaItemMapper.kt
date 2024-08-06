package com.example.musicplayer.utilities

import androidx.media3.common.MediaItem
import com.example.musicplayer.data.model.SongModel

fun toMusicItem(mediaItem: MediaItem): SongModel {
    val songPath = mediaItem.mediaMetadata.extras?.getString("KEY_SONG_PATH") ?: ""
    return SongModel(
        songAlbum = mediaItem.mediaMetadata.albumTitle.toString(),
        songArtist = mediaItem.mediaMetadata.artist.toString(),
        songDuration = 0,
        songId = mediaItem.mediaId,
        songArt = mediaItem.mediaMetadata.artworkUri.toString(),
        songName = mediaItem.mediaMetadata.displayTitle.toString(),
        songMimeType = "mp3",
        songDateAdded = 0,
        songPath = songPath // Include songPath
    )
}