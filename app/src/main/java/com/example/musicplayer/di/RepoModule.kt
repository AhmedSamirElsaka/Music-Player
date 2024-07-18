package com.example.musicplayer.di

import android.content.Context
import com.example.musicplayer.data.source.AlbumRepository
import com.example.musicplayer.data.source.ArtistRepository
import com.example.musicplayer.data.source.local.MusicDao
import com.example.musicplayer.data.source.MusicRepository
import com.example.musicplayer.data.source.PlaylistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideSongsRepo( musicDao: MusicDao , @ApplicationContext appContext: Context): MusicRepository {
        return MusicRepository(musicDao , appContext)
    }

    @Provides
    fun provideArtistsRepo( musicDao: MusicDao , @ApplicationContext appContext: Context): ArtistRepository {
        return ArtistRepository(musicDao , appContext)
    }

    @Provides
    fun provideAlbumsRepo( musicDao: MusicDao , @ApplicationContext appContext: Context): AlbumRepository {
        return AlbumRepository(musicDao , appContext)
    }

    @Provides
    fun providePlaylistsRepo( musicDao: MusicDao , @ApplicationContext appContext: Context): PlaylistRepository {
        return PlaylistRepository(musicDao , appContext)
    }
}