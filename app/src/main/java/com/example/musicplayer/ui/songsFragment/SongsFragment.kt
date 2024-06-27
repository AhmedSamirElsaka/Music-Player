package com.example.musicplayer.ui.songsFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentSongsBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.homeFragment.HomeViewModel
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SongsFragment : BaseFragment<FragmentSongsBinding>(), OnSongsListener {
    override val layoutFragmentId: Int = R.layout.fragment_songs
    override val viewModel: HomeViewModel by activityViewModels()
    private lateinit var songsAdapter: SongsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchAudioFiles()

//        var song1 = SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg")

        songsAdapter = SongsAdapter(
            mutableListOf(
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
//                SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg"),
            ), this
        )

        binding.songsRv.adapter = songsAdapter
        binding.songsRv.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioList.collect {
                if (it is UiState.Success) {
                    songsAdapter.setData((it.data).sortedByDescending { it.songDateAdded })
                    "${it.data.size} songs".also { binding.songsCountTv.text = it }
                }
            }
        }


    }

    override fun onSongClick(audio: SongModel) {
        TODO("Not yet implemented")
    }

    override fun onMoreImageClick() {
        TODO("Not yet implemented")
    }


}