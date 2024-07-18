package com.example.musicplayer.ui.sortSongsBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.R
import com.example.musicplayer.databinding.SortByBottomSheetBinding
import com.example.musicplayer.utilities.SortOption
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortSongsBottomSheet : BottomSheetDialogFragment() {
    private val layoutFragmentId: Int = R.layout.sort_by_bottom_sheet
    private lateinit var binding: SortByBottomSheetBinding


    private var sortOptionSelectedListener: OnSortOptionSelectedListener? = null

    companion object {
        var sortingOption: SortOption = SortOption.DATE_ADDED

        fun newInstance(
            listener: OnSortOptionSelectedListener,
        ): SortSongsBottomSheet {
            val fragment = SortSongsBottomSheet()
            fragment.sortOptionSelectedListener = listener
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (sortingOption) {
            SortOption.DATE_ADDED -> binding.buttonDateAdded.isChecked = true
            SortOption.SONG_NAME -> binding.buttonSongName.isChecked = true
            SortOption.ARTIST_NAME -> binding.buttonArtistName.isChecked = true
        }

        binding.sortingOptionsGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.button_song_name -> {
                    sortOptionSelectedListener?.onSortOptionSelected(SortOption.SONG_NAME)
                    dismiss()
                }

                R.id.button_artist_name -> {
                    sortOptionSelectedListener?.onSortOptionSelected(SortOption.ARTIST_NAME)
                    dismiss()
                }

                R.id.button_date_added -> {
                    sortOptionSelectedListener?.onSortOptionSelected(SortOption.DATE_ADDED)
                    dismiss()
                }
            }
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