package com.example.musicplayer.ui.musicBottomSheet

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.musicplayer.R
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.data.source.PlaylistRepository
import com.example.musicplayer.databinding.PlayedSongBottomSheetBinding
import com.example.musicplayer.ui.allSongsBottomSheet.AllSongsBottomSheet
import com.example.musicplayer.ui.homeSongMoreButtonBottomSheet.HomeSongMoreButtonBottomSheet
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.utilities.PlayerEvents
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MusicBottomSheetFragment(var song: SongModel) : BottomSheetDialogFragment() {

    private val layoutFragmentId: Int = R.layout.played_song_bottom_sheet
    val viewModel: MusicPlayerViewModel by activityViewModels()
    private lateinit var binding: PlayedSongBottomSheetBinding

    @Inject
    lateinit var playlistRepository: PlaylistRepository

    private var isSongLiked: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior

        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
            behavior.isHideable = false
        }


        var likedPlaylist: PlaylistModel? = null
        viewLifecycleOwner.lifecycleScope.launch {
            playlistRepository.getLikedPlaylist().collect {
                likedPlaylist = it.toData()
            }
        }.invokeOnCompletion {
            if (likedPlaylist != null && likedPlaylist!!.playlistSongs.contains(song)) {
                binding.loveSongImage.setImageResource(R.drawable.yellow_heart_icon)
                val color = ContextCompat.getColor(requireContext(), R.color.main_color)
                val colorStateList = ColorStateList.valueOf(color)
                binding.loveSongImage.imageTintList = colorStateList
                isSongLiked = true
            }
        }

        val bottomSheet = view.parent as View
        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT


        binding.viewModel = viewModel

        if (viewModel!!.isShuffleClicked.value) {
            binding.playeModeImage.setImageResource(R.drawable.shuffle)
        } else if (viewModel!!.isRepeatClick.value) {
            binding.playeModeImage.setImageResource(R.drawable.loop_1)
        } else {
            binding.playeModeImage.setImageResource(R.drawable.loop_list)
        }

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
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    if (fromUser) viewModel!!.onPlayerEvents(
                        PlayerEvents.MoveToSpecificPosition(
                            progress.toLong()
                        )
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // Do something when the user starts touching the SeekBar
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // Do something when the user stops touching the SeekBar
                }
            })

            binding.moreButton.setOnClickListener {
                val moreButtonBottomSheet =
                    HomeSongMoreButtonBottomSheet.newInstance(viewModel!!.currentSongFlow.value)
                fragmentManager?.let { moreButtonBottomSheet.show(it, moreButtonBottomSheet.tag) }
            }

            binding.playeModeImage.setOnClickListener {
                if (viewModel!!.isShuffleClicked.value) {
                    viewModel!!.onPlayerEvents(PlayerEvents.Shuffle)
                    viewModel!!.onPlayerEvents(PlayerEvents.Repeat)
                    binding.playeModeImage.setImageResource(R.drawable.loop_1)
                } else if (viewModel!!.isRepeatClick.value) {
                    viewModel!!.onPlayerEvents(PlayerEvents.Repeat)
                    binding.playeModeImage.setImageResource(R.drawable.loop_list)
                } else {
                    viewModel!!.onPlayerEvents(PlayerEvents.Shuffle)
                    binding.playeModeImage.setImageResource(R.drawable.shuffle)
                }
            }

            binding.loveSongImage.setOnClickListener {
                if (likedPlaylist != null && !isSongLiked) {
                    playlistRepository.addSongToPlaylist(song, likedPlaylist!!)
                    binding.loveSongImage.setImageResource(R.drawable.yellow_heart_icon)
                    val color = ContextCompat.getColor(requireContext(), R.color.main_color)
                    val colorStateList = ColorStateList.valueOf(color)
                    binding.loveSongImage.imageTintList = colorStateList
                }
            }

            binding.listImage.setOnClickListener {
                val allSongsBottomSheet = AllSongsBottomSheet()
                fragmentManager?.let { allSongsBottomSheet.show(it, allSongsBottomSheet.tag) }
            }
        }
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