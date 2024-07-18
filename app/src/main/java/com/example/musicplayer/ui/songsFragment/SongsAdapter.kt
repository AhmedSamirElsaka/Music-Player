package com.example.musicplayer.ui.songsFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.SongsRvItemBinding
import com.example.musicplayer.ui.base.BaseDiffUtil

class SongsAdapter(
    private var list: List<SongModel>,
    private var listener: OnSongsListener
) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    class SongViewHolder(val binding: SongsRvItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<SongModel>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(list, newList, ::areItemsSame, ::areContentSame))
        list = newList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.binding.song = list[position]
        holder.binding.playedSongNameTv.text = list[position].songName
        holder.binding.nonPlayedSongNameTv.text = list[position].songName
        holder.binding.listener = listener
        holder.binding.position = position

    }

    override fun getItemCount(): Int = list.size
    private fun areItemsSame(oldItem: SongModel, newItem: SongModel) =
        oldItem.songId == newItem.songId

    private fun areContentSame(oldItem: SongModel, newItem: SongModel) =
        oldItem.songName == newItem.songName


    fun getData(): List<SongModel> {
        return list
    }
}

interface OnSongsListener {
    fun onSongClick(song: SongModel , position: Int)
    fun onMoreImageClick()
}

