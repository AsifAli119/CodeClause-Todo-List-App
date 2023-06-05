package com.example.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Adapter.TodoAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
//-----------------------------------------------------------------------------





public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private TodoAdapter adapter;
    public TouchHelper(TodoAdapter adapter) {
        super(0,ItemTouchHelper.LEFT  | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setMessage(" Are you sure?").setTitle("Delete Task")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteTask(position);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(position);
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (direction == ItemTouchHelper.LEFT) {
            // Handle left swipe for update action
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setMessage("Edit Task?")
                    .setTitle("Edit Task")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.editTAsk(position);
                            // Implement the code to edit the task at the specified position
                            // You can open an activity or a dialog to allow the user to edit the task
                            // You can pass the task information (e.g., position, task object) to the editing activity/dialog
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(position);
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeRightActionIcon(R.drawable.del)
                .addSwipeRightBackgroundColor(Color.RED)
                .addSwipeLeftActionIcon(R.drawable.edit)
                .addSwipeLeftBackgroundColor(Color.GREEN)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
