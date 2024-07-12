package com.example.musicplayer.ui.artistFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ArtistFragment : BaseFragment<FragmentArtistBinding>(), OnArtistListener {
    override val layoutFragmentId: Int = R.layout.fragment_artist
    override val viewModel: ArtistViewModel by viewModels()
    lateinit var artistAdapter: ArtistAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadArtistsFiles()

//        var song1 = SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg")

        artistAdapter = ArtistAdapter(mutableListOf(), this)


        binding.artistsRv.apply {
            adapter = artistAdapter
            layoutManager = LinearLayoutManager(context)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioListGroupedByArtist.collect {
                if (it is UiState.Success) {
                    artistAdapter.setData((it.data.values.toList()).sortedBy { it.artistName })
                    "${it.data.size} songs".also { binding.artistsCountTv.text = it }
                }
            }
        }


    }

    override fun onArtistClick(artist: ArtistModel) {

    }
}