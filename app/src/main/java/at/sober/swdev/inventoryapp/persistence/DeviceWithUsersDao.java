package at.sober.swdev.inventoryapp.persistence;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface DeviceWithUsersDao {
    @Transaction
    @Query("SELECT * FROM devices WHERE deviceId = :deviceId")
    public List<DeviceWithUsers> getDeviceWithUsers(long deviceId);
}
