package at.sober.swdev.inventoryapp.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    @Nullable
    public String description;

    @NonNull
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    public transient Bitmap image;

    @ColumnInfo(name = "created_at")
    public LocalDateTime createdAt;

    public Device(@NonNull String name, @NonNull String category, @NonNull String serial, @NonNull Bitmap image,@Nullable String description) {
        this.name = name;
        this.category = category;
        this.serial = serial;
        this.image = image;
        this.description = description;

        createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId=" + deviceId +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", serial='" + serial + '\'' +
                ", owner=" + owner +
                ", description='" + description + '\'' +
                ", image=" + image +
                ", createdAt=" + createdAt +
                '}';
    }

    // cf. https://stackoverflow.com/questions/8955034/how-to-fix-a-java-io-notserializableexception-android-graphics-bitmap
    private void writeObject(ObjectOutputStream oos) throws IOException {
        // This will serialize all fields that you did not mark with 'transient'
        // (Java's default behaviour)
        oos.defaultWriteObject();
        // Now, manually serialize all transient fields that you want to be serialized
        if(image!=null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = image.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if(success){
                oos.writeObject(byteStream.toByteArray());
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        // Now, all again, deserializing - in the SAME ORDER!
        // All non-transient fields
        ois.defaultReadObject();
        // All other fields that you serialized
        byte[] array = (byte[]) ois.readObject();
        if(array != null && array.length > 0){
            image = BitmapFactory.decodeByteArray(array, 0, array.length);
        }
    }
}
