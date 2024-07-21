package com.example.musicplayer.ui.newPlaylistFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentNewPlaylistBinding
import com.example.musicplayer.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPlaylistFragment : BaseFragment<FragmentNewPlaylistBinding>() {
    override val layoutFragmentId: Int = R.layout.fragment_new_playlist
    override val viewModel: AddNewPlaylistViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}