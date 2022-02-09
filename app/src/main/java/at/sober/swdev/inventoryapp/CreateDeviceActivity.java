package at.sober.swdev.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import at.sober.swdev.inventoryapp.databinding.ActivityCreateDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.view.DeviceViewModel;
import at.sober.swdev.inventoryapp.view.UserViewModel;
import at.sober.swdev.inventoryapp.view.ViewModelFactory;

public class CreateDeviceActivity extends AppCompatActivity {

    private ActivityCreateDeviceBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        // ViewBinding aktivieren
        binding = ActivityCreateDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserViewModel viewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(getApplication())
        ).get(UserViewModel.class);

        String[] owners = viewModel.getAllUsersForSpinner().stream().map(User::toString).toArray(String[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Constants.categories);
        binding.deviceCategory.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, owners);
        binding.deviceOwner.setAdapter(adapter2);


    }

    public void createDevice(View view) {

        ObjectMapper objectMapper = new ObjectMapper();

        // 1 Daten aus den Feldern auslesen
        String serial = binding.deviceSerial.getText().toString();
        String name = binding.deviceTitle.getText().toString();
        String category = binding.deviceCategory.getSelectedItem().toString();

        String jsonString = binding.deviceOwner.getSelectedItem().toString();
        User user = null;
        try {
            user = objectMapper.readValue(jsonString, User.class);
        } catch (IOException e) {

        }


        // 2 Neue Note-Objekt erstellen
        Device device = new Device(name, category, serial);

        // 3 Ergebnisse verpacken
        Intent intent = new Intent();
        intent.putExtra("device", device)
                .putExtra("user", user);

        // 4 Ergebnisse zurückmelden
        setResult(RESULT_OK, intent);
        finish();
    }


}