package com.example.musicplayer.ui.allSongsBottomSheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentAllSongsBottomSheetBinding
import com.example.musicplayer.ui.homeSongMoreButtonBottomSheet.HomeSongMoreButtonBottomSheet
import com.example.musicplayer.ui.musicBottomSheet.MusicBottomSheetFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.ui.songsFragment.OnSongsListener
import com.example.musicplayer.ui.songsFragment.SongViewModel
import com.example.musicplayer.ui.songsFragment.SongsAdapter
import com.example.musicplayer.utilities.PlayerEvents
import com.example.musicplayer.utilities.UiState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllSongsBottomSheet :
    BottomSheetDialogFragment(), OnSongsListener {


    private lateinit var songsAdapter: SongsAdapter
    private val layoutFragmentId: Int = R.layout.fragment_all_songs_bottom_sheet
    val musicViewModel: MusicPlayerViewModel by activityViewModels()
    private lateinit var binding: FragmentAllSongsBottomSheetBinding
    val viewModel: SongViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songsAdapter = SongsAdapter(mutableListOf(), this, requireContext())


        viewModel.fetchAllMusics()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioList.collect {
                if (it is UiState.Success && it.data.isNotEmpty()) {
                    songsAdapter.setData((it.data).sortedByDescending { it.songDateAdded })


                    binding.songsRv.apply {
                        adapter = songsAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                    }

                    "${it.data.size} Songs".also { binding.songsCount.text = it }
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? ViewGroup
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.peekHeight = 1200 // Set your desired height here in pixels
                it.layoutParams.height = 1200 // Set your desired height here in pixels
                it.layoutParams = it.layoutParams
            }
        }
        return dialog
    }

    override fun onSongClick(song: SongModel, position: Int) {
        musicViewModel.onPlayerEvents(
            PlayerEvents.GetThePositionOfSpecificSongInsideThePlaylist(
                song.songId
            )
        )
        val musicBottomSheetFragment = MusicBottomSheetFragment(song)
        fragmentManager?.let { musicBottomSheetFragment.show(it, musicBottomSheetFragment.tag) }
        songsAdapter.setPlayedSong(song)
    }


    override fun onMoreImageClick(song: SongModel) {
        val moreButtonBottomSheet = HomeSongMoreButtonBottomSheet.newInstance(song)
        fragmentManager?.let { moreButtonBottomSheet.show(it, moreButtonBottomSheet.tag) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, layoutFragmentId, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            return root
        }

    }

}