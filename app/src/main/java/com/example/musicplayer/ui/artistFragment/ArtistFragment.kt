package com.example.musicplayer.ui.artistFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentArtistBinding
import com.example.musicplayer.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArtistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ArtistFragment : BaseFragment<FragmentArtistBinding>(){
    override val layoutFragmentId: Int
        get() = TODO("Not yet implemented")
    override val viewModel: ViewModel
        get() = TODO("Not yet implemented")

}