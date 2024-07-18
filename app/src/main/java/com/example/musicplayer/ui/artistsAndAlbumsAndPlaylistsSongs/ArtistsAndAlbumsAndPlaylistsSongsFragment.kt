package com.example.musicplayer.ui.artistsAndAlbumsAndPlaylistsSongs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentArtistsAndAlbumsSongBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.musicBottomSheet.MusicBottomSheetFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.ui.songsFragment.OnSongsListener
import com.example.musicplayer.ui.songsFragment.SongsAdapter
import com.example.musicplayer.utilities.PlayerEvents
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ArtistsAndAlbumsAndPlaylistsSongsFragment :
    BaseFragment<FragmentArtistsAndAlbumsSongBinding>(),
    OnSongsListener {
    override val layoutFragmentId: Int = R.layout.fragment_artists_and_albums_song
    override val viewModel: ArtistsAndAlbumsSongsViewModel by viewModels()
    private lateinit var songsAdapter: SongsAdapter
    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()
    val args: ArtistsAndAlbumsAndPlaylistsSongsFragmentArgs by navArgs()
    private var albumId = ""
    private var artistId = ""
    private var playlistName = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumId = args.albumId
        artistId = args.artistId
        playlistName = args.playlistName

        binding.isFromAlbumsFragment = args.isFromAlbumsOrPlaylistFragment

        if (!albumId.equals("")) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getSpecificSongsByAlbumId(albumId)
                viewModel.albumAudioList.collect {
                    if (it is UiState.Success) {
                        val songs = it.data.albumSongs
                        songsAdapter.setData((songs).sortedByDescending { it.songDateAdded })
                        binding.image.setImageResource(R.drawable.album)
                        binding.name.text = it.data.albumName
                        "${it.data.albumSongs.size} songs".also { binding.songsCount.text = it }

                    }
                }
            }
        } else if (!artistId.equals("")) {
            viewModel.getSpecificSongsByArtistId(artistId)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.artistAudioList.collect {
                    if (it is UiState.Success) {
                        val songs = it.data.artistSongs
                        songsAdapter.setData((songs).sortedByDescending { it.songDateAdded })
                    }
                }
            }
        } else {
            Log.i("hello", "onViewCreated: beign")
            Log.i("hello", "onViewCreated: " + playlistName)

            viewModel.getSpecificPlaylistSongsByName(playlistName)
            viewLifecycleOwner.lifecycleScope.launch {
                Log.i("hello", "onViewCreated: error")
                viewModel.playlistsList.collect {

                    if (it is UiState.Success) {
                        Log.i("hello", "onViewCreated: success")
                        val songs = it.data.playlistSongs
                        songsAdapter.setData((songs).sortedByDescending { it.songDateAdded })
                        binding.image.setImageResource(R.drawable.playlist)
                        binding.name.text = playlistName
                        "${it.data.playlistSongs.size} songs".also { binding.songsCount.text = it }
                    }
                }
            }
        }
        songsAdapter = SongsAdapter(mutableListOf(), this)



        binding.songsRv.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

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

    override fun onMoreImageClick() {
    }
}