package at.sober.swdev.inventoryapp.persistence;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private UserDao dao;
    private LiveData<List<User>> users;

    public UserRepository(Application application) {
        DeviceDatabase database = DeviceDatabase.getInstance(application);
        dao = database.userDao();

        // Insert Demo Users
        /*deleteAll();
        insert(new User("Obergröbner", "Developer"));
        insert(new User("Tauchner", "Developer"));*/

        // LiveData abholen
        users = dao.getAllUsers();
        

    }

    public LiveData<List<User>> getAllUsers() {
        return users;
    }

    public List<User> getAllUsersForSpinner() {

        try {

            return new GetAllUsersForSpinnerTask(dao).execute().get();
        } catch(Exception e) {
        }

        return null;

    }

    public void insert(User user) {
        // Insert in eigenem Thread
        new InsertUserTask(dao).execute(user);
    }

    public void update(User user) {
        new UserRepository.UpdateUserTask(dao).execute(user);
    }

    public void delete(User user) {
        new UserRepository.DeleteUserTask(dao).execute(user);
    }

    public void deleteAll() {
        new UserRepository.DeleteAllTask(dao).execute();
    }

    // Async Tasks pro CRUD Operation als innere Klassen
    private static class InsertUserTask extends AsyncTask<User, Void, Void> {

        private UserDao dao;

        public InsertUserTask(UserDao dao) {
            this.dao = dao;
        }

        // Diese Methode läuft in einem eigen Thread
        @Override
        protected Void doInBackground(User... users) {
            // Eine Notiz in die Datenbank einfügen
            dao.insert(users);

            return null;
        }
    }


    private static class UpdateUserTask extends AsyncTask<User, Void, Void> {

        private UserDao dao;

        public UpdateUserTask(UserDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.update(users[0]);

            return null;
        }
    }

    private static class DeleteUserTask extends AsyncTask<User, Void, Void> {

        private UserDao dao;

        public DeleteUserTask(UserDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.delete(users[0]);
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<User, Void, Void> {

        private UserDao dao;

        public DeleteAllTask(UserDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User... users) {
            dao.deleteAllUsers();
            return null;
        }
    }

    private static class GetAllUsersForSpinnerTask extends AsyncTask<User, Void, List<User>> {
        private UserDao dao;

        public GetAllUsersForSpinnerTask(UserDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<User> doInBackground(User... users) {
            return dao.getAllUsersForSpinner();

        }
    }
}
