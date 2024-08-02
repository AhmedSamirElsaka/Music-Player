package com.example.musicplayer.ui.playlistFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.databinding.FragmentPlaylistBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.homeFragment.HomeFragmentDirections
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>(), OnPlaylistsListener {
    override val layoutFragmentId: Int = R.layout.fragment_playlist
    override val viewModel: PlaylistViewModel by viewModels()
    private lateinit var playListAdapter: PlaylistAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playListAdapter = PlaylistAdapter(mutableListOf(), this)

        viewModel.fetchAllPlaylists()


        binding.playlistRv.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlists.collect {
                if (it is UiState.Success && it.data.isNotEmpty()) {

                    Log.i("ahmed", "onViewCreated: " + it )
                    playListAdapter.setData(
                        (it.data.toMutableList().apply {
                            add(
                                PlaylistModel(
                                    "",
                                    mutableListOf()
                                )
                            )
                        }.toList()).sortedByDescending { it.playlistName })
                }
            }
        }


    }

    override fun onPlayListClick(playlist: PlaylistModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToArtistsAndAlbumsSongFragment(
            true,
            "",
            "",
            playlist.playlistName
        )
        findNavController().navigate(action)
    }

    override fun onAddNewPlaylistClick() {
        val action = HomeFragmentDirections.actionHomeFragmentToNewPlaylistFragment()
        findNavController().navigate(action)
    }
}