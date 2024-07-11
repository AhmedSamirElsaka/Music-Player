package com.example.musicplayer.utilities

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.R
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.ui.songsFragment.SongsAdapter
import com.google.android.material.imageview.ShapeableImageView


@BindingAdapter("app:setItemList")
fun <T> setItemList(view: RecyclerView, list: List<SongModel>?) {
    if (list != null) {
        (view.adapter as SongsAdapter).setData(list)
    } else {
        (view.adapter as SongsAdapter).setData(emptyList())
    }
}

@BindingAdapter("app:loadImageResource")
fun loadImage(imageView: ShapeableImageView, source: String?) {
    if (source != null) {
        Glide.with(imageView.context)
            .load(Uri.parse(source))
            .into(imageView)
//            Log.i("hello", "loadImage: $source")
    } else {
        imageView.setImageResource(R.drawable.storytel4997)
//            Log.i("hello", "loadImage: not")
    }
}

@BindingAdapter("app:showVisibleWhenLoading")
fun <T> showVisibleWhenLoading(view: View, state: UiState<T>?) {
    view.isVisible = state is UiState.Loading
}

@BindingAdapter("app:setImagePlayOrStop")
fun setImagePlayOrStop(imageView: ImageView, isPlay: Boolean) {
    if (isPlay) {
        imageView.setImageResource(R.drawable.pause_svgrepo_com)
    } else {
        imageView.setImageResource(R.drawable.play_svgrepo_com)
    }
}

@BindingAdapter("app:setMaxProgressForSeekBar")
fun setMaxProgressForSeekBar(seekBar: SeekBar, maxProgress: Long) {
    seekBar.max = maxProgress.toInt()
}

@BindingAdapter("app:setCurrentProgressForSeekBar")
fun setCurrentProgressForSeekBar(seekBar: SeekBar, progress: Long) {
    seekBar.progress = progress.toInt()
}

@BindingAdapter("app:setImageIsLoading")
fun setImageIsLoading(imageView: ImageView , isByffering :Boolean) {
    if (isByffering){
        imageView.setImageResource(R.drawable.loading)
    }
}

@BindingAdapter("app:setTimeMinutes")
fun setTimeMinutes(textView: TextView, progress : Long) {
    textView.text = convertLongDurationToTime(progress)
}
