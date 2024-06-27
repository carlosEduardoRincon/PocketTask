package br.edu.ifsp.dpdm.taskhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Task implements Parcelable {
    private Integer id;
    private String title;
    private String description;
    private Date deadline;
    private String priority;
    private boolean completed;

    public Task() {}

    public Task(String title, String description, Date deadline, String priority, boolean completed) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.completed = completed;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        priority = in.readString();
        completed = in.readByte() != 0;
        long tmpDate = in.readLong();
        this.deadline = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(priority);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeLong(deadline != null ? deadline.getTime() : -1);
    }
}
