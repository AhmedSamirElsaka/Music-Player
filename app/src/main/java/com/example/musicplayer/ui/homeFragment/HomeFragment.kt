package com.example.musicplayer.ui.homeFragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModel
import com.example.musicplayer.R
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

        binding.more.setOnClickListener {
            showPopupMenu(it);
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.home_three_dots , popupMenu.menu)
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