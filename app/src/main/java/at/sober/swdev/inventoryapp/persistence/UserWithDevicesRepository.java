package at.sober.swdev.inventoryapp.persistence;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserWithDevicesRepository {
    private UserWithDevicesDao dao;
    private List<UserWithDevices> devices;

    public UserWithDevicesRepository(Application application) {
        DeviceDatabase database = DeviceDatabase.getInstance(application);
        dao = database.userWithDevicesDao();


    }

    public List<UserWithDevices> getAllDevices(User user) {

        devices = readUserWithDevices(user); //dao.getUserWithDevices(userId);

        return devices;
    }

    public List<UserWithDevices> readUserWithDevices(User user) {
        try {

            return new ReadUserWithDevicesTask(dao).execute(user).get();
        } catch(Exception e) {
        }

        return null;
    }

    private static class ReadUserWithDevicesTask extends AsyncTask<User, Void, List<UserWithDevices>> {

        private UserWithDevicesDao dao;

        public ReadUserWithDevicesTask(UserWithDevicesDao dao) {
            this.dao = dao;
        }

        // Diese Methode läuft in einem eigenen Thread
        @Override
        protected List<UserWithDevices> doInBackground(User... users) {
            return dao.getUserWithDevices(users[0].userId);

        }
    }

    public void insertCrossRef(long userId, long deviceId) {
       new InsertCrossRefTask(dao).execute(userId,deviceId);
    }

    private static class InsertCrossRefTask extends AsyncTask<Long, Void, Void> {

        private UserWithDevicesDao dao;

        public InsertCrossRefTask(UserWithDevicesDao dao) {
            this.dao = dao;
        }

        // Diese Methode läuft in einem eigenen Thread
        @Override
        protected Void doInBackground(Long... ids) {
            dao.insertCrossRef(ids[1], ids[0]);

            return null;
        }
    }
}
