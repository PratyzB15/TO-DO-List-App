package com.example.to_dolistapp;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolistapp.Adapter.ToDoAdapter;
import com.example.to_dolistapp.Model.ToDoModel;
import com.example.to_dolistapp.Util.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener{

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    DatabaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycleView);
        addButton = findViewById(R.id.addButton);
        myDB = new DatabaseHelper(MainActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        mList= myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface){
        mList= myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }
}