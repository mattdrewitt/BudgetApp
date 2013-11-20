package com.example.budgetapp.databaseclasses;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;


public class Budget {

	private int id;
	private Date start_date;
	private Date end_date;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_START_DATE = "start_date";
    static final String KEY_END_DATE = "end_date";
    static final String TAG = "Budget";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "budget";
    static final int DATABASE_VERSION = 1;
    
    
    private String formatDate(Date d){
    	TimeZone tz = TimeZone.getTimeZone("UTC");
    	SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");
    	df.setTimeZone(tz);
    	String nowAsISO = df.format(d);
    	return nowAsISO;
    }
    
    static final String DATABASE_CREATE =
            "create table budget (_id integer primary key autoincrement, "
            + "start_date text, end_date text);";

    //final Context context;

   SQLiteDatabase db;

   //---insert a contact into the database---
   
   public long insertBudget(Date startDate, Date endDate) 
   {
       ContentValues initialValues = new ContentValues();
       initialValues.put(KEY_START_DATE, formatDate(startDate));
       initialValues.put(KEY_END_DATE, formatDate(endDate));
       return db.insert(DATABASE_TABLE, null, initialValues);
   }

   //---deletes a particular contact---
   public boolean deleteBudget(long rowId) 
   {
       return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
   }

   //---retrieves all the contacts---
   public Cursor getAllBudgets()
   {
       return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_START_DATE,
               KEY_END_DATE}, null, null, null, null, null);
   }

   //---retrieves a particular contact---
   public Cursor getBudget(long rowId) throws SQLException 
   {
       Cursor mCursor =
               db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
               KEY_START_DATE, KEY_END_DATE}, KEY_ROWID + "=" + rowId, null,
               null, null, null, null);
       if (mCursor != null) {
           mCursor.moveToFirst();
       }
       return mCursor;
   }

   //---updates a contact---
   public boolean updateBudget(long rowId, Date startDate, Date endDate) 
   {
       ContentValues args = new ContentValues();
       args.put(KEY_START_DATE, formatDate(startDate));
       args.put(KEY_END_DATE, formatDate(endDate));
       return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
   }

   
   
   
   
}
