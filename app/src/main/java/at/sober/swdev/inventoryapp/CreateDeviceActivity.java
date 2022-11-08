package at.sober.swdev.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
    private static final int CAMERA_PERMISSION_CODE = 4;
    private ImageView imageView;
    private int CAMERA_REQUEST = 10;

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

        // Kamera konfigurieren
        this.imageView = (ImageView)this.findViewById(R.id.imageView);
        Button photoButton = (Button) this.findViewById(R.id.imageButton);

        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startCameraActivity();
                photoButton.setVisibility(View.INVISIBLE);
            }
        });

        this.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startCameraActivity();
            }
        });
    }

    private void startCameraActivity() {
        if (ContextCompat.checkSelfPermission(CreateDeviceActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateDeviceActivity.this,
                    Manifest.permission.CAMERA)) {


            } else {
                ActivityCompat.requestPermissions(CreateDeviceActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        else
        {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createDevice(View view) {

        ObjectMapper objectMapper = new ObjectMapper();

        // 1 Daten aus den Feldern auslesen
        String serial = binding.deviceSerial.getText().toString();
        String name = binding.deviceTitle.getText().toString();
        String category = binding.deviceCategory.getSelectedItem().toString();

        User user = (User) binding.deviceOwner.getSelectedItem();


        Drawable drawable = binding.imageView.getDrawable();
        Bitmap image = null;
        if (drawable != null)
            image = ((BitmapDrawable) drawable).getBitmap();

        String description = binding.deviceDescription.getText().toString();

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

        if (!name.isEmpty() && image != null) {
            // 2 Neue Note-Objekt erstellen
            Device device = new Device(name, category, serial, image, description);

            // 3 Ergebnisse verpacken
            Intent intent = new Intent();
            intent.putExtra("device", device)
                    .putExtra("user", user);

            // 4 Ergebnisse zurückmelden
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}