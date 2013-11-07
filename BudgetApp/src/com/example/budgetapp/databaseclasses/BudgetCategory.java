package com.example.budgetapp.databaseclasses;
import java.sql.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BudgetCategory {

	private int id;
	private String title;
	private String description;
	
	
	static final String KEY_ROWID = "_id";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String TAG = "BudgetCategory";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "budget_category";
    static final int DATABASE_VERSION = 1;
    
    static final String DATABASE_CREATE =
            "create table BudgetCategory (_id integer primary key autoincrement, "
            + "title text not null, description text not null);";


    SQLiteDatabase db;

    //---insert a contact into the database---
    
    public long insertBudgetCategory(String title,String description) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_DESC, description);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteBudgetCategory(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllBudgets()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
        		KEY_DESC}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getBudgetCategory(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                		KEY_TITLE, KEY_DESC}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateBudgetCategory(long rowId, String title, String description) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_DESC, description);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
}
