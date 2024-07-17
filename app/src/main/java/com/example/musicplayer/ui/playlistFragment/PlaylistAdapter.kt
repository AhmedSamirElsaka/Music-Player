package com.example.musicplayer.ui.playlistFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.PlaylistRvItemBinding
import com.example.musicplayer.databinding.SongsRvItemBinding
import com.example.musicplayer.ui.base.BaseDiffUtil

class PlaylistAdapter(
    private var list: List<PlaylistModel>,
    private var listener: OnPlaylistsListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(val binding: PlaylistRvItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<PlaylistModel>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(list, newList, ::areItemsSame, ::areContentSame))
        list = newList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
//        holder.binding.song = list[position]
//        holder.binding.playedSongNameTv.text = list[position].songName
//        holder.binding.nonPlayedSongNameTv.text = list[position].songName
//        holder.binding.listener = listener
//        holder.binding.position = position

    }

    override fun getItemCount(): Int = list.size
    private fun areItemsSame(oldItem: PlaylistModel, newItem: PlaylistModel) =
        oldItem.playlistName == newItem.playlistName

    private fun areContentSame(oldItem: PlaylistModel, newItem: PlaylistModel) =
        oldItem.playlistName == newItem.playlistName
}

interface OnPlaylistsListener {
    fun onPlayListClick(playlist: PlaylistModel )
}

