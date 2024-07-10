package com.example.musicplayer.utilities

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.ui.songsFragment.SongsAdapter

@BindingAdapter("app:setItemList")
fun <T> setItemList(view: RecyclerView,  list :List<SongModel>?) {
    if (list !=null ) {
        (view.adapter as SongsAdapter).setData(list)
    }else{
        (view.adapter as SongsAdapter).setData( emptyList())
    }
}


@BindingAdapter("app:showVisibleWhenLoading")
fun <T> showVisibleWhenLoading(view: View, state: UiState<T>?) {
    view.isVisible = state is UiState.Loading
}

