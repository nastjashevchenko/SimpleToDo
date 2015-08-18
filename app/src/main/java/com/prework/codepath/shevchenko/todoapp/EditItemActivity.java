package com.prework.codepath.shevchenko.todoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;


public class EditItemActivity extends ActionBarActivity {
    private EditText itemText;
    private Spinner itemPriority;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemText = (EditText) findViewById(R.id.editItemText);
        itemPriority = (Spinner) findViewById(R.id.priority);
        task = getIntent().getParcelableExtra("task");
        itemText.setText(task.getDescription());
        itemText.setSelection(task.getDescription().length());
        itemPriority.setSelection(task.getPriority());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSaveAfterEdit(View view) {
        Intent data = new Intent();
        task.setPriority(itemPriority.getSelectedItemPosition());
        task.setDescription(itemText.getText().toString());
        data.putExtra("position", getIntent().getIntExtra("position", 0))
                .putExtra("task", task);
        setResult(RESULT_OK, data);
        finish();
    }
}
