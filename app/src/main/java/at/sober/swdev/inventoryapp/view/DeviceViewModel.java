package at.sober.swdev.inventoryapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.DeviceRepository;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserWithDevices;
import at.sober.swdev.inventoryapp.persistence.UserWithDevicesRepository;

public class DeviceViewModel extends AndroidViewModel {

    private DeviceRepository repository;
    private LiveData<List<Device>> devices;

    private UserWithDevicesRepository userWithDevicesRepository;
    private List<UserWithDevices> userWithDevices;

    public DeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(application);
        devices = repository.getAllDevices();

        userWithDevicesRepository = new UserWithDevicesRepository(application);
    }

    public long insert(Device device) {
        return repository.insert(device);
    }

    public void insertCrossRef(long userId, long deviceId) {
        userWithDevicesRepository.insertCrossRef(userId,deviceId);
    }

    public void update(Device device) {
        repository.update(device);
    }

    public void delete(Device device) {
        repository.delete(device);
    }

    public LiveData<List<Device>> getAllDevices() {
        return devices;
    }

    public List<UserWithDevices> getUserWithDevices(User user) {

        userWithDevices = userWithDevicesRepository.getAllDevices(user);

        return userWithDevices;
    }

}
