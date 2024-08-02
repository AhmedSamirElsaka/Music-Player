package com.example.musicplayer.ui.homeSongMoreButtonBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.SongMoreButtonBottomSheetBinding
import com.example.musicplayer.ui.addToPlaylistBottomSheet.AddToPlaylistBottomSheet
import com.example.musicplayer.ui.homeFragment.HomeFragmentDirections
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.utilities.PlayerEvents
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeSongMoreButtonBottomSheet
    (private val song: SongModel) : BottomSheetDialogFragment() {
    private val layoutFragmentId: Int = R.layout.song_more_button_bottom_sheet
    private lateinit var binding: SongMoreButtonBottomSheetBinding
    private lateinit var songDetails: SongModel

    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()

    companion object {
        fun newInstance(
            song: SongModel
        ): HomeSongMoreButtonBottomSheet {
            val fragment = HomeSongMoreButtonBottomSheet(song)
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutFragmentId, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.artistName = song.songArtist
        binding.albumName = song.songAlbum


        binding.apply {
            artistSongMoreButton.setOnClickListener{
                val action = HomeFragmentDirections.actionHomeFragmentToArtistsAndAlbumsSongFragment(
                    false,
                    "",
                    song.songArtist
                )
                findNavController().navigateUp()
                findNavController().navigate(action)
                dismiss()
            }
            albumSongMoreButton.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToArtistsAndAlbumsSongFragment(
                    true,
                    song.songAlbum,
                    ""
                )
                findNavController().navigateUp()
                findNavController().navigate(action)

                dismiss()
            }
            playNextSongMoreButton.setOnClickListener {
                musicPlayerViewModel.onPlayerEvents(PlayerEvents.AddSongToPlayNext(song.songId))
//                musicPlayerViewModel.onPlayerEvents(PlayerEvents.Next)
                Toast.makeText(context, "This Song will be after the current song", Toast.LENGTH_SHORT).show();
                dismiss()
            }
            addToPlaylistSongMoreButton.setOnClickListener{
                val moreButtonBottomSheet = AddToPlaylistBottomSheet.newInstance(song)
                fragmentManager?.let { moreButtonBottomSheet.show(it, moreButtonBottomSheet.tag) }
                dismiss()
            }

            songInfoSongMoreButton.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToSongInfoFragment(
                    song
                )
                findNavController().navigate(action)
                dismiss()
            }
        }
    }
}