package at.sober.swdev.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

import at.sober.swdev.inventoryapp.databinding.ActivityCreateDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;
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
        List<String> nameJobTitleJson = viewModel.getAllUsersForSpinner().stream().map(User::toJsonString).collect(Collectors.toList());
        List<String> nameJobTitle = viewModel.getAllUsersForSpinner().stream().map(User::toString).collect(Collectors.toList());
        /*Map<String,String> hashMap = new HashMap<>();
        hashMap.keySet().addAll(nameJobTitleJson);
        hashMap.values().addAll(nameJobTitle);*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Data.categories);
        binding.deviceCategory.setAdapter(adapter);

        ArrayAdapter<User> adapter2 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, viewModel.getAllUsersForSpinner());
        binding.deviceOwner.setAdapter(adapter2);




        int pos = viewModel.getAllUsersForSpinner().indexOf(user);
        binding.deviceOwner.setSelection(pos);
    }

    public void createDevice(View view) {

        ObjectMapper objectMapper = new ObjectMapper();

        // 1 Daten aus den Feldern auslesen
        String serial = binding.deviceSerial.getText().toString();
        String name = binding.deviceTitle.getText().toString();
        String category = binding.deviceCategory.getSelectedItem().toString();

        User user = (User) binding.deviceOwner.getSelectedItem();

        /*String jsonString = binding.deviceOwner.getSelectedItem().toString();
        User user = null;
        try {
            user = objectMapper.readValue(jsonString, User.class);
        } catch (IOException e) {

        }*/

        /*User selectedUser;
        binding.deviceOwner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUser = (User) parent.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/



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