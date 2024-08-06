package com.example.musicplayer.ui.searchFragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentSearchBinding
import com.example.musicplayer.ui.albumFragment.AlbumAdapter
import com.example.musicplayer.ui.albumFragment.OnAlbumListener
import com.example.musicplayer.ui.artistFragment.ArtistAdapter
import com.example.musicplayer.ui.artistFragment.OnArtistListener
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.homeFragment.HomeFragmentDirections
import com.example.musicplayer.ui.homeSongMoreButtonBottomSheet.HomeSongMoreButtonBottomSheet
import com.example.musicplayer.ui.musicBottomSheet.MusicBottomSheetFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.ui.songsFragment.OnSongsListener
import com.example.musicplayer.ui.songsFragment.SongsAdapter
import com.example.musicplayer.utilities.PlayerEvents
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(), OnSongsListener,
    OnArtistListener, OnAlbumListener {
    override val layoutFragmentId: Int = R.layout.fragment_search
    override val viewModel: SearchViewModel by viewModels()
    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var artistAdapter: ArtistAdapter
    private lateinit var albumAdapter: AlbumAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        songsAdapter = SongsAdapter(mutableListOf(), this, requireContext())
        artistAdapter = ArtistAdapter(mutableListOf(), this)
        albumAdapter = AlbumAdapter(mutableListOf(), this, requireContext())


        binding.AlbumsRv.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.songsRv.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        binding.artistsRv.apply {
            adapter = artistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioList.collect {
                if (it is UiState.Success && it.data.isNotEmpty()) {
                    songsAdapter.setData((it.data).sortedByDescending { it.songDateAdded })
                    Log.i("ahmed", "onViewCreated: " + it.data)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioListGroupedByArtist.collect {
                if (it is UiState.Success) {
                    artistAdapter.setData((it.data).sortedBy { it.artistName })
                    Log.i("ahmed", "onViewCreated: " + it.data)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.albumList.collect {
                if (it is UiState.Success) {
                    albumAdapter.setData((it.data).sortedBy { it.albumName })
                    Log.i("ahmed", "onViewCreated: " + it.data)
                }
            }
        }

        // Optionally, show the keyboard
        binding.searchEt.post {
            binding.searchEt.requestFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("ahmed", "onTextChanged: " + s)
                if (!s.isNullOrEmpty()) {
                    viewModel.searchSong("$s")
                    viewModel.searchAlbum("$s")
                    viewModel.searchArtist("$s")
                    binding.searchItemsGroup.visibility = View.VISIBLE
                }else {
                    binding.searchItemsGroup.visibility = View.GONE
                    viewModel.clearData()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onSongClick(song: SongModel, position: Int) {
        musicPlayerViewModel.onPlayerEvents(
            PlayerEvents.GetThePositionOfSpecificSongInsideThePlaylist(
                song.songId
            )
        )
        val musicBottomSheetFragment = MusicBottomSheetFragment()
        fragmentManager?.let { musicBottomSheetFragment.show(it, musicBottomSheetFragment.tag) }
    }

    override fun onMoreImageClick(song: SongModel) {
        val moreButtonBottomSheet = HomeSongMoreButtonBottomSheet.newInstance(song)
        fragmentManager?.let { moreButtonBottomSheet.show(it, moreButtonBottomSheet.tag) }
    }

    override fun onArtistClick(artistName: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToArtistsAndAlbumsSongFragment(
            false,
            "",
            artistName,
        )
        findNavController().navigate(action)
    }

    override fun onAlbumClick(albumName: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToArtistsAndAlbumsSongFragment(
            true,
            albumName,
            ""
        )
        findNavController().navigate(action)
    }


}