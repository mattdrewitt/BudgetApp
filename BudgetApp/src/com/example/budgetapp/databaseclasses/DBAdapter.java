package com.example.budgetapp.databaseclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	/*
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_EMAIL = "email";
    static final String KEY_AGE = "age";
    static final String KEY_YEAR = "year";
    */
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final int DATABASE_VERSION = 1;

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(BudgetCategory.DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL(Budget.DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL(BudgetItem.DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL(Item.DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL(PurchaseItem.DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL(InventoryItem.DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() 
    {
        DBHelper.close();
    }

   
}
