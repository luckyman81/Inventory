package at.sober.swdev.inventoryapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserWithDevicesDao {
    @Transaction
    @Query("SELECT * FROM users WHERE userId = :userId")
    public List<UserWithDevices> getUserWithDevices(long userId);

    @Transaction
    @Query("INSERT INTO DeviceUserCrossRef(deviceId, userId) VALUES (:deviceId, :userId)")
    void insertCrossRef(long deviceId, long userId);
}
