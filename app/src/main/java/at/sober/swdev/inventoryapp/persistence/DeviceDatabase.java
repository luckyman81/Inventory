package at.sober.swdev.inventoryapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(version = 1, entities = {Device.class, User.class, DeviceUserCrossRef.class}, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class DeviceDatabase extends RoomDatabase {

    private static DeviceDatabase INSTANCE;

    // Getter pro DAO
    public abstract DeviceDao deviceDao();
    public abstract UserDao userDao();
    public abstract UserWithDevicesDao userWithDevicesDao();

    // Entwurfsmuster Singleton
    // Nur eine Instanz der Klasse darf erstellt werden
    public static DeviceDatabase getInstance(Context context) {

        // Haben wir schon eine Instanz erstellt
        if (INSTANCE == null) {

            // Sperre für andere Threads
            synchronized (DeviceDatabase.class) {

                if (INSTANCE == null) {
                    // Nein, es gibt noch keine Instanz
                    // -> eine neue Instanz erzeugen

                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DeviceDatabase.class,
                            "database.sqlite"
                    ).build();

                }
            }
        }

        return INSTANCE;
    }

}
