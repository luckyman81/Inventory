package at.sober.swdev.inventoryapp.persistence;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class UserWithDevicesRepository {
    private UserWithDevicesDao dao;
    private List<UserWithDevices> devices;

    public UserWithDevicesRepository(Application application) {
        DeviceDatabase database = DeviceDatabase.getInstance(application);
        dao = database.userWithDevicesDao();


    }

    public List<UserWithDevices> getAllDevices(User user) {

        devices = getUserWithDevices(user);

        return devices;
    }

    public List<UserWithDevices> getUserWithDevices(User user) {
        try {

            return new GetUserWithDevicesTask(dao).execute(user).get();
        } catch(Exception e) {
        }

        return null;
    }



    private static class GetUserWithDevicesTask extends AsyncTask<User, Void, List<UserWithDevices>> {

        private UserWithDevicesDao dao;

        public GetUserWithDevicesTask(UserWithDevicesDao dao) {
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

    public void deleteCrossRef(long deviceId, long userId) {
        new DeleteCrossRefTask(dao).execute(userId,deviceId);
    }

    private static class DeleteCrossRefTask extends AsyncTask<Long, Void, Void> {

        private UserWithDevicesDao dao;

        public DeleteCrossRefTask(UserWithDevicesDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Long... ids) {
            dao.deleteCrossRef(ids[1], ids[0]);

            return null;
        }
    }
}
