package com.prework.codepath.shevchenko.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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
        itemsAdapter.add(task);
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        tasks.get(pos).delete();
                        tasks.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int pos, long id) {
                        //@TODO Make Task object Parcelable for putting it to extras
                        // instead of putting different fields and returning them back
                        Intent editItem = new Intent(view.getContext(), EditItemActivity.class);
                        editItem.putExtra("itemText", tasks.get(pos).getDescription())
                                .putExtra("priority", tasks.get(pos).getPriority())
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
            editedTask.setDescription(data.getStringExtra("itemText"));
            editedTask.setPriority(data.getIntExtra("priority", 1));
            editedTask.save();
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
