package com.example.musicplayer.ui.addToPlaylistBottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.model.PlaylistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.AddToPlaylistRvItemBinding
import com.example.musicplayer.ui.base.BaseDiffUtil

class AddToPlaylistAdapter(
    private var list: List<PlaylistModel>,
    private var listener: OnAddToPlaylistListener
) : RecyclerView.Adapter<AddToPlaylistAdapter.AddToPlaylistViewHolder>() {

    class AddToPlaylistViewHolder(val binding: AddToPlaylistRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<PlaylistModel>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(list, newList, ::areItemsSame, ::areContentSame))
        list = newList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddToPlaylistViewHolder {
        val binding =
            AddToPlaylistRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddToPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddToPlaylistViewHolder, position: Int) {


    }

    override fun getItemCount(): Int = list.size
    private fun areItemsSame(oldItem: PlaylistModel, newItem: PlaylistModel) =
        oldItem.playlistName == newItem.playlistName

    private fun areContentSame(oldItem: PlaylistModel, newItem: PlaylistModel) =
        oldItem.playlistName == newItem.playlistName


}

interface OnAddToPlaylistListener {
    fun onPlaylistClick(song: SongModel)
}

