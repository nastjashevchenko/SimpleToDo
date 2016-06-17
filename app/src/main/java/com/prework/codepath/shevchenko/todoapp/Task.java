package com.prework.codepath.shevchenko.todoapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class for tasks in todo list
 */
@Table(name = "Tasks")
public class Task extends Model implements Parcelable, Comparable<Task> {
    private static final int DEFAULT_PRIORITY = 1;
    static int sorting = 0;

    @Column(name = "Description")
    private String description;

    @Column(name = "Priority")
    private int priority;

    @Column(name = "DueDate")
    private long dueDate;

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

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public String getDateStr() {
        return Task.getDateStr(this.dueDate);
    }

    public static String getDateStr(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        return dateFormat.format(new Date(date));
    }

    private int compareDates(long d1, long d2) {
        if (d1 == 0L) {
            return 1;
        }
        else if (d2 == 0L) {
            return -1;
        } else {
            return (int) (d1 - d2);
        }
    }

    @Override
    public int compareTo(Task task) {
        switch (sorting) {
            case 0:
                return (task.priority - priority);
            case 1:
                return compareDates(dueDate, task.dueDate);
            case 2:
                int priorityDiff = task.priority - priority;
                if (priorityDiff != 0) {
                    return priorityDiff;
                } else {
                    return compareDates(dueDate, task.dueDate);
                }
            default:
                return (task.priority - priority);
        }
    }

    public static List<Task> getAll() {
        return new Select()
                .from(Task.class)
                .execute();
    }

    public void copyFields(Task task) {
        this.description = task.description;
        this.priority = task.priority;
        this.dueDate = task.dueDate;
    }

    // Methods to make Task object Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeLong(dueDate);
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
        this.dueDate = in.readLong();
    }
}