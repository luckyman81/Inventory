package at.sober.swdev.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import at.sober.swdev.inventoryapp.databinding.ActivityUpdateDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserWithDevices;
import at.sober.swdev.inventoryapp.view.DeviceListAdapter;
import at.sober.swdev.inventoryapp.view.DeviceViewModel;
import at.sober.swdev.inventoryapp.view.UserListAdapter;
import at.sober.swdev.inventoryapp.view.UserViewModel;
import at.sober.swdev.inventoryapp.view.ViewModelFactory;

public class UpdateDeviceActivity extends AppCompatActivity {

    private int UPDATE_DEVICE_CODE = 2;
    private ActivityUpdateDeviceBinding binding;
    private Device device;
    UserViewModel viewModel;
    DeviceViewModel deviceViewModel;
    List<User> users;
    DeviceListAdapter deviceListAdapter;

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

        deviceViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(getApplication())
        ).get(DeviceViewModel.class);

        deviceListAdapter = new DeviceListAdapter(this);

        binding.deviceOwner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.deviceOwner.setItemChecked(position, true);
            }
        });

        binding.deviceCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.deviceCategory.setSelection(position,false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Data.categories);
        binding.deviceCategory.setAdapter(adapter);

        ArrayAdapter<User> adapter2 = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_multiple_choice, viewModel.getAllUsersForSpinner());
        binding.deviceOwner.setAdapter(adapter2);

        // Note aus Intent auspacken
        device = (Device) getIntent().getSerializableExtra("device");

        // Felder mit den aktuellen Werten der Notiz befüllen
        binding.deviceTitle.setText(device.name);
        binding.deviceSerial.setText(device.serial);

        int pos = Data.categories.indexOf(device.category);
        binding.deviceCategory.setSelection(pos);

        users = viewModel.getDeviceWithUsers(device).get(0).users;

        if(users.size() != 0) {

            pos = viewModel.getAllUsersForSpinner().indexOf(users.get(0));
            binding.deviceOwner.setItemChecked(pos, true);
            // TODO multiple select
        }

        binding.imageView.setImageBitmap(device.image);
    }

    public void updateDevice(View view) {

        ObjectMapper objectMapper = new ObjectMapper();

        // Inhalte auslesen
        device.name = binding.deviceTitle.getText().toString();
        device.serial = binding.deviceSerial.getText().toString();

        int pos = binding.deviceCategory.getSelectedItemPosition();
        device.category = (String)binding.deviceCategory.getAdapter().getItem(pos);

        pos = binding.deviceOwner.getCheckedItemPosition();

        if (pos != -1) {
            User user = (User) binding.deviceOwner.getAdapter().getItem(pos);
            User oldUser = users.size() != 0 ? users.get(0) : null;
        /*User user = null;
        try {
            user = objectMapper.readValue(jsonString, User.class);
        } catch (IOException e) {
        }*/

            // Intent bauen
            Intent intent = new Intent();
            intent.putExtra("device", device)
                    .putExtra("user", user)
                    .putExtra("old_user", oldUser);

            // Ergebnisse zurückschicken
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }

        device.description = binding.deviceDescription.getText().toString();

        finish();


    }
}