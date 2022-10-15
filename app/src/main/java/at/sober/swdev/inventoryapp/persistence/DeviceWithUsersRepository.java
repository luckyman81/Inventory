package at.sober.swdev.inventoryapp.persistence;


import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class DeviceWithUsersRepository {
    private DeviceWithUsersDao dao;
    private List<DeviceWithUsers> users;

    public DeviceWithUsersRepository(Application application) {
        DeviceDatabase database = DeviceDatabase.getInstance(application);
        dao = database.deviceWithUsersDao();
    }

    public List<DeviceWithUsers> getAllDevices(Device device) {

        users = getDeviceWithUsers(device);

        return users;
    }

    public List<DeviceWithUsers> getDeviceWithUsers(Device device) {
        try {

            return new GetDeviceWithUsersTask(dao).execute(device).get();
        } catch(Exception e) {
        }

        return null;
    }

    private static class GetDeviceWithUsersTask extends AsyncTask<Device, Void, List<DeviceWithUsers>> {
        private DeviceWithUsersDao dao;
        public GetDeviceWithUsersTask(DeviceWithUsersDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<DeviceWithUsers> doInBackground(Device... devices) {
            return dao.getDeviceWithUsers(devices[0].deviceId);

        }
    }
}
