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
	private boolean new_cat;
	
	private DBAdapter db;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String TAG = "BudgetCategory";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "budget_category";
    static final int DATABASE_VERSION = 1;
    
    static final String DATABASE_CREATE =
            "create table budget_category (_id integer primary key autoincrement, "
            + "title text not null, "
            + "description text not null);";


    public BudgetCategory(DBAdapter adapter) {
    	this.db = adapter;
    	this.new_cat = true;
    }
    
    // Getters/Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
    

    //---insert a contact into the database---
    
    public boolean saveCategory() 
    {
    	if(new_cat == true) {
    		// Insert new record
    		ContentValues initialValues = new ContentValues();
    		initialValues.put(KEY_TITLE, title);
    	    initialValues.put(KEY_DESC, description);
	        
	        db.open();
	        db.exec.insert(DATABASE_TABLE, null, initialValues);
	        db.close();
	        new_cat = false;
	        
	        return true;
    	} else {
    		// Update existing record
    		ContentValues args = new ContentValues();
    		args.put(KEY_TITLE, title);
    		args.put(KEY_DESC, description);
            
	        db.open();
	        db.exec.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);
	        db.close();
	        
            return true;
    	}
    }

    //---retrieves all the contacts---
    public Cursor getAllCategories()
    {
    	db.open();
        Cursor c = db.exec.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
        		KEY_DESC}, null, null, null, null, null);
        db.close();
        
        return c;
    }

    //---retrieves a particular contact---
    public boolean getBudgetCategory(long rowId) throws SQLException 
    {
    	db.open();
        Cursor mCursor =
                db.exec.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                		KEY_TITLE, KEY_DESC}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            id = mCursor.getInt(0);
            title = mCursor.getString(1);
            description = mCursor.getString(2);
            
            new_cat = false;
        } else {
        	new_cat = true;
        	
        	db.close();
        	return false;
        }
        db.close();
        
        return true;
    }    
}
