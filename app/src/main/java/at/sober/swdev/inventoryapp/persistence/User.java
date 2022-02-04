package at.sober.swdev.inventoryapp.persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long userId;

    @NonNull
    public String name;

    @NonNull
    public String jobTitle;

    public User(@NonNull String name, @NonNull String jobTitle) {

        this.name = name;
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", name='" + name + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
