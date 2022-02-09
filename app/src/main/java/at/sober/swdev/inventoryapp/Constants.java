package at.sober.swdev.inventoryapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserRepository;
import at.sober.swdev.inventoryapp.view.UserViewModel;

public class Constants {
    UserViewModel viewModel;

    public static final String[] categories = {"Input devices", "Computers", "Output devices", "Storages"};
    public static LiveData<List<User>> owners;

    public Constants() {

        owners = viewModel.getAllUsers();
    }
}
