package com.apdallahy3.accenturetask.data.source.local;

import androidx.room.TypeConverter;

import java.util.Date;

public class TimestampConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

}
