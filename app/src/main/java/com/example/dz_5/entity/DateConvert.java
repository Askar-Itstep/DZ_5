package com.example.dz_5.entity;

import androidx.room.TypeConverter;

import java.sql.Date;

public class DateConvert {

    private static final String TAG = "===DateConvert===";
    @TypeConverter
    public static Long fromDate(Date date){
        return date == null? 0: date.getTime();
    }

    @TypeConverter
    public static Date toDate(Long dateLong){
//        Log.e(TAG, "datelong: "+dateLong);
        return dateLong == 0 ? new Date(0): new Date(dateLong);
    }
}
