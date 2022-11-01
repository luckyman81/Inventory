package at.sober.swdev.inventoryapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Insert
    void insert(User... users);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAllUsers();

    @Query("DELETE FROM deviceusercrossref")
    void deleteAllDeviceUserCrossRefs();

    @Query("DELETE FROM devices")
    void deleteAllDevices();

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users")
    List<User> getAllUsersForSpinner();
}
