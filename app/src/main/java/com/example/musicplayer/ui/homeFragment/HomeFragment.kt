package com.example.musicplayer.ui.homeFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
 class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutFragmentId: Int = com.example.musicplayer.R.layout.fragment_home


     override val viewModel: ViewModel
         get() = TODO()

    private lateinit  var viewPagerAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter =  ViewPagerAdapter(this);
        binding.homeViewPager.adapter = viewPagerAdapter

        val tabTitles = listOf("Song", "Artist", "Album", "Playlist")

        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}