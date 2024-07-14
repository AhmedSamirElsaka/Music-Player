package com.example.musicplayer.ui.musicBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.musicplayer.R
import com.example.musicplayer.databinding.PlayedSongBottomSheetBinding
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.utilities.PlayerEvents
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MusicBottomSheetFragment : BottomSheetDialogFragment() {

    private val layoutFragmentId: Int = R.layout.played_song_bottom_sheet
    val viewModel: MusicPlayerViewModel by activityViewModels()
    private lateinit var binding: PlayedSongBottomSheetBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.isHideable = false
        }

        val bottomSheet = view.parent as View
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT


        binding.viewModel = viewModel

        binding.apply {
            hideBottomSheetBotton.setOnClickListener { dismiss() }
            playPauseImage.setOnClickListener { viewModel!!.onPlayerEvents(PlayerEvents.PausePlay) }
            nextSongImage.setOnClickListener { viewModel!!.onPlayerEvents(PlayerEvents.Next) }
            previousSongImage.setOnClickListener { viewModel!!.onPlayerEvents(PlayerEvents.Previous) }
            forward.setOnClickListener { viewModel!!.onPlayerEvents(PlayerEvents.SeekForward) }
            rewind.setOnClickListener { viewModel!!.onPlayerEvents(PlayerEvents.SeekBack) }
            musicProgressSeekbar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser)  viewModel!!.onPlayerEvents(PlayerEvents.MoveToSpecificPosition(progress.toLong()))
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // Do something when the user starts touching the SeekBar
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // Do something when the user stops touching the SeekBar
                }
            })
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
}