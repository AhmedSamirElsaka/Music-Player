package com.example.musicplayer.ui.addToPlaylistBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.databinding.AddToPlaylistBottomSheetBinding
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddToPlaylistBottomSheet(private val playlists: List<PlaylistModel>) :
    BottomSheetDialogFragment() {
    private val layoutFragmentId: Int = R.layout.add_to_playlist_rv_item
    private lateinit var binding: AddToPlaylistBottomSheetBinding

    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()

    companion object {
        fun newInstance(
            playlists: List<PlaylistModel>
        ): AddToPlaylistBottomSheet {
            val fragment = AddToPlaylistBottomSheet(playlists)
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutFragmentId, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}