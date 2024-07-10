package com.example.musicplayer.ui.songsFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentSongsBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.utilities.PlayerEvents
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SongsFragment : BaseFragment<FragmentSongsBinding>(), OnSongsListener {
    override val layoutFragmentId: Int = R.layout.fragment_songs
    override val viewModel: SongViewModel by activityViewModels()
    private lateinit var songsAdapter: SongsAdapter
    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        var song1 = SongModel("rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg", "rdgdrfg")

        songsAdapter = SongsAdapter(mutableListOf(), this)

        viewModel.fetchAudioFiles()

        binding.songsRv.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

//        binding.songsRv.adapter = songsAdapter
//        binding.songsRv.layoutManager = LinearLayoutManager(context)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioList.collect {
                if (it is UiState.Success) {
                    songsAdapter.setData((it.data).sortedByDescending { it.songDateAdded })
//                    val bottomSheetFragment = MusicBottomSheetFragment()
//                    bottomSheetFragment.show(parentFragmentManager, "MusicBottomSheetFragment")
                    musicPlayerViewModel.onPlayerEvents(PlayerEvents.AddPlaylist(it.data.sortedByDescending { it.songDateAdded }))

                }
            }
        }


    }

    override fun onSongClick(audio: SongModel , position:Int) {
//        binding.bottomControlMusicPanel.root.visibility = View.VISIBLE
        musicPlayerViewModel.onPlayerEvents(PlayerEvents.GoToSpecificItem(position))
    }

    override fun onMoreImageClick() {
        TODO("Not yet implemented")
    }


}