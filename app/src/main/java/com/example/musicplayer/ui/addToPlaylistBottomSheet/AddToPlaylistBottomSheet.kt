package com.example.musicplayer.ui.addToPlaylistBottomSheet

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.AddToPlaylistBottomSheetBinding
import com.example.musicplayer.databinding.AddToPlaylistRvItemBinding
import com.example.musicplayer.ui.artistsAndAlbumsAndPlaylistsSongs.ArtistsAndAlbumsSongsViewModel
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.ui.songsFragment.SongsAdapter
import com.example.musicplayer.utilities.UiState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddToPlaylistBottomSheet(private val song: SongModel) :
    BottomSheetDialogFragment() , OnAddToPlaylistListener{
    private val layoutFragmentId: Int = R.layout.add_to_playlist_bottom_sheet
    private lateinit var binding: AddToPlaylistBottomSheetBinding
     val viewModel: AddToPlaylistViewModel by viewModels()
    private lateinit  var addToPlaylistAdapter: AddToPlaylistAdapter


    companion object {
        fun newInstance(
            song: SongModel
        ): AddToPlaylistBottomSheet {
            val fragment = AddToPlaylistBottomSheet(song)
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutFragmentId, container, false)
        addToPlaylistAdapter = AddToPlaylistAdapter(mutableListOf(
            PlaylistModel("demo" , mutableListOf())
        ), this)

        binding.apply {
            playlistNamesRv.adapter = addToPlaylistAdapter
            lifecycleOwner = viewLifecycleOwner
            playlistNamesRv.adapter = addToPlaylistAdapter
            playlistNamesRv.layoutManager = LinearLayoutManager(requireContext())
            playlistNamesRv.setHasFixedSize(true)
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.isHideable = false
        }

        val bottomSheet = view.parent as View
        bottomSheet.layoutParams.height = 1000



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllPlaylists()
            viewModel.playlists.collect {
                if (it is UiState.Success) {
                    val playlists = it.data
                    addToPlaylistAdapter.setData((playlists).sortedByDescending { it.playlistName })

                    Log.i("ahmed", "onViewCreated: " + playlists)
//                        binding.image.setImageResource(R.drawable.album_icon)
//                    Glide.with(this@ArtistsAndAlbumsAndPlaylistsSongsFragment)
//                        .load(Uri.parse(it.data.albumArt))
//                        .into(binding.image)
//                    binding.name.text = it.data.albumName
//                    "${it.data.albumSongs.size} songs".also { binding.songsCount.text = it }

                }
            }
        }
    }

    override fun onPlaylistClick(song: SongModel) {
        TODO("Not yet implemented")
    }
}