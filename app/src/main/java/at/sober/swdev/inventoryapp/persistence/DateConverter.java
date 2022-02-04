package at.sober.swdev.inventoryapp.persistence;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateConverter {

    // LocalDateTime -> Timestamp
    @TypeConverter
    public static long dateToTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
    }

    // Timestamp -> LocalDateTime
    @TypeConverter
    public static LocalDateTime timestampToDatetime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
}
