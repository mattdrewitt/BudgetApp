package com.example.budgetapp.databaseclasses;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.MainActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

public class InventoryItem {
	private int id; 
	private int item_id;
	private int qoh; 
	private int percent_remaining;
	private boolean new_item;
	private List<InventoryItem> itemsList;
	private DBAdapter db;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_ITEM_ID = "item_id";
    static final String KEY_QOH = "qoh";
    static final String KEY_PERCENT_REMAINING = "percent_remaining";
    static final String TAG = "InventoryItem";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "inventory_item";
    static final int DATABASE_VERSION = 1;
    
    
    static final String DATABASE_CREATE =
            "create table inventory_item (_id integer primary key autoincrement, "
            + "item_id integer not null, "
            + "qoh integer not null, "
            + "percent_remaining integer not null, "
            + "FOREIGN KEY(item_id) REFERENCES item(_id));";
    
    // Constructors
    public InventoryItem(DBAdapter adapter) {
    	this.db = adapter;
    	this.new_item = true;
    }
    
    public InventoryItem(int id, int item_id, int qoh, int percent_remaining) {
    	this.id = id;
		this.item_id = item_id;
		this.qoh = qoh;
		this.percent_remaining = percent_remaining;
		this.new_item = false;
	}
    

	// Getters and Setters
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getItem_id() {
		return item_id;
	}


	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}


	public int getQoh() {
		return qoh;
	}


	public void setQoh(int qoh) {
		this.qoh = qoh;
	}


	public int getPercent_remaining() {
		return percent_remaining;
	}


	public void setPercent_remaining(int percent_remaining) {
		this.percent_remaining = percent_remaining;
	}


	public boolean isNew_item() {
		return new_item;
	}


	public void setNew_item(boolean new_item) {
		this.new_item = new_item;
	}
	
	public List<InventoryItem> getItemsList() {
		return itemsList;
	}
    
	//---find an item by upc, or set the item to a new item with this upc
    public boolean getInventoryByItemId(int item_id) throws SQLException 
    {
    	db.open();
        Cursor mCursor =
                db.exec.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_ITEM_ID, 
					KEY_QOH, KEY_PERCENT_REMAINING}, KEY_ITEM_ID + "='" + item_id + "'", null,
                null, null, null, null);
        
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            
            id = mCursor.getInt(0);
            item_id = mCursor.getInt(1);
            qoh = mCursor.getInt(2);
            percent_remaining = mCursor.getInt(3);
            
            new_item = false;
        } else {
        	new_item = true;
        }
        db.close();
        
        return true;
    }
    
    //---inserts or updates the item, depending on if it is a new record or not---
	public boolean saveItem() {
    	if(new_item == true) {
    		// Insert new record
    		ContentValues initialValues = new ContentValues();
    		initialValues.put(KEY_ITEM_ID, item_id);
    		initialValues.put(KEY_QOH, qoh);
    		initialValues.put(KEY_PERCENT_REMAINING, percent_remaining);
	        
	        db.open();
	        db.exec.insert(DATABASE_TABLE, null, initialValues);
	        db.close();
	        new_item = false;
	        
	        return true;
    	} else {
    		// Update exisSting record
    		ContentValues args = new ContentValues();
    		args.put(KEY_ITEM_ID, item_id);
    		args.put(KEY_QOH, qoh);
    		args.put(KEY_PERCENT_REMAINING, percent_remaining);
    		
	        db.open();
	        db.exec.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);
	        db.close();
	        
            return true;
    	}
    }
    
    //---retrieves all the items---
    public boolean getAllInventory()
    {
    	db.open();
        Cursor mCursor = db.exec.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_ITEM_ID, 
        		KEY_QOH, KEY_PERCENT_REMAINING}, null, null, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0) {
        	mCursor.moveToFirst();
	        itemsList = new ArrayList<InventoryItem>();
	        
			do {
				InventoryItem i = new InventoryItem(mCursor.getInt(0), mCursor.getInt(1), mCursor.getInt(2), mCursor.getInt(3));
				itemsList.add(i);				
			} while(mCursor.moveToNext());
        } else {
        	db.close();
        	
        	return false;
        }
        db.close();
        return true;
    }
    
    public Item getItem() {
    	Item item;
    	db.open();
        Cursor mCursor =
                db.exec.query(true, Item.DATABASE_TABLE, new String[] {Item.KEY_ROWID, Item.KEY_UPC,
                Item.KEY_NAME, Item.KEY_QTY_DESIRED, Item.KEY_REFILL_POINT, Item.KEY_PURCHASE_OCCURANCE, Item.KEY_REGULAR_PURCHASE,
                Item.KEY_SERVICE, Item.KEY_CATEGORY_ID}, Item.KEY_ROWID + "='" + this.item_id + "'", null,
                null, null, null, null);
        
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            
            item = new Item(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2), mCursor.getInt(3), 
					mCursor.getInt(4), mCursor.getString(5), mCursor.getInt(6) == 1 ? true : false, 
					mCursor.getInt(7) == 1 ? true : false, mCursor.getInt(8));
        } else {
        	item = new Item(MainActivity.db);
        }
        
        db.close();
        
        return item;
    }
}
