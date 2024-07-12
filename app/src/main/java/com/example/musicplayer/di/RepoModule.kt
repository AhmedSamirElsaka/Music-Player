package com.example.musicplayer.di

import android.content.Context
import com.example.musicplayer.data.source.local.MusicDao
import com.example.musicplayer.data.source.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideRepo( musicDao: MusicDao , @ApplicationContext appContext: Context): MusicRepository {
        return MusicRepository(musicDao , appContext)
    }
}