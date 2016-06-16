package com.prework.codepath.shevchenko.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context mContext;

    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        mContext = context;
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
        LinearLayout item = (LinearLayout) convertView.findViewById(R.id.item);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView dueDate = (TextView) convertView.findViewById(R.id.date);

        description.setText(task.getDescription());
        if (task.getDueDate() != 0L) dueDate.setText(getContext().getResources()
                .getString(R.string.due, task.getDateStr()));

        int color;
        switch(task.getPriority()) {
            case 0 : color = R.color.green;
                     break;
            case 1 : color = R.color.yellow;
                     break;
            case 2 : color = R.color.red;
                     break;
            default: color = Color.TRANSPARENT;
                     break;
        }
        item.setBackgroundColor(ContextCompat.getColor(mContext, color));
        // Return the completed view to render on screen
        return convertView;
    }
}
