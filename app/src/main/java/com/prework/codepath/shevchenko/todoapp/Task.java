package com.prework.codepath.shevchenko.todoapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Class for tasks in todo list
 */
@Table(name = "Tasks")
public class Task extends Model implements Parcelable {
    private static final int DEFAULT_PRIORITY = 1;
    @Column(name = "Description")
    private String description;

    @Column(name = "Priority")
    private int priority;

    public Task() {
        super();
    }

    public Task(String description) {
        super();
        this.description = description;
        this.priority = DEFAULT_PRIORITY;
    }

    public Task(String description, int priority) {
        super();
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public static List<Task> getAll() {
        return new Select()
                .from(Task.class)
                .execute();
    }

    public void copyFields(Task task) {
        this.description = task.description;
        this.priority = task.priority;
    }

    // Methods to make Task object Parceable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeInt(priority);
    }

    public static final Parcelable.Creator<Task> CREATOR
            = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[0];
        }
    };

    private Task(Parcel in) {
        this.description = in.readString();
        this.priority = in.readInt();
    }
}