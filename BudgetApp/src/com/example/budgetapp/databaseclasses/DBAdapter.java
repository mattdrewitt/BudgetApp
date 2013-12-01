package com.example.budgetapp.databaseclasses;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "BudgetAppDB";
    static final int DATABASE_VERSION = 1;

    final Context context;

    DatabaseHelper DBHelper;
    public SQLiteDatabase exec;
    
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
        
        @Override
        public void onOpen(SQLiteDatabase db) {
        	super.onOpen(db);
        	if(!db.isReadOnly()) {
        		//Enable foreign keys
        		db.execSQL("PRAGMA foreign_keys=ON;");
        	}
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
    	exec = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() 
    {
        DBHelper.close();
    }

   
}
