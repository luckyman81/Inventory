package at.sober.swdev.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import at.sober.swdev.inventoryapp.databinding.ActivityUpdateDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;

public class UpdateDeviceActivity extends AppCompatActivity {

    private ActivityUpdateDeviceBinding binding;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Constants.categories);
        binding.deviceCategory.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Constants.owners);
        binding.deviceOwner.setAdapter(adapter2);

        // Note aus Intent auspacken
        device = (Device) getIntent().getSerializableExtra("device");

        // Felder mit den aktuellen Werten der Notiz befüllen
        binding.deviceTitle.setText(device.name);
        binding.deviceCategory.setSelection(getIndex(binding.deviceCategory, device.category));
        binding.deviceSerial.setText(device.serial);
        binding.deviceOwner.setSelection(getIndex(binding.deviceOwner, device.owner));
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equals(myString)){
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
        device.owner = binding.deviceOwner.getSelectedItem().toString();

        // Intent bauen
        Intent intent = new Intent();
        intent.putExtra("device", device);

        // Ergebnisse zurückschicken
        setResult(RESULT_OK, intent);
        finish();
    }

}