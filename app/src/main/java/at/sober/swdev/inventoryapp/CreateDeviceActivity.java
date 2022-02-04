package at.sober.swdev.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import at.sober.swdev.inventoryapp.databinding.ActivityCreateDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;

public class CreateDeviceActivity extends AppCompatActivity {

    private ActivityCreateDeviceBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");

        // ViewBinding aktivieren
        binding = ActivityCreateDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Constants.categories);
        binding.deviceCategory.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Constants.owners);
        binding.deviceOwner.setAdapter(adapter2);
    }

    public void createDevice(View view) {

        // 1 Daten aus den Feldern auslesen
        String serial = binding.deviceSerial.getText().toString();
        String name = binding.deviceTitle.getText().toString();
        String category = binding.deviceCategory.getSelectedItem().toString();
        String owner = binding.deviceOwner.getSelectedItem().toString();

        // 2 Neue Note-Objekt erstellen
        Device device = new Device(name, category, serial, owner);

        // 3 Ergebnisse verpacken
        Intent intent = new Intent();
        intent.putExtra("device", device)
                .putExtra("user", user);

        // 4 Ergebnisse zurückmelden
        setResult(RESULT_OK, intent);
        finish();
    }


}