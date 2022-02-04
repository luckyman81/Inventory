package at.sober.swdev.inventoryapp.persistence;

import androidx.room.Entity;

@Entity(primaryKeys = {"deviceId", "userId"})
public class DeviceUserCrossRef {

    public long deviceId;
    public long userId;
}
