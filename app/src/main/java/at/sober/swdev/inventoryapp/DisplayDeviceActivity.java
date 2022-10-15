package at.sober.swdev.inventoryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.sober.swdev.inventoryapp.databinding.ActivityDeviceBinding;
import at.sober.swdev.inventoryapp.databinding.ActivityDisplayDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserWithDevices;
import at.sober.swdev.inventoryapp.view.DeviceListAdapter;
import at.sober.swdev.inventoryapp.view.DeviceViewModel;
import at.sober.swdev.inventoryapp.view.ViewModelFactory;

public class DisplayDeviceActivity extends AppCompatActivity {

    private static final int UPDATE_DEVICE_CODE = 2;
    private static final int DELETE_DEVICE_CODE = 3;
    private ActivityDisplayDeviceBinding binding;
    private DeviceViewModel viewModel;
    private DeviceListAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Device device = (Device) getIntent().getSerializableExtra("device");
        User user = (User) getIntent().getSerializableExtra("user");

        //noinspection SimplifiableIfStatement
        if (id == R.id.editDevice) {
            Intent intent = new Intent(DisplayDeviceActivity.this, UpdateDeviceActivity.class);
            intent.putExtra("device", device);

            startActivityForResult(intent, UPDATE_DEVICE_CODE);

        } else if (id == R.id.deleteDevice) {
            viewModel.delete(device);
            viewModel.deleteCrossRef(device.deviceId,user.userId);
            Snackbar.make(binding.getRoot(), "Device gelöscht!", Snackbar.LENGTH_LONG).show();

            List<UserWithDevices> userWithDevices = viewModel.getUserWithDevices(user);

            if (userWithDevices !=null)
                adapter.setDevices(userWithDevices.get(0).devices);

            // TODO Löschen richtig stellen - activty die dahinterliegt aktual.
            Intent intent = new Intent(this, DeviceActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);




        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");
        Device device = (Device) getIntent().getSerializableExtra("device");

        // ViewBinding konfigurieren
        binding = ActivityDisplayDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new DeviceListAdapter(this);

        // ViewModel - Holen der Daten aus der Datenbank
        viewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(getApplication())
        ).get(DeviceViewModel.class);

        Toolbar toolbar = (Toolbar) binding.toolbar;
        toolbar.setTitle(user.name);//getString(R.string.app_name));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        setDeviceDetails(user,device);

    }

    private void setDeviceDetails(User user, Device device) {


        ObjectMapper mapper =new ObjectMapper();

        binding.titleTV.setText(device.name);
        binding.categoryTV.setText(device.category);
        binding.serialTV.setText(device.serial);

        String jsonString = "";
        Map<String, Object> map = new HashMap<>();
        try {
            jsonString = mapper.writeValueAsString(user);
            map  = mapper.readValue(jsonString, new TypeReference<Map<String,Object>>(){});

        } catch (JsonProcessingException e) {

        } catch (IOException e) {

        }

        binding.ownerTV.setText(map.get("name") + ", " + map.get("jobTitle"));
        //binding.descriptionTV.setText(device.description);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_DEVICE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                User user = (User) data.getSerializableExtra("user");
                User oldUser = (User) data.getSerializableExtra("old_user");
                Device device = (Device) data.getSerializableExtra("device");
                // 2 Notiz via Viewmodel in die Datenbank schreiben
                viewModel.delete(device);
                if(oldUser != null)
                    viewModel.deleteCrossRef(device.deviceId,oldUser.userId );
                // TODO duplicate entries, update verwenden
                device.owner = user;
                device.owner.userId = user.userId;
                device.deviceId = viewModel.insert(device);

                viewModel.insertCrossRef(user.userId, device.deviceId);

                //viewModel.update(device);

                // 3 Snackbar mit Info anzeigen
                Snackbar.make(binding.getRoot(), "Device aktualisiert!", Snackbar.LENGTH_LONG).show();

                // Device Details aktualisieren
                setDeviceDetails(user,device);
            }
        } else if (requestCode == DELETE_DEVICE_CODE && resultCode == RESULT_OK) {

        }
    }
}