package at.sober.swdev.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    public static final int DEVICE_DETAILS_CODE = 3;
    private ActivityDeviceBinding binding;
    private RecyclerView recyclerView;
    private DeviceViewModel viewModel;
    private DeviceListAdapter adapter;

    /*@Override
    protected void onResume() {
        super.onResume();
        User user = (User) getIntent().getSerializableExtra("user");
        Device device = (Device) getIntent().getSerializableExtra("device");

        viewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(getApplication())
        ).get(DeviceViewModel.class);


        // 2 Notiz via Viewmodel in die Datenbank schreiben
        viewModel.update(device);
        // 3 Snackbar mit Info anzeigen
        Snackbar.make(binding.getRoot(), "Device aktualisiert!", Snackbar.LENGTH_LONG).show();

        // Update list in activity
        List<UserWithDevices> userWithDevices = viewModel.getUserWithDevices(user);
        if (userWithDevices != null)
            adapter.setDevices(userWithDevices.get(0).devices);
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("user");

        // ViewBinding konfigurieren
        binding = ActivityDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = (Toolbar) binding.toolbar;
        toolbar.setTitle(user.name);//getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                //setResult(RESULT_CANCELED);
            }
        });*/




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

                User user = (User) getIntent().getSerializableExtra("user");

                // startActivityForResult aufrufen und geklickte Notiz mitschicken
                Intent intent = new Intent(DeviceActivity.this, DisplayDeviceActivity.class);
                intent.putExtra("device", device)
                    .putExtra("user",user);


                startActivity(intent);


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



        // Prüfen von requestCode und resultCode
        if (requestCode == CREATE_DEVICE_CODE && resultCode == RESULT_OK) {
            // Verarbeiten der Daten im Intent
            if (data != null) {
                User user = (User) data.getSerializableExtra("user");

                // Neue Notiz in die Datenbank schreiben
                // 1 Notiz aus Intent auspacken
                Device device = (Device) data.getSerializableExtra("device");

                // 2 Notiz via Viewmodel in die Datenbank schreiben
                device.deviceId = viewModel.insert(device);
                viewModel.insertCrossRef(user.userId, device.deviceId);
                // 3 Snackbar mit Info anzeigen
                Snackbar.make(binding.getRoot(), "Device erstellt!", Snackbar.LENGTH_LONG).show();

                // Update list in activity
                List<UserWithDevices> userWithDevices = viewModel.getUserWithDevices(user);
                adapter.setDevices(userWithDevices.get(0).devices);
            }
        }

    }
}