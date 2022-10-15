package at.sober.swdev.inventoryapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserRepository;
import at.sober.swdev.inventoryapp.view.UserViewModel;

public class Data {

    static List<String> categories = new ArrayList<String>(Arrays.asList("Input devices","Computers","Output devices","Storages"));

}
