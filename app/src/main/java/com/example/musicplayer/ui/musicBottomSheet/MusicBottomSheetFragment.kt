package com.example.musicplayer.ui.musicBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.example.musicplayer.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicBottomSheetFragment : BottomSheetDialogFragment() {

    private var songTitle: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =
            inflater.inflate(R.layout.played_song_bottom_sheet, container, false).also {
                songTitle = it.findViewById<TextView>(R.id.testTv)
            }


        // Set up your UI components and logic here
        // For example, you can set the song title and handle play/pause button click
        return view
    }

}