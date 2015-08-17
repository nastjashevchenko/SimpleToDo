package com.prework.codepath.shevchenko.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }
        // Lookup view for data population
        TextView description = (TextView) convertView.findViewById(R.id.description);
        View priority = convertView.findViewById(R.id.priority);

        description.setText(task.getDescription());
        int color;
        switch(task.getPriority()) {
            case 0 : color = Color.GRAY;
                     break;
            case 1 : color = Color.YELLOW;
                     break;
            case 2 : color = Color.RED;
                     break;
            default: color = Color.TRANSPARENT;
                     break;
        }
        priority.setBackgroundColor(color);
        // Return the completed view to render on screen
        return convertView;
    }
}
