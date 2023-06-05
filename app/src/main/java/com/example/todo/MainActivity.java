package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todo.Adapter.TodoAdapter;
import com.example.todo.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListner{
    private RecyclerView recyclerView;
    private FloatingActionButton mfab;
    private FirebaseFirestore firestore;
    private TodoAdapter adapter;
    private List<ToDoModel> mlist;
    private Query query;
    private ListenerRegistration listenerRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycle);
        mfab = findViewById(R.id.addT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        firestore = FirebaseFirestore.getInstance();

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
        mlist = new ArrayList<>();
        adapter = new TodoAdapter(MainActivity.this, mlist);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        showData();

    }
    private void showData(){
       query =  firestore.collection("task").orderBy("time", Query.Direction.DESCENDING);
       listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        String id = documentChange.getDocument().getId();
                        ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                        mlist.add(toDoModel);
                        adapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
            }
        });
    }

    @Override
    public void onDialogCLoseListner(DialogInterface dialogInterface) {
        showData();
        mlist.clear();
        adapter.notifyDataSetChanged();
    }
}