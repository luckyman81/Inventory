package at.sober.swdev.inventoryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import at.sober.swdev.inventoryapp.databinding.ActivityUserBinding;
import at.sober.swdev.inventoryapp.persistence.Device;
import at.sober.swdev.inventoryapp.persistence.User;
import at.sober.swdev.inventoryapp.persistence.UserWithDevices;
import at.sober.swdev.inventoryapp.view.UserListAdapter;
import at.sober.swdev.inventoryapp.view.UserViewModel;
import at.sober.swdev.inventoryapp.view.ViewModelFactory;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class UserActivity extends AppCompatActivity {

    public static final int CREATE_DEVICE_CODE = 1;
    public static final int UPDATE_DEVICE_CODE = 2;
    public static final int DISPLAY_DEVICES_CODE = 3;
    private ActivityUserBinding binding;
    private RecyclerView recyclerView;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerView;
        UserListAdapter adapter = new UserListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(
                this,
                new ViewModelFactory(getApplication())
        ).get(UserViewModel.class);

        viewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });

        adapter.getClickedUserObserver().subscribe(new io.reactivex.Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Egal
            }

            @Override
            public void onNext(@NonNull User user) {
                // Hier bekommen wir die Notiz, auf die gerade geklickt wurde

                // startActivityForResult aufrufen und geklickte Notiz mitschicken
                Intent intent = new Intent(UserActivity.this, DeviceActivity.class);
                intent.putExtra("user", user);

                startActivityForResult(intent, DISPLAY_DEVICES_CODE);
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
        Intent intent = new Intent(this, CreateDeviceActivity.class);
        startActivityForResult(intent, CREATE_DEVICE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Prüfen von requestCode und resultCode
        if (requestCode == CREATE_DEVICE_CODE && resultCode == RESULT_OK) {
            // Verarbeiten der Daten im Intent

            if (data != null) {
                // Neue Notiz in die Datenbank schreiben
                // 1 Notiz aus Intent auspacken
                Device device = (Device) data.getSerializableExtra("device");
                // 2 Notiz via Viewmodel in die Datenbank schreiben
                //viewModel.insert(device);
                // 3 Snackbar mit Info anzeigen
                Snackbar.make(binding.getRoot(), "Device erstellt!", Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == UPDATE_DEVICE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Device device = (Device) data.getSerializableExtra("device");
                // 2 Notiz via Viewmodel in die Datenbank schreiben
                //viewModel.update(device);
                // 3 Snackbar mit Info anzeigen
                Snackbar.make(binding.getRoot(), "Device aktualisiert!", Snackbar.LENGTH_LONG).show();

            }


        } else if (requestCode == DISPLAY_DEVICES_CODE && resultCode == RESULT_OK) {


        }
    }
}