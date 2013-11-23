package com.example.budgetapp.databaseclasses;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.MainActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

public class BudgetItem {
	private int id;
	private int category_id;
	private int budget_id;
	private int target_total;
	private boolean new_item;
	private List<BudgetItem> itemsList;
	private DBAdapter db;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_CATEGORY_ID = "category_id";
    static final String KEY_BUDGET_ID = "budget_id";
    static final String KEY_TARGET_TOTAL = "target_total";
    static final String TAG = "BudgetItem";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "budget_item";
    static final int DATABASE_VERSION = 1;
    

    static final String DATABASE_CREATE =
        "create table budget_item (_id integer primary key autoincrement, "
        + "category_id integer not null, "
        + "budget_id integer not null, "
        + "target_total real, "
        + "FOREIGN KEY(category_id) REFERENCES budget_category(_id),"
        + "FOREIGN KEY(budget_id) REFERENCES budget(_id));";
    
    // Constructors
    public BudgetItem(DBAdapter adapter) {
    	this.db = adapter;
    	this.budget_id = MainActivity.dbBudget.getId(); // We are always on current budget for new
    	itemsList = new ArrayList<BudgetItem>();
    	this.new_item = true;
    }
        
    public BudgetItem(int id, int category_id, int budget_id, int target_total) {
		this.id = id;
		this.category_id = category_id;
		this.budget_id = budget_id;
		this.target_total = target_total;
	}

    // Getters/Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getBudget_id() {
		return budget_id;
	}

	public void setBudget_id(int budget_id) {
		this.budget_id = budget_id;
	}

	public int getTarget_total() {
		return target_total;
	}

	public void setTarget_total(int target_total) {
		this.target_total = target_total;
	}

	public List<BudgetItem> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<BudgetItem> itemsList) {
		this.itemsList = itemsList;
	}

	public boolean isNew_item() {
		return new_item;
	}

	public boolean saveItem()
    {
    	if(new_item == true) {
    		// Insert new record
    		ContentValues initialValues = new ContentValues();
    		initialValues.put(KEY_CATEGORY_ID, category_id);
	        initialValues.put(KEY_BUDGET_ID, budget_id);
	        initialValues.put(KEY_TARGET_TOTAL, target_total);
	        
	        db.open();
	        id = (int)db.exec.insert(DATABASE_TABLE, null, initialValues);
	        db.close();
	        new_item = false;
	        
	        return true;
    	} else {
    		// Update exisSting record
    		ContentValues args = new ContentValues();
    		args.put(KEY_CATEGORY_ID, category_id);
    		args.put(KEY_BUDGET_ID, budget_id);
    		args.put(KEY_TARGET_TOTAL, target_total);
            
	        db.open();
	        db.exec.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);
	        db.close();
	        
            return true;
    	}
    }
	
	//---find an item by upc, or set the item to a new item with this upc
    public boolean getItem(int rowId) throws SQLException 
    {
    	db.open();
        Cursor mCursor =
                db.exec.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_CATEGORY_ID, KEY_BUDGET_ID,
                		KEY_TARGET_TOTAL}, KEY_ROWID + "='" + rowId + "'", null, null, null, null, null);
        
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            
            id = mCursor.getInt(0);
            category_id = mCursor.getInt(1);
            budget_id = mCursor.getInt(2);
            target_total = mCursor.getInt(3);
            new_item = false;
        } else {
        	new_item = true;
        }
        db.close();
        
        return true;
    }

    //---deletes a particular contact---
    public boolean deleteItem(int rowId) 
    {
    	db.open();
        boolean result = db.exec.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
        db.close();
        return result;
    }

    //---retrieves all the contacts---
    public boolean getAllItemsByBudget(int budget)
    {
    	db.open();
        Cursor mCursor = db.exec.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_CATEGORY_ID, KEY_BUDGET_ID,
        		KEY_TARGET_TOTAL}, KEY_BUDGET_ID + "=" + budget, null, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0) {
        	mCursor.moveToFirst();
	        itemsList = new ArrayList<BudgetItem>();
	        
			do {
				BudgetItem b = new BudgetItem(mCursor.getInt(0), mCursor.getInt(1), mCursor.getInt(2), mCursor.getInt(3));
				itemsList.add(b);				
			} while(mCursor.moveToNext());
        } else {
        	return false;
        }
        db.close();
        return true;
    }
}
