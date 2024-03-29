package at.sober.swdev.inventoryapp.persistence;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "users")
public class User implements Serializable {

    @JsonProperty("userId")
    @PrimaryKey(autoGenerate = true)
    public long userId;

    @JsonProperty("name")
    @NonNull
    public String name;

    @JsonProperty("jobTitle")
    @NonNull
    public String jobTitle;

    @JsonCreator
    public User(@JsonProperty("userId") @NonNull long userId,
                @JsonProperty("name") @NonNull String name,
                @JsonProperty("jobTitle") @NonNull String jobTitle) {
        this.userId = userId;
        this.name = name;
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return name + "," + jobTitle;
    }

    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {


            return "{" +
                    "'id':'" + userId + '\'' +
                    ", 'name':'" + name + '\'' +
                    ", 'jobTitle':'" + jobTitle + '\'' +
                    "}";
        }
    }

    public User fromJsonString(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, User.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && jobTitle.equals(user.jobTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, jobTitle);
    }
}
