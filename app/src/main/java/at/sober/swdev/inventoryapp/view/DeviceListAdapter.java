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
import at.sober.swdev.inventoryapp.persistence.Device;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {

    // Siehe Reactive X bzw. RxJava
    private PublishSubject<Device> onClickSubject = PublishSubject.create();

    private LayoutInflater inflater;
    private Context context;
    private List<Device> devices;

    public DeviceListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
        // Damit teilen wir dem Adapter mit, dass sich die anzuzeigenden Daten
        // geändert haben.
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public Device getNoteAt(int position) {
        if (devices != null) {
            return devices.get(position);
        }

        return null;
    }

    public Observable<Device> getClickedUserObserver() {
        return onClickSubject;
    }


    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 1 Aus dem XML-Layout ein Objekt erstellen
        View itemView = inflater.inflate(R.layout.device_list_item, parent, false);

        // 2 Einen NoteViewHolder erzeugen und zurückgeben
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        // Verknüpfen der einzelnen Note mit ihrer "Anzeigefläche"

        // Hat schon jemand Daten mit setNotes() geliefert?
        if (devices != null) {
            // Notiz an der Stelle "position" aus der Liste "notes" holen
            final Device device = devices.get(position);

            // Inhalt von note im ItemView anzeigen
            holder.deviceNameTV.setText(device.name);
            holder.createdAtTV.setText(device.createdAt.toString());
            // Ternärer Operator
            //holder.doneIV.setVisibility(device.done ? View.VISIBLE : View.INVISIBLE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Es wurde auf dieses Listenelement geklickt
                    onClickSubject.onNext(device);
                }
            });

        }
    }

    @Override
    public int getItemCount() {

        // Wenn wir schon Notizen, geben wir die Anzahl zurück
        if (devices != null) {
            return devices.size();
        }

        // Sonst 0
        return 0;
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {

        private final TextView deviceNameTV;
        private final TextView createdAtTV;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNameTV = itemView.findViewById(R.id.deviceNameTV);
            createdAtTV = itemView.findViewById(R.id.createdAtTV);
        }
    }
}
