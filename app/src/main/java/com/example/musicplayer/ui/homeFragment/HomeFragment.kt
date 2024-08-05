package com.example.musicplayer.ui.homeFragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.musicBottomSheet.MusicBottomSheetFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.utilities.PlayerEvents
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), ClickHandlers {
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
        binding.clickHandler = this

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
                    val action = HomeFragmentDirections.actionHomeFragmentToScanLocalMusicFragment()
                    findNavController().navigate(action)
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

    override fun showMusicBottomSheet() {
        val musicBottomSheetFragment = MusicBottomSheetFragment()
        fragmentManager?.let { musicBottomSheetFragment.show(it, musicBottomSheetFragment.tag) }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//
//        inflater.inflate(R.menu.search_item_menu, menu)
//        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView
//
//        searchView.queryHint = "songs "
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // Handle search query submission
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // Handle search text change
//                return false
//            }
//        })
//    }
//    override fun onCreateOptionsMenu(menu: Menu? , inflater: MenuInflater ): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView
//
//        searchView.queryHint = getString(R.string.search_hint)
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // Handle search query submission
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // Handle search text change
//                return false
//            }
//        })
//        return true
//    }

}