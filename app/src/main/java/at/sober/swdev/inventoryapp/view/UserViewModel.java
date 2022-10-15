package at.sober.swdev.inventoryapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.DeviceRepository;
import at.sober.swdev.inventoryapp.persistence.DeviceWithUsers;
import at.sober.swdev.inventoryapp.persistence.DeviceWithUsersRepository;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserRepository;
import at.sober.swdev.inventoryapp.persistence.UserWithDevices;
import at.sober.swdev.inventoryapp.persistence.UserWithDevicesRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> users;
    private DeviceWithUsersRepository deviceWithUsersRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);

        repository = new UserRepository(application);
        deviceWithUsersRepository = new DeviceWithUsersRepository(application);
        users = repository.getAllUsers();

    }

    public LiveData<List<User>> getAllUsers() {
        return users;
    }

    public List<User> getAllUsersForSpinner() {
        return repository.getAllUsersForSpinner();
    }

    public List<DeviceWithUsers> getDeviceWithUsers(Device device) {

        return deviceWithUsersRepository.getAllDevices(device);


    }
}
