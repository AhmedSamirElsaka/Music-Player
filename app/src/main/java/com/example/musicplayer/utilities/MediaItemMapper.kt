package com.example.musicplayer.utilities

import androidx.media3.common.MediaItem
import com.example.musicplayer.data.model.SongModel

fun toMusicItem(mediaItem: MediaItem): SongModel =
    SongModel(
        songAlbum = mediaItem.mediaMetadata.albumArtist.toString(),
        songArtist = mediaItem.mediaMetadata.artist.toString(),
        songDuration = 0,
        songId = mediaItem.mediaId,
        songArt = mediaItem.mediaMetadata.artworkUri.toString(),
        songName = mediaItem.mediaMetadata.displayTitle.toString(),
        songPath = mediaItem.requestMetadata.mediaUri.toString(),
        songMimeType = "mp3",
        songDateAdded = 0
    )
