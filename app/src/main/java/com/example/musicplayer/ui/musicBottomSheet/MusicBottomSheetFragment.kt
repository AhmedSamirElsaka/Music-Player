package com.example.musicplayer.ui.musicBottomSheet

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.example.musicplayer.R
import com.example.musicplayer.databinding.PlayedSongBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MusicBottomSheetFragment : BottomSheetDialogFragment() {

    val layoutFragmentId: Int = R.layout.played_song_bottom_sheet
    val viewModel: ViewModel? = null
    private lateinit var binding: PlayedSongBottomSheetBinding

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener { dialogInterface ->
//            val bottomSheetDialog = dialogInterface as BottomSheetDialog
//            setupFullHeight(bottomSheetDialog)
//        }
//        return dialog
//    }

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
    }
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, layoutFragmentId, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
//            setVariable(BR.viewModel, viewModel)
            return root
        }

//        val view: View =
//            inflater.inflate(com.example.musicplayer.R.layout.played_song_bottom_sheet, container, false).also {
//            }
//
//        return view
    }

//    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
////        val bottomSheet =
////            bottomSheetDialog.findViewById<View>(com.example.musicplayer.R.id.bottom_sheet) as FrameLayout?
//        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(binding.bottomSheet)
//        val layoutParams = binding.bottomSheet.layoutParams
//        val windowHeight = getWindowHeight()
//        if (layoutParams != null) {
//            layoutParams.height = windowHeight
//        }
//        binding.bottomSheet.layoutParams = layoutParams
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//    }
//
//    private fun getWindowHeight(): Int {
//        // Calculate window height for fullscreen use
//        val displayMetrics = DisplayMetrics()
//        (requireContext() as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        return displayMetrics.heightPixels
//    }
}