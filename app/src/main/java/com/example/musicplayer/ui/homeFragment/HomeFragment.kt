package com.example.musicplayer.ui.homeFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.utilities.LAST_PLAYED_SONG
import com.example.musicplayer.utilities.PlayerEvents
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutFragmentId: Int = com.example.musicplayer.R.layout.fragment_home
    override val viewModel: MusicPlayerViewModel by activityViewModels()


    private lateinit var viewPagerAdapter: ViewPagerAdapter




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(this);
        binding.homeViewPager.adapter = viewPagerAdapter

        val tabTitles = listOf("Song", "Artist", "Album", "Playlist")

        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding.more.setOnClickListener {
            showPopupMenu(it);
        }

        binding.viewModel = viewModel

        binding.apply {
            homeNextSongImage.setOnClickListener { viewModel!!.onPlayerEvents(PlayerEvents.Next) }
            homeSongPlayPauseImage.setOnClickListener { viewModel!!.onPlayerEvents(PlayerEvents.PausePlay) }
        }


    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.home_three_dots, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.find_local_songs -> {

                    true
                }

                R.id.sort_by -> {

                    true
                }

                R.id.manage_songs -> {

                    true
                }

                R.id.settings -> {

                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }


}