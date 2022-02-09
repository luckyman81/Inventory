package at.sober.swdev.inventoryapp.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "devices")
public class Device implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long deviceId;

    @NonNull
    public String name;

    @NonNull
    public String category;

    @NonNull
    public String serial;

    @Ignore
    public User owner;

    @ColumnInfo(name = "created_at")
    public LocalDateTime createdAt;

    public Device(@NonNull String name, @NonNull String category, @NonNull String serial) {
        this.name = name;
        this.category = category;
        this.serial = serial;

        createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId=" + deviceId +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", serial='" + serial + '\'' +
                ", owner='" + owner + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
