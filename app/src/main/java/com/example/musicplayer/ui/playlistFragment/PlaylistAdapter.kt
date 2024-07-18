package com.example.musicplayer.ui.playlistFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.BR
import com.example.musicplayer.R
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.ui.base.BaseDiffUtil

class PlaylistAdapter(
    private var list: List<PlaylistModel>,
    private var listener: OnPlaylistsListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<PlaylistModel>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(list, newList, ::areItemsSame, ::areContentSame))
        list = newList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun getItemViewType(position: Int): Int {
        return if(position == (list.size - 1)) {
            R.layout.add_playlist_rv_item
        } else {
            R.layout.playlist_rv_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), viewType, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        if (position != (list.size - 1)) {
            holder.binding.run {
                setVariable(
                    BR.playlistModel, list[position]
                )
                setVariable(
                    BR.listener, listener
                )
            }
        }
    }

    override fun getItemCount(): Int = list.size
    private fun areItemsSame(oldItem: PlaylistModel, newItem: PlaylistModel) =
        oldItem.playlistName == newItem.playlistName

    private fun areContentSame(oldItem: PlaylistModel, newItem: PlaylistModel) =
        oldItem.playlistName == newItem.playlistName
}

interface OnPlaylistsListener {
    fun onPlayListClick(playlist: PlaylistModel)
}

