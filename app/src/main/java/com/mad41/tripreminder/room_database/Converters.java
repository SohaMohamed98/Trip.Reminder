package com.mad41.tripreminder.room_database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public String fromArrayListToString(ArrayList<String> notes){
        return new Gson().toJson(notes);
    }
    @TypeConverter
    public ArrayList<String> fromStringToArrayList(String strNote){
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(strNote,listType);
    }
}
