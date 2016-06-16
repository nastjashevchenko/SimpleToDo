package com.prework.codepath.shevchenko.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.AddTaskDialogListener {
    ListView lvItems;
    TaskAdapter itemsAdapter;
    List<Task> tasks;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void saveToPref(int value) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Sorting", value);
        editor.apply();
    }

    public int getSorting() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt("Sorting", 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sorting) {
            final AlertDialog sorting;
            final int initSorting = getSorting();
            final CharSequence[] items = {" Priority "," Due Date "," Priority + Due Date "};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Set sorting type")
                    .setSingleChoiceItems(items, getSorting(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            saveToPref(item);
                        }
                    })
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resort();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveToPref(initSorting);
                        }
                    });
            sorting = builder.create();
            sorting.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = Task.getAll();
        Collections.sort(tasks);
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new TaskAdapter(this, tasks);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        Task.sorting = getSorting();
    }

    public void onAddItem(View view) {
        DialogFragment dialog = new AddTaskDialog();
        dialog.show(getSupportFragmentManager(), "AddTaskDialog");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, final int pos, long id) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(item.getContext());
                        alert.setMessage(tasks.get(pos).getDescription())
                                .setTitle(R.string.delete_title)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        tasks.get(pos).delete();
                                        tasks.remove(pos);
                                        itemsAdapter.notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), R.string.was_deleted,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alert.create().show();
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        DialogFragment dialog = AddTaskDialog.newInstance(tasks.get(pos).getId(), pos);
                        dialog.show(getSupportFragmentManager(), "AddTaskDialog");
                    }
                }
        );
    }

    public void resort() {
        Task.sorting = getSorting();
        Collections.sort(tasks);
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishDialog(boolean add, int position, Task task) {
        if (add) {
            tasks.add(task);
        } else {
            Task editedTask = tasks.get(position);
            editedTask.copyFields(task);
            editedTask.save();
        }
        // Re-sort after adding new task by priority
        resort();
    }
}
