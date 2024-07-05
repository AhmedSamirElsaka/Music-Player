package com.example.musicplayer.ui.albumFragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.data.model.AlbumModel
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.example.musicplayer.databinding.AlbumsRvItemBinding
import com.example.musicplayer.databinding.ArtistsRvItemBinding
import com.example.musicplayer.ui.base.BaseDiffUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlbumAdapter(
    private var list: List<AlbumModel>,
    private var listener: OnAlbumListener ,
    private var context:Context
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {



    class AlbumViewHolder(val binding: AlbumsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<AlbumModel>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(list, newList, ::areItemsSame, ::areContentSame))
        list = newList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding =
            AlbumsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = list[position]
        holder.binding.albumNameTv.text = item.albumName
        holder.binding.artistNameTv.text = item.albumCreator

        Log.i("hello",item.albumArt.toString() )
        if (item.albumArt != null) {
            Glide.with(context).load(item.albumArt).into(holder.binding.albumImage);
        }
//        holder.binding.artistNameTv.text = list[position].artistName
//        holder.binding.song = list[position]
////        Log.i("hello", "onBindViewHolder")
//        holder.binding.playedSongNameTv.text = list[position].songName
////        Log.i("hello",  list[position].songName)
//        holder.binding.nonPlayedSongNameTv.text = list[position].songName
//        holder.binding.listener = listener
    }

    override fun getItemCount(): Int = list.size
    private fun areItemsSame(oldItem: AlbumModel, newItem: AlbumModel) =
        oldItem.albumID == newItem.albumID

    private fun areContentSame(oldItem: AlbumModel, newItem: AlbumModel) =
        oldItem.albumName == newItem.albumName
}

interface OnAlbumListener {
    fun onAlbumClick(artist: SongModel)
}

