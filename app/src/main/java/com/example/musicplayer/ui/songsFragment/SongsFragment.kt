package com.example.musicplayer.ui.songsFragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentSongsBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.musicBottomSheet.MusicBottomSheetFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.utilities.LAST_PLAYED_SONG
import com.example.musicplayer.utilities.PlayerEvents
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SongsFragment : BaseFragment<FragmentSongsBinding>(), OnSongsListener {
    override val layoutFragmentId: Int = R.layout.fragment_songs
    override val viewModel: SongViewModel by viewModels()
    private lateinit var songsAdapter: SongsAdapter
    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()
    private var lastPlayedSongPosition = 0F

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songsAdapter = SongsAdapter(mutableListOf(), this)

        viewModel.fetchAllMusics()


        binding.songsRv.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioList.collect {
                if (it is UiState.Success && it.data.isNotEmpty()) {

                    songsAdapter.setData((it.data).sortedByDescending { it.songDateAdded })
                    musicPlayerViewModel.onPlayerEvents(PlayerEvents.AddPlaylist(it.data.sortedByDescending { it.songDateAdded }))

                    lastPlayedSongPosition = getFloatValue(0F)
                    musicPlayerViewModel.onPlayerEvents(
                        PlayerEvents.GoToSpecificItem(
                            lastPlayedSongPosition.toInt()
                        )
                    )
//                    musicPlayerViewModel.onPlayerEvents(PlayerEvents.PausePlay)
                }
            }
        }


    }

    override fun onSongClick(song: SongModel, position: Int) {
        musicPlayerViewModel.onPlayerEvents(PlayerEvents.GoToSpecificItem(position))
        val musicBottomSheetFragment = MusicBottomSheetFragment()
        fragmentManager?.let { musicBottomSheetFragment.show(it, musicBottomSheetFragment.tag) }
//        (parentFragment as HomeFragment).binding.currentPlayedSong = song
    }

    override fun onMoreImageClick() {
        TODO("Not yet implemented")
    }

    fun getFloatValue(defaultValue: Float): Float {
        return sharedPreferences.getFloat(LAST_PLAYED_SONG, defaultValue)
    }

}