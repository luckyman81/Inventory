package at.sober.swdev.inventoryapp.persistence;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithDevices {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "deviceId",
            associateBy = @Junction(DeviceUserCrossRef.class)
    )
    public List<Device> devices;
}
