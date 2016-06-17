package com.prework.codepath.shevchenko.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.activeandroid.Model;

import java.util.Calendar;

public class AddTaskDialog extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TASK_ID = "id";
    private static final String TASK_POSITION = "position";

    EditText dueDate;
    private long date = 0L;

    public static AddTaskDialog newInstance(Long taskId, int position) {
        AddTaskDialog dialog = new AddTaskDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(TASK_ID, taskId);
        bundle.putInt(TASK_POSITION, position);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        showDatePickerDialog(v);
    }

    public interface AddTaskDialogListener {
        void onFinishDialog(boolean add, int position, Task task);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Task task;
        final boolean add = (getArguments() == null);
        final int position;
        if (add) {
            task = new Task();
            position = -1;
        } else {
            Long taskId = getArguments().getLong(TASK_ID);
            position = getArguments().getInt(TASK_POSITION);
            task = Model.load(Task.class, taskId);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        builder.setView(dialogView);
        builder.setTitle(add ? "Add task" : "Edit Task");

        final Spinner priority = (Spinner) dialogView.findViewById(R.id.priority);
        final EditText description = (EditText) dialogView.findViewById(R.id.description);
        dueDate = (EditText) dialogView.findViewById(R.id.date);
        dueDate.setOnClickListener(this);

        if (!add) {
            priority.setSelection(task.getPriority());
            description.setText(task.getDescription());
            date = task.getDueDate();
            if (date != 0L) dueDate.setText(task.getDateStr());
        }

        builder.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        task.setPriority(priority.getSelectedItemPosition());
                        task.setDescription(description.getText().toString());
                        task.setDueDate(date);
                        task.save();
                        AddTaskDialogListener listener = (AddTaskDialogListener) getActivity();
                        listener.onFinishDialog(add, position, task);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        return builder.create();
    }

    public void showDatePickerDialog(View view) {
        final Calendar c = Calendar.getInstance();
        if (date != 0L) c.setTimeInMillis(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(view.getContext(), this, year, month, day);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        date = c.getTimeInMillis();
        dueDate.setText(Task.getDateStr(date));
    }
}
