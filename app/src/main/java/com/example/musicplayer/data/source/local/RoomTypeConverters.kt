package com.example.musicplayer.data.source.local

import androidx.room.TypeConverter
import com.example.musicplayer.data.model.ArtistModel
import com.example.musicplayer.data.model.SongModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomTypeConverters {
    @TypeConverter
    fun convertSongListToJSONString(songList: List<SongModel>): String = Gson().toJson(songList)

    @TypeConverter
    fun convertJSONStringToSongList(jsonString: String): List<SongModel> {
        val listType = object : TypeToken<List<SongModel>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    @TypeConverter
    fun convertArtistListToJSONString(songList: List<ArtistModel>): String = Gson().toJson(songList)

    @TypeConverter
    fun convertJSONStringToArtistList(jsonString: String): List<ArtistModel> {
        val listType = object : TypeToken<List<ArtistModel>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
//    @TypeConverter
//    fun convertArtistHashMapToJSONString(songList: HashMap<String, ArtistModel>): String = Gson().toJson(songList)
//
//    @TypeConverter
//    fun convertJSONStringToArtistHashMap(jsonString: String): HashMap<String, ArtistModel> {
//        val listType = object : TypeToken<HashMap<String, ArtistModel>>() {}.type
//        return Gson().fromJson(jsonString, listType)
//    }

}