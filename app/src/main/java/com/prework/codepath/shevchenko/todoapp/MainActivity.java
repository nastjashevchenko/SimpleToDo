package com.prework.codepath.shevchenko.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    ListView lvItems;
    TaskAdapter itemsAdapter;
    List<Task> tasks;
    private static final int EDIT_ITEM_CODE = 100;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        Task task = new Task(etNewItem.getText().toString());
        task.save();
        // Re-sort after adding new task by priority
        tasks.add(task);
        Collections.sort(tasks);
        itemsAdapter.notifyDataSetChanged();
        etNewItem.setText("");
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
                        Intent editItem = new Intent(view.getContext(), EditItemActivity.class);
                        editItem.putExtra("task", tasks.get(pos))
                                .putExtra("position", pos);
                        startActivityForResult(editItem, EDIT_ITEM_CODE);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_CODE) {
            Task editedTask = tasks.get(data.getIntExtra("position", 0));
            editedTask.copyFields((Task) data.getParcelableExtra("task"));
            editedTask.save();
            Collections.sort(tasks);
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
