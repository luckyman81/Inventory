package at.sober.swdev.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import at.sober.swdev.inventoryapp.databinding.ActivityDeviceBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserWithDevices;
import at.sober.swdev.inventoryapp.view.DeviceListAdapter;
import at.sober.swdev.inventoryapp.view.DeviceViewModel;
import at.sober.swdev.inventoryapp.view.SwipeToDeleteCallback;
import at.sober.swdev.inventoryapp.view.ViewModelFactory;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class DeviceActivity extends AppCompatActivity {

    public static final int CREATE_DEVICE_CODE = 1;
    public static final int UPDATE_DEVICE_CODE = 2;
    private ActivityDeviceBinding binding;
    private RecyclerView recyclerView;
    private DeviceViewModel viewModel;
    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding konfigurieren
        binding = ActivityDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecyclerView - Anzeigen der Liste
        recyclerView = binding.recyclerView;
        adapter = new DeviceListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ViewModel - Holen der Daten aus der Datenbank
        viewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(getApplication())
        ).get(DeviceViewModel.class);

        // LiveData vom ViewModel abonnieren, dadurch werden wir verständigt, wenn
        // sich etwas an den Notizen geändert hat.
        /*viewModel.getAllDevices().observe(this, new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                adapter.setDevices(devices);
            }
        });*/

        User user = (User) getIntent().getSerializableExtra("user");


        List<UserWithDevices> userWithDevices = viewModel.getUserWithDevices(user);

        if (userWithDevices !=null)
            adapter.setDevices(userWithDevices.get(0).devices);
        setResult(RESULT_OK);


        // Swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, viewModel));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.getClickedUserObserver().subscribe(new io.reactivex.Observer<Device>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Egal
            }

            @Override
            public void onNext(@NonNull Device device) {
                // Hier bekommen wir die Notiz, auf die gerade geklickt wurde

                // startActivityForResult aufrufen und geklickte Notiz mitschicken
                Intent intent = new Intent(DeviceActivity.this, UpdateDeviceActivity.class);
                intent.putExtra("device", device);

                startActivityForResult(intent, UPDATE_DEVICE_CODE);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                // Egal
            }

            @Override
            public void onComplete() {
                // Egal
            }
        });


    }

    public void startNewDeviceActivity(View view) {
        User user = (User) getIntent().getSerializableExtra("user");

        Intent intent = new Intent(this, CreateDeviceActivity.class);
        intent.putExtra("user", user);

        startActivityForResult(intent, CREATE_DEVICE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        User user = (User) data.getSerializableExtra("user");

        // Prüfen von requestCode und resultCode
        if (requestCode == CREATE_DEVICE_CODE && resultCode == RESULT_OK) {
            // Verarbeiten der Daten im Intent
            if (data != null) {
                // Neue Notiz in die Datenbank schreiben
                // 1 Notiz aus Intent auspacken
                Device device = (Device) data.getSerializableExtra("device");

                // 2 Notiz via Viewmodel in die Datenbank schreiben
                device.deviceId = viewModel.insert(device);
                viewModel.insertCrossRef(user.userId, device.deviceId);
                // 3 Snackbar mit Info anzeigen
                Snackbar.make(binding.getRoot(), "Device erstellt!", Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == UPDATE_DEVICE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Device device = (Device) data.getSerializableExtra("device");
                // 2 Notiz via Viewmodel in die Datenbank schreiben
                viewModel.update(device);

                // 3 Snackbar mit Info anzeigen
                Snackbar.make(binding.getRoot(), "Device aktualisiert!", Snackbar.LENGTH_LONG).show();
            }
        }

        // Update list in activity
        List<UserWithDevices> userWithDevices = viewModel.getUserWithDevices(user);
        adapter.setDevices(userWithDevices.get(0).devices);


    }
}