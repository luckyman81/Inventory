package at.sober.swdev.inventoryapp.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Device.class, User.class, DeviceUserCrossRef.class},
        autoMigrations = {
                @AutoMigration(from = 1, to =2)
        },
        exportSchema = true,
        version = 2)
@TypeConverters({DateConverter.class, ImageConverter.class})
public abstract class DeviceDatabase extends RoomDatabase {

    private static DeviceDatabase INSTANCE;
    /*private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE devices "
                    + " ADD COLUMN image BLOB");
        }
    };*/

    // Getter pro DAO
    public abstract DeviceDao deviceDao();
    public abstract UserDao userDao();
    public abstract UserWithDevicesDao userWithDevicesDao();
    public abstract DeviceWithUsersDao deviceWithUsersDao();

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
                    )
                            //.fallbackToDestructiveMigration()
                            //.addMigrations(MIGRATION_1_2)
                            .build();

                }
            }
        }

        return INSTANCE;
    }


}
