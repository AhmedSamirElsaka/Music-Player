package com.example.musicplayer.ui.songInfoFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.databinding.FragmentSongInfoBinding
import com.example.musicplayer.ui.artistFragment.ArtistAdapter
import com.example.musicplayer.ui.artistsAndAlbumsAndPlaylistsSongs.ArtistsAndAlbumsAndPlaylistsSongsFragmentArgs
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.utilities.UiState
import kotlinx.coroutines.launch


class SongInfoFragment : BaseFragment<FragmentSongInfoBinding>() {
    override val layoutFragmentId: Int = R.layout.fragment_song_info
    override val viewModel: ViewModel
        get() = TODO("Not yet implemented")

    val args: SongInfoFragmentArgs by navArgs()
    private lateinit var song:SongModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        song = args.song

        binding.song = song
    }

}