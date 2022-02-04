package at.sober.swdev.inventoryapp.persistence;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class DeviceWithUsers {
    @Embedded
    public Device device;
    @Relation(
            parentColumn = "deviceId",
            entityColumn = "userId",
            associateBy = @Junction(DeviceUserCrossRef.class)
    )
    public List<User> users;
}
