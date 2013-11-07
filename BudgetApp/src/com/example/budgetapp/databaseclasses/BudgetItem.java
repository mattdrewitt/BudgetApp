package com.example.budgetapp.databaseclasses;

import java.math.BigDecimal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BudgetItem {
	private int id;
	private int category_id;
	private int budget_id;
	private BigDecimal target_total;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_CATEGORY_ID = "category_id";
    static final String KEY_TARGET_TOTAL = "target_total";
    static final String TAG = "BudgetItem";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "budget_item";
    static final int DATABASE_VERSION = 1;
    

    static final String DATABASE_CREATE =
        "create table BudgetItem (_id integer primary key autoincrement, "
        + "category_id integer not null, "
        + "budget_id integer not null, "
        + "target_total integer, "
        + "FOREIGN KEY category_id REFERENCES BudgetCategory _id,"
        + "FOREIGN KEY budget_id REFERENCES Budget _id);";
    
    
    SQLiteDatabase db;
    
    public long insertBudgetItem(int catId, BigDecimal targetTotal) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CATEGORY_ID, catId);
        initialValues.put(KEY_TARGET_TOTAL, String.valueOf(targetTotal));
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteBudgetItem(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllBudgetItemss()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_CATEGORY_ID,
        		KEY_TARGET_TOTAL}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getBudgetItem(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                		KEY_CATEGORY_ID, KEY_TARGET_TOTAL}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateBudgetItem(long rowId, int catId, BigDecimal targetTotal) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_CATEGORY_ID, catId);
        args.put(KEY_TARGET_TOTAL, String.valueOf(targetTotal));
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
