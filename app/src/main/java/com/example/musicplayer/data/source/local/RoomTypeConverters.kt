package com.example.musicplayer.data.source.local

import androidx.room.TypeConverter
import com.example.musicplayer.data.model.SongModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomTypeConverters {
    @TypeConverter
    fun convertInvoiceListToJSONString(songList: List<SongModel>): String = Gson().toJson(songList)

    @TypeConverter
    fun convertJSONStringToInvoiceList(jsonString: String): List<SongModel> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

}