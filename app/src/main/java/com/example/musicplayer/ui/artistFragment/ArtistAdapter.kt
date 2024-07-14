package com.example.musicplayer.ui.artistFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.ArtistsRvItemBinding
import com.example.musicplayer.ui.base.BaseDiffUtil

class ArtistAdapter(
    private var list: List<ArtistModel>,
    private var listener: OnArtistListener
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(val binding: ArtistsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<ArtistModel>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(list, newList, ::areItemsSame, ::areContentSame))
        list = newList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding =
            ArtistsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.binding.artistName =  list[position].artistName
        holder.binding.listener = listener
    }

    override fun getItemCount(): Int = list.size
    private fun areItemsSame(oldItem: ArtistModel, newItem: ArtistModel) =
        oldItem.artistName == newItem.artistName

    private fun areContentSame(oldItem: ArtistModel, newItem: ArtistModel) =
        oldItem.artistName == newItem.artistName
}

interface OnArtistListener {
    fun onArtistClick(artistName: String)
}

