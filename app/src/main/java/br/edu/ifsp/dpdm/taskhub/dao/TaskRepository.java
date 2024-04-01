package br.edu.ifsp.dpdm.taskhub.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskRepository<T extends Object> extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "sgcp.sqlite3";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_TASK = "task";

    protected Context context;
    protected String[] fields;
    protected String tableName;

    public static final String CREATE_TABLE_TASK = "CREATE TABLE task ("
            + " id integer primary key autoincrement NOT NULL,"
            + " description varchar(200),"
            + " title varchar(75),"
            + " deadline varchar(75),"
            + " completed int);";

    public TaskRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_TASK);
    }

    protected void closeDatabase(SQLiteDatabase db) {
        if(db.isOpen())
            db.close();
    }
}
