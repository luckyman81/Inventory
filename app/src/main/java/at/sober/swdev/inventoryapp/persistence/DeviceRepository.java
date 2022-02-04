package at.sober.swdev.inventoryapp.persistence;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Das Repository abstrahiert den direkten
 * Zugriff auf das DAO und sorgt zudem dafür,
 * dass Datenbank-Abfragen in einem eigenen Thread
 * ausgeführt werden.
 */
public class DeviceRepository {

    private DeviceDao dao;
    private LiveData<List<Device>> notes;

    public DeviceRepository(Application application) {
        DeviceDatabase database = DeviceDatabase.getInstance(application);
        dao = database.deviceDao();

        // LiveData anholen
        notes = dao.getAllDevices();
    }

    public LiveData<List<Device>> getAllDevices() {
        return notes;
    }

    public long insert(Device device) {
        // Insert in eigenem Thread
        try {
            return new InsertDeviceTask(dao).execute(device).get();
        } catch (Exception e) {
        }

        return 0;
    }

    public void update(Device device) {
        new UpdateDeviceTask(dao).execute(device);
    }

    public void delete(Device device) {
        new DeleteDeviceTask(dao).execute(device);
    }


    // Async Tasks pro CRUD Operation als innere Klassen
    private static class InsertDeviceTask extends AsyncTask<Device, Void, Long> {

        private DeviceDao dao;

        public InsertDeviceTask(DeviceDao dao) {
            this.dao = dao;
        }

        // Diese Methode läuft in einem eigen Thread
        @Override
        protected Long doInBackground(Device... devices) {
            // Eine Notiz in die Datenbank einfügen
            return dao.insert(devices[0]);

        }
    }


    private static class UpdateDeviceTask extends AsyncTask<Device, Void, Void> {

        private DeviceDao dao;

        public UpdateDeviceTask(DeviceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            dao.update(devices[0]);

            return null;
        }
    }

    private static class DeleteDeviceTask extends AsyncTask<Device, Void, Void> {

        private DeviceDao dao;

        public DeleteDeviceTask(DeviceDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Device... devices) {
            dao.delete(devices[0]);
            return null;
        }
    }


}
