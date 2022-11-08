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
                /*@AutoMigration(from = 1, to = 2),
                @AutoMigration(from = 2, to = 3),
                @AutoMigration(from = 3, to = 4)*/
        },
        exportSchema = true,
        version = 3)
@TypeConverters({DateConverter.class, ImageConverter.class})
public abstract class DeviceDatabase extends RoomDatabase {

    private static DeviceDatabase INSTANCE;
    /*private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE devices "
                    + " ADD COLUMN image BLOB");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE devices "
                    + " ADD COLUMN description TEXT");
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
                            .fallbackToDestructiveMigration()
                            /*.addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)*/
                            .build();

                }
            }
        }

        return INSTANCE;
    }


}
