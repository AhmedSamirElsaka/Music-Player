package com.example.musicplayer.ui.playlistFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.musicplayer.R
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.databinding.FragmentPlaylistBinding
import com.example.musicplayer.databinding.FragmentSongsBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.songsFragment.OnSongsListener
import com.example.musicplayer.ui.songsFragment.SongViewModel
import com.example.musicplayer.ui.songsFragment.SongsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>(), OnPlaylistsListener {
    override val layoutFragmentId: Int = R.layout.fragment_playlist
    override val viewModel: PlaylistViewModel by viewModels()
    private lateinit var playListAdapter: PlaylistAdapter


    override fun onPlayListClick(playlist: PlaylistModel) {
        TODO("Not yet implemented")
    }


}