package com.example.musicplayer.ui.songsFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.SongsRvItemBinding
import com.example.musicplayer.ui.base.BaseDiffUtil

class SongsAdapter(
    private var list: List<SongModel>,
    private var listener: OnSongsListener ,
    private var context: Context
) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    private var isPlayedSongShowed :Boolean = false
    private var playedSong:SongModel? = null
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
        if(playedSong != null && list[position] == playedSong ){
            holder.binding.PlayedGroup.visibility = View.VISIBLE
            holder.binding.nonPlayedGroup.visibility = View.GONE
            isPlayedSongShowed = true
        }else {
            holder.binding.nonPlayedGroup.visibility = View.VISIBLE
            holder.binding.PlayedGroup.visibility = View.GONE
        }
        holder.binding.song = list[position]
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

    fun setPlayedSong(song: SongModel){
        playedSong = song
        notifyDataSetChanged()
    }
}

interface OnSongsListener {
    fun onSongClick(song: SongModel , position: Int)
    fun onMoreImageClick(song: SongModel )
}

