package ru.pavlenty.roomexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import ru.pavlenty.roomexample.room.Task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import static ru.pavlenty.roomexample.DBClient.*;

public class MainActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton buttonAddTask;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAddTask = findViewById(R.id.floating_button_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        getInstance(getApplicationContext())
                .getAppDatabase()
                .taskDao()
                .getAll()
                // подписываемся на изменения и получаем данные
                .observeOn(AndroidSchedulers.mainThread()) // данные приходят в основной поток
                .subscribe(new Consumer<List<Task>>() {
                    @Override
                    public void accept(List<Task> tasks) throws Exception {
                        TaskAdapter adapter = new TaskAdapter(MainActivity.this, tasks);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}
