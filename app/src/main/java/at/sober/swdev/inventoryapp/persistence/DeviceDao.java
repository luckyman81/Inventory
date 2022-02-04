package at.sober.swdev.inventoryapp.persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO (Data Access Object)
 * Hier werden unsere SQL-Abfrage gecodet bzw. generiert
 */
@Dao
public interface DeviceDao {

    // Wir brauchen CRUD (Create, Retrieve/Read, Update, Delete)

    @Insert
    long insert(Device device);

    // Insert mit mehreren Notes
    @Insert
    void insert(Device... devices);



    @Update
    void update(Device device);

    @Delete
    void delete(Device device);

    @Query("DELETE FROM devices")
    void deleteAllDevices();

    @Query("SELECT * FROM devices ORDER BY created_at ASC")
    LiveData<List<Device>> getAllDevices();

    @Query("SELECT * FROM devices WHERE created_at >= :dateTime")
    LiveData<List<Device>> getDevicesSinceDate(LocalDateTime dateTime);

    @Query("SELECT * FROM devices WHERE deviceId = :deviceId")
    LiveData<List<Device>> getDeviceById(long deviceId);
}
