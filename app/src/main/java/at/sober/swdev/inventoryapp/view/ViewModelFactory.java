package at.sober.swdev.inventoryapp.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;

    public ViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        // Ein neues NoteViewModel erstellen
        if (modelClass == DeviceViewModel.class) {
            return (T) new DeviceViewModel(application);
        } else if (modelClass == UserViewModel.class) {
            return (T) new UserViewModel(application);
        }

        return super.create(modelClass);
    }
}
