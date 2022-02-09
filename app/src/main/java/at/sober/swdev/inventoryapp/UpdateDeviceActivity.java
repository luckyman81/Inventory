package at.sober.swdev.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.stream.Collectors;

import at.sober.swdev.inventoryapp.databinding.ActivityUpdateDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.view.UserListAdapter;
import at.sober.swdev.inventoryapp.view.UserViewModel;
import at.sober.swdev.inventoryapp.view.ViewModelFactory;

public class UpdateDeviceActivity extends AppCompatActivity {

    private ActivityUpdateDeviceBinding binding;
    private Device device;
    UserViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserListAdapter userListAdapter = new UserListAdapter(this);

        viewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(getApplication())
        ).get(UserViewModel.class);

        List<User> users = viewModel.getAllUsersForSpinner();
        User[] owners = users.stream().toArray(User[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Constants.categories);
        binding.deviceCategory.setAdapter(adapter);

        ArrayAdapter<User> adapter2 = new ArrayAdapter<User>(this,R.layout.support_simple_spinner_dropdown_item, owners);
        binding.deviceOwner.setAdapter(adapter2);

        // Note aus Intent auspacken
        device = (Device) getIntent().getSerializableExtra("device");

        // Felder mit den aktuellen Werten der Notiz befüllen
        binding.deviceTitle.setText(device.name);
        binding.deviceCategory.setSelection(this.<Spinner, String>getIndex(binding.deviceCategory, device.category));
        binding.deviceSerial.setText(device.serial);
        binding.deviceOwner.setSelection(this.<Spinner, User>getIndex(binding.deviceOwner, device.owner));
    }

    private <S,T> int getIndex (S spinner, T myString) {
        int index = 0;
        for (int i=0;i<((Spinner)spinner).getCount();i++){
            if (((Spinner)spinner).getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void updateDevice(View view) {

        // Inhalte auslesen
        device.name = binding.deviceTitle.getText().toString();
        device.category = binding.deviceCategory.getSelectedItem().toString();
        device.serial = binding.deviceSerial.getText().toString();
        device.owner = (User)binding.deviceOwner.getSelectedItem();

        // Intent bauen
        Intent intent = new Intent();
        intent.putExtra("device", device);

        // Ergebnisse zurückschicken
        setResult(RESULT_OK, intent);
        finish();

    }

}