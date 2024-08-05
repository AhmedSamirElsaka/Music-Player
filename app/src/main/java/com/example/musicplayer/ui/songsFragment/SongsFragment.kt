package com.example.musicplayer.ui.songsFragment

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.FragmentSongsBinding
import com.example.musicplayer.ui.base.BaseFragment
import com.example.musicplayer.ui.homeSongMoreButtonBottomSheet.HomeSongMoreButtonBottomSheet
import com.example.musicplayer.ui.musicBottomSheet.MusicBottomSheetFragment
import com.example.musicplayer.ui.musicPlayer.MusicPlayerViewModel
import com.example.musicplayer.ui.sortSongsBottomSheet.OnSortOptionSelectedListener
import com.example.musicplayer.ui.sortSongsBottomSheet.SortSongsBottomSheet
import com.example.musicplayer.ui.sortSongsBottomSheet.SortSongsBottomSheet.Companion.sortingOption
import com.example.musicplayer.utilities.LAST_PLAYED_SONG
import com.example.musicplayer.utilities.PlayerEvents
import com.example.musicplayer.utilities.SortOption
import com.example.musicplayer.utilities.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


@AndroidEntryPoint
class SongsFragment : BaseFragment<FragmentSongsBinding>(), OnSongsListener,
    OnSortOptionSelectedListener {
    override val layoutFragmentId: Int = R.layout.fragment_songs
    override val viewModel: SongViewModel by viewModels()
    private lateinit var songsAdapter: SongsAdapter
    private val musicPlayerViewModel: MusicPlayerViewModel by activityViewModels()
//    private var lastPlayedSongPosition = 0F

    @Inject
    lateinit var sharedPreferences: SharedPreferences



    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionContinuation?.resume(true)
        } else {
            permissionContinuation?.resume(false)
        }
    }

    private var permissionContinuation: Continuation<Boolean>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songsAdapter = SongsAdapter(mutableListOf(), this , requireContext())

        lifecycleScope.launch {
            if (requestReadExternalStoragePermission()) {
                // Permission granted, start your actions
                viewModel.fetchAllMusics()
            } else {
                Log.i("hello", "onViewCreated: not granted permission")
            }
        }

//        viewModel.fetchAllMusics()


        binding.songsRv.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        binding.apply {
            songsRv.adapter = songsAdapter
            songsRv.layoutManager = LinearLayoutManager(requireContext())
            songsRv.setHasFixedSize(true)
            viewModel = this@SongsFragment.viewModel
            playAllTv.setOnClickListener {
                musicPlayerViewModel.onPlayerEvents(
                    PlayerEvents.GoToSpecificItem(
                        0
                    )
                )
            }
            playAllImg.setOnClickListener {
                musicPlayerViewModel.onPlayerEvents(
                    PlayerEvents.GoToSpecificItem(
                        0
                    )
                )
            }

            sort.setOnClickListener {
                val sortBottomSheet = SortSongsBottomSheet.newInstance(this@SongsFragment)
                fragmentManager?.let { sortBottomSheet.show(it, sortBottomSheet.tag) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.audioList.collect {
                if (it is UiState.Success && it.data.isNotEmpty()) {
                    songsAdapter.setData((it.data).sortedByDescending { it.songDateAdded })
//                    musicPlayerViewModel.onPlayerEvents(PlayerEvents.ClearMediaItems)
                    musicPlayerViewModel.onPlayerEvents(
                        PlayerEvents.AddPlaylist(
                            it.data.sortedByDescending { it.songDateAdded },
                            false
                        )
                    )
//                    lastPlayedSongPosition = getFloatValue()
//                    Log.i("hello", "onViewCreated: $lastPlayedSongPosition")
//                    musicPlayerViewModel.onPlayerEvents(
//                        PlayerEvents.GoToSpecificItem(
//                            lastPlayedSongPosition.toInt()
//                        )
//                    )
//                    musicPlayerViewModel.onPlayerEvents(PlayerEvents.PausePlay)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            musicPlayerViewModel.currentMediaPositionInList.collect{
                if(songsAdapter.getData().isNotEmpty()){
                    songsAdapter.setPlayedSong(songsAdapter.getData()[it.toInt()])
                }
            }
        }


    }

    override fun onSongClick(song: SongModel, position: Int) {
        musicPlayerViewModel.onPlayerEvents(
            PlayerEvents.GetThePositionOfSpecificSongInsideThePlaylist(
                song.songId
            )
        )
        val musicBottomSheetFragment = MusicBottomSheetFragment()
        fragmentManager?.let { musicBottomSheetFragment.show(it, musicBottomSheetFragment.tag) }

        songsAdapter.setPlayedSong(song)
    }

    override fun onMoreImageClick(song: SongModel ) {
        val moreButtonBottomSheet = HomeSongMoreButtonBottomSheet.newInstance(song)
        fragmentManager?.let { moreButtonBottomSheet.show(it, moreButtonBottomSheet.tag) }
    }

    private fun getFloatValue(defaultValue: Float = 0F): Float {
        return sharedPreferences.getFloat(LAST_PLAYED_SONG, defaultValue)
    }

    override fun onSortOptionSelected(sortOption: SortOption) {
        when (sortOption) {
            SortOption.DATE_ADDED -> {
                songsAdapter.setData(songsAdapter.getData().sortedByDescending { it.songDateAdded })
                sortingOption = SortOption.DATE_ADDED
            }

            SortOption.SONG_NAME -> {
                songsAdapter.setData(songsAdapter.getData().sortedByDescending { it.songName })
                sortingOption = SortOption.SONG_NAME
            }

            SortOption.ARTIST_NAME -> {
                songsAdapter.setData(songsAdapter.getData().sortedByDescending { it.songArtist })
                sortingOption = SortOption.ARTIST_NAME
            }
        }
    }

    private suspend fun requestReadExternalStoragePermission(): Boolean =
        suspendCancellableCoroutine { continuation ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                continuation.resume(true)
            } else {
                permissionContinuation = continuation
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }


}