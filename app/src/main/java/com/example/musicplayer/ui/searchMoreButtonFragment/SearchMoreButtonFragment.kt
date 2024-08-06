package com.example.musicplayer.ui.searchMoreButtonFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentSearchMoreButtonBinding
import com.example.musicplayer.ui.albumFragment.AlbumAdapter
import com.example.musicplayer.ui.albumFragment.OnAlbumListener
import com.example.musicplayer.ui.artistFragment.ArtistAdapter
import com.example.musicplayer.ui.artistFragment.OnArtistListener
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.homeSongMoreButtonBottomSheet.HomeSongMoreButtonBottomSheet
import com.example.musicplayer.ui.musicBottomSheet.MusicBottomSheetFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.ui.searchFragment.SearchFragmentDirections
import com.example.musicplayer.ui.songsFragment.OnSongsListener
import com.example.musicplayer.ui.songsFragment.SongsAdapter
import com.example.musicplayer.utilities.PlayerEvents


class SearchMoreButtonFragment : BaseFragment<FragmentSearchMoreButtonBinding>(), OnSongsListener,
    OnArtistListener, OnAlbumListener {
    override val layoutFragmentId: Int = R.layout.fragment_search_more_button
    override val viewModel: ViewModel
        get() = TODO("Not yet implemented")


    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var artistAdapter: ArtistAdapter
    private lateinit var albumAdapter: AlbumAdapter

    val args: SearchMoreButtonFragmentArgs by navArgs()
    private var albumList = arrayOf<AlbumModel>()
    private var artistList = arrayOf<ArtistModel>()
    private var songList = arrayOf<SongModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        songsAdapter = SongsAdapter(mutableListOf(), this, requireContext())
        artistAdapter = ArtistAdapter(mutableListOf(), this)
        albumAdapter = AlbumAdapter(mutableListOf(), this, requireContext())

        albumList = args.albumList
        artistList = args.artistList
        songList = args.songList


        if (albumList.isNotEmpty()) {
            albumAdapter.setData(albumList.toList())
            binding.Rv.apply {
                adapter = albumAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        } else if (artistList.isNotEmpty()) {
            artistAdapter.setData(artistList.toList())
            binding.Rv.apply {
                adapter = artistAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        } else if (songList.isNotEmpty()) {
            songsAdapter.setData(songList.toList())
            binding.Rv.apply {
                adapter = songsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    override fun onSongClick(song: SongModel, position: Int) {
        musicPlayerViewModel.onPlayerEvents(
            PlayerEvents.GetThePositionOfSpecificSongInsideThePlaylist(
                song.songId
            )
        )
        val musicBottomSheetFragment = MusicBottomSheetFragment(song)
        fragmentManager?.let { musicBottomSheetFragment.show(it, musicBottomSheetFragment.tag) }
    }

    override fun onMoreImageClick(song: SongModel) {
        val moreButtonBottomSheet = HomeSongMoreButtonBottomSheet.newInstance(song)
        fragmentManager?.let { moreButtonBottomSheet.show(it, moreButtonBottomSheet.tag) }
    }

    override fun onArtistClick(artistName: String) {
        val action = SearchMoreButtonFragmentDirections.actionSearchMoreButtonFragmentToArtistsAndAlbumsSongFragment(
            false,
            "",
            artistName,
        )
        findNavController().navigate(action)
    }

    override fun onAlbumClick(albumName: String) {
        val action = SearchMoreButtonFragmentDirections.actionSearchMoreButtonFragmentToArtistsAndAlbumsSongFragment(
            true,
            albumName,
            ""
        )
        findNavController().navigate(action)
    }

}