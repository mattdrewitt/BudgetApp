package com.example.budgetapp.databaseclasses;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Pair;


public class Budget {

	private int id;
	private Date start_date;
	private Date end_date;
	private DBAdapter db;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_START_DATE = "start_date";
    static final String KEY_END_DATE = "end_date";
    static final String TAG = "Budget";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "budget";
    static final int DATABASE_VERSION = 1;
    
    // Constructors
    public Budget(DBAdapter adapter) {
    	this.db = adapter;
    }
    
    // Getters/Setters
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
    
    
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
    
   //---retrieves all the contacts---
    public Cursor getAllBudgets()
    {
    	return db.exec.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_START_DATE,
    			KEY_END_DATE}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getBudget(long rowId) throws SQLException {
    	Cursor mCursor =
    			db.exec.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
    					KEY_START_DATE, KEY_END_DATE}, KEY_ROWID + "=" + rowId, null,
    					null, null, null, null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }
    
    public boolean getCurrentBudget() throws SQLException { 	
    	Pair<String, String> currentMonth = getDateRange();
    	db.open();
    	
    	Cursor mCursor =
    			db.exec.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
    					KEY_START_DATE, KEY_END_DATE}, 
    					KEY_START_DATE + "='" + currentMonth.first + "' AND " + 
    					KEY_END_DATE + "='" + currentMonth.second + "'", 
    					null, null, null, null, null);
    	
    	if (mCursor != null && mCursor.getCount() > 0) {
    		mCursor.moveToFirst();
          
          	id = mCursor.getInt(0);
          	try {
        	  	start_date = new SimpleDateFormat("yyyy-MM-dd").parse(mCursor.getString(1));
        	  	end_date = new SimpleDateFormat("yyyy-MM-dd").parse(mCursor.getString(2));
          	} catch (Exception ex) {
        	  	System.out.println("Error while parsing dates: " + ex.getMessage());
          	}
          	
          	db.close();
    	} else {
    		db.close();
    		
    	  	initializeBudget();
      	}
    	
    	return true;
    }
    
    public boolean initializeBudget() throws SQLException {
    	db.open();
    	
    	Pair<String, String> currentMonth = getDateRange();
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_START_DATE, currentMonth.first);
        initialValues.put(KEY_END_DATE, currentMonth.second);
        id = (int)db.exec.insert(DATABASE_TABLE, null, initialValues);
    	
        db.close();
        
    	return true;
    }
    
    // Get the first and last of the month that the current budget will live in.
    public Pair<String, String> getDateRange() {
        String beginning, end;

        {
        	Calendar calendar = Calendar.getInstance();
        	
            calendar.set(Calendar.DAY_OF_MONTH, 1);   

            beginning = new SimpleDateFormat("yyy-MM-dd").format(calendar.getTime()); 
        }

        {
        	Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.MONTH, 1);  
            calendar.set(Calendar.DAY_OF_MONTH, 1);  
            calendar.add(Calendar.DATE, -1);  

            end = new SimpleDateFormat("yyy-MM-dd").format(calendar.getTime()); 
        }
        
        return Pair.create(beginning, end);
    }
}
