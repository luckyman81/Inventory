package at.sober.swdev.inventoryapp.view;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import at.sober.swdev.inventoryapp.R;
import at.sober.swdev.inventoryapp.persistence.Device;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private DeviceListAdapter adapter;
    private DeviceViewModel viewModel;
    private Drawable icon;
    private ColorDrawable background;
    private Device recentlyDeletedDevice;

    public SwipeToDeleteCallback(DeviceListAdapter adapter, DeviceViewModel viewModel) {

        //0001...TOP
        //0010...BOTTOM
        //0100...LEFT
        //1000...RIGHT
        //_________
        //1100
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.viewModel = viewModel;
        // Icon aus dem Drawable-Resource Folder
        icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.delete);
        background = new ColorDrawable(adapter.getContext().getResources().getColor(R.color.color_delete));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // Wir wollen nur Swipes, Move ist egal (nur Drag-Gesten)
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        // Margin, damit Icon innerhalb des Listen-Elements vertikal zentriert ist
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

        // Wo positioniere ich die obere Kante des Icons, ausgehend von der oberen Kante des Listen-Elements
        int iconTop = itemView.getTop() + iconMargin;
        int iconBottom = iconTop + icon.getIntrinsicHeight();
        // alternativ:
        // int iconBottom = itemView.getBottom() - iconMargin;

        /**
         * 0,0--------------------> x
         * |
         * |
         * |
         * |
         * v
         *
         * y
         */
        // In welche Richtung wurde geswiped?
        // dX ist der Abstand zwischen dem linken bzw. rechten Rand des Bildschirms und dem linken bzw. rechten Rand des Listen-Elements
        if (dX > 0) {
            // Schiebe-Effekt für Icon (linke bzw. rechte Seite ändern sich während des Swipe-Vorgangs)
            int slideIn = ((int) dX < iconMargin + icon.getIntrinsicWidth() + iconMargin) ? (int) dX - icon.getIntrinsicWidth() - 2 * iconMargin : 0;

            // wir wischen von links nach rechts (x wird größer)
            background.setBounds(
                    itemView.getLeft(),
                    itemView.getTop(),
                    itemView.getLeft() + (int) dX,
                    itemView.getBottom()
            );
            icon.setBounds(
                    itemView.getLeft() + iconMargin + slideIn,
                    iconTop,
                    itemView.getLeft() + iconMargin + icon.getIntrinsicWidth() + slideIn,
                    iconBottom
            );
        } else if (dX < 0) {

            // Schiebe-Effekt für Icon (linke bzw. rechte Seite ändern sich während des Swipe-Vorgangs)
            int slideIn = ((int) dX > -2 * iconMargin - icon.getIntrinsicWidth()) ? (int) dX + icon.getIntrinsicWidth() + 2 * iconMargin : 0;

            // rechts nach links
            background.setBounds(
                    itemView.getRight() + (int) dX, // dX ist schon negativ, deshalb hier plus
                    itemView.getTop(),
                    itemView.getRight(),
                    itemView.getBottom()
            );

            icon.setBounds(
                    itemView.getRight() - iconMargin - icon.getIntrinsicWidth() + slideIn,
                    iconTop,
                    itemView.getRight() - iconMargin + slideIn,
                    iconBottom
            );
        } else {
            // Un-Swipe, Background und Icon nicht zeichnen
            background.setBounds(0, 0, 0, 0);
            icon.setBounds(0, 0, 0, 0);
        }

        // Reihenfolge wichtig, da Elemente übereinander gezeichnet werden
        background.draw(canvas);
        icon.draw(canvas);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        // Welches Element wurde gewischt?
        int position = viewHolder.getAdapterPosition();

        // Note, die gelöscht werden soll
        recentlyDeletedDevice = adapter.getNoteAt(position);

        // Notiz aus der DB löschen
        viewModel.delete(recentlyDeletedDevice);

        // Undo-Möglichkeit
        showUndoSnackbar(viewHolder.itemView);
    }

    private void showUndoSnackbar(View view) {
        Snackbar.make(view, "Notiz gelöscht", Snackbar.LENGTH_LONG)
                .setAction("Rückgängig", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewModel.insert(recentlyDeletedDevice);
                    }
                }).show();
    }
}
