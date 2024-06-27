package com.example.musicplayer.ui.homeFragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.musicplayer.ui.albumFragment.AlbumFragment
import com.example.musicplayer.ui.artistFragment.ArtistFragment
import com.example.musicplayer.ui.playlistFragment.PlaylistFragment
import com.example.musicplayer.ui.songsFragment.SongsFragment


class ViewPagerAdapter(fragmentActivity: HomeFragment) : FragmentStateAdapter(fragmentActivity) {

    private val fragments: List<Fragment> = listOf(
        SongsFragment(),
        ArtistFragment(),
        AlbumFragment(),
        PlaylistFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}