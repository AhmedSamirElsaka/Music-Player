package com.example.musicplayer.ui.albumFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.databinding.FragmentAlbumBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.homeFragment.HomeFragmentDirections
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : BaseFragment<FragmentAlbumBinding>(), OnAlbumListener {
    override val layoutFragmentId: Int = R.layout.fragment_album
    override val viewModel: AlbumViewModel by viewModels()
    private lateinit var albumAdapter: AlbumAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadAlbumsFiles()


        albumAdapter = AlbumAdapter(mutableListOf(), this, requireContext())


        binding.AlbumsRv.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(context)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.albumList.collect {
                if (it is UiState.Success) {
                    albumAdapter.setData((it.data).sortedBy { it.albumName })
                    "${it.data.size} songs".also { binding.albumsCountTv.text = it }
                }
            }
        }
    }

    override fun onAlbumClick(albumId: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToArtistsAndAlbumsSongFragment(true , albumId , "")
        findNavController().navigate(action)
    }
}