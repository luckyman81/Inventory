package at.sober.swdev.inventoryapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import at.sober.swdev.inventoryapp.R;
import at.sober.swdev.inventoryapp.persistence.User;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    // Siehe Reactive X bzw. RxJava
    private PublishSubject<User> onClickSubject = PublishSubject.create();

    private LayoutInflater inflater;
    private Context context;
    private List<User> users;

    public UserListAdapter(Context context) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setUsers(List<User> users) {
        this.users = users;
        // Damit teilen wir dem Adapter mit, dass sich die anzuzeigenden Daten
        // geändert haben.
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public User getUserAt(int position) {
        if (users != null) {
            return users.get(position);
        }

        return null;
    }

    public Observable<User> getClickedUserObserver() {
        return onClickSubject;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1 Aus dem XML-Layout ein Objekt erstellen
        View itemView = inflater.inflate(R.layout.user_list_item, parent, false);

        // 2 Einen NoteViewHolder erzeugen und zurückgeben
        return new UserListAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Verknüpfen der einzelnen Note mit ihrer "Anzeigefläche"

        // Hat schon jemand Daten mit setNotes() geliefert?
        if (users != null) {
            // Notiz an der Stelle "position" aus der Liste "notes" holen
            final User user = users.get(position);

            // Inhalt von note im ItemView anzeigen
            holder.userNameTV.setText(user.name);
            holder.jobTitleTV.setText(user.jobTitle);
            // Ternärer Operator
            //holder.doneIV.setVisibility(device.done ? View.VISIBLE : View.INVISIBLE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Es wurde auf dieses Listenelement geklickt
                    onClickSubject.onNext(user);
                }
            });

        }
    }

    @Override
    public int getItemCount() {

        if (users != null) {
            return users.size();
        }

        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private final TextView userNameTV;
        private final TextView jobTitleTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTV = itemView.findViewById(R.id.userNameTV);
            jobTitleTV = itemView.findViewById(R.id.jobTitleTV);
        }
    }
}
