package com.example.musicplayer.ui.scanLocalMusicsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentScanLocalMusicBinding
import com.example.musicplayer.ui.artistFragment.ArtistAdapter
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScanLocalMusicFragment :BaseFragment<FragmentScanLocalMusicBinding>() {
    override val layoutFragmentId: Int = R.layout.fragment_scan_local_music
    override val viewModel: ScanLocalMusicViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.refreshSongsAndCaching()


        binding.scanAgainButton.setOnClickListener {
            viewModel.refreshSongsAndCaching()
            binding.loadingProgressBar.visibility = View.VISIBLE
            binding.finishLoadingGroup.visibility = View.GONE
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioList.collect {
                if (it is UiState.Success) {
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.finishLoadingGroup.visibility = View.VISIBLE
                    "${it.data.size} Songs".also { binding.scanningState2Tv.text = it }
                }
            }
        }
    }
}