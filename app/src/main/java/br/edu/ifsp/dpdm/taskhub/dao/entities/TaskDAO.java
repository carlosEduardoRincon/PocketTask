package br.edu.ifsp.dpdm.taskhub.dao.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.edu.ifsp.dpdm.taskhub.dao.TaskRepository;
import br.edu.ifsp.dpdm.taskhub.model.Task;

public class TaskDAO extends TaskRepository<Task> {

    private SQLiteDatabase database;

    public TaskDAO(Context context) {
        super(context);
        fields = new String[]{"id", "title", "description", "deadline", "priority", "completed"};
        tableName = "task";
        this.database = getWritableDatabase();
    }

    public Task getByNome(String title) {
        Task task = null;

        Cursor cursor = executeSelect("title = ?", new String[]{title}, null);
        if (cursor!=null && cursor.moveToFirst()) {
            task = serializeByCursor(cursor);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return task;
    }

    public Task getByID(Integer id) {
        Task task = null;

        Cursor cursor = executeSelect("id = ?", new String[]{String.valueOf(id)}, null);
        if (cursor!=null && cursor.moveToFirst()) {
            task = serializeByCursor(cursor);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return task;
    }

    public List<Task> listAll() {
        List<Task> list = new ArrayList<Task>();
        Cursor cursor =  executeSelect(null, null, "1");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(serializeByCursor(cursor));
            } while (cursor.moveToNext());
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return list;
    }

    public boolean salvar(Task task) {
        ContentValues values = serializeContentValues(task);
        return database.insert(tableName, null, values) > 0;
    }

    public boolean deletar(Integer id) {
        return database.delete(tableName, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean atualizar(Task task) {
        ContentValues values = serializeContentValues(task);
        return database.update(tableName,
                values,
                "id = ? ",
                new String[]{String.valueOf(task.getId())}) > 0;
    }


    private Task serializeByCursor(Cursor cursor) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Task task = new Task();

        try {
            Log.v("TaskDAO", "cursor: " + cursor);
            task.setId(cursor.getInt(0));
            task.setTitle(cursor.getString(1));
            task.setDescription(cursor.getString(2));

            Date originalDate = inputFormat.parse(cursor.getString(3));
            String formattedDateString = outputFormat.format(originalDate);
            Date formattedDate = outputFormat.parse(formattedDateString);
            task.setDeadline(formattedDate);

            task.setPriority(cursor.getString(4));
            task.setCompleted(cursor.getInt(5) == 1);
        } catch (Exception ignored) {}

        return task;
    }

    private ContentValues serializeContentValues(Task task)
    {
        ContentValues values = new ContentValues();
        values.put("id", task.getId());
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("deadline", String.valueOf(task.getDeadline()));
        values.put("priority", task.getPriority());
        values.put("completed", task.isCompleted()? 1 : 0);

        return values;
    }

    private Cursor  executeSelect(String selection, String[] selectionArgs, String orderBy) {
        return database.query(tableName,
                fields,
                selection,
                selectionArgs,
                null,
                null,
                orderBy);
    }
}
