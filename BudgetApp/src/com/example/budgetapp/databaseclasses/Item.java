package com.example.budgetapp.databaseclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Item {
	private int id;
	private String upc;
	private String name;
	private int qty_desired;
	private int refill_point;
	private String purchase_occurance; 
	private boolean regular_purchase;
	private boolean service_non_inventory; 
	private int category_id;
	private boolean new_item;

	
	private DBAdapter db;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_UPC = "upc";
    static final String KEY_NAME = "name";
    static final String KEY_QTY_DESIRED = "qty_desired";
    static final String KEY_REFILL_POINT = "refill_point";
    static final String KEY_PURCHASE_OCCURANCE = "purchase_occurance";
    static final String KEY_REGULAR_PURCHASE = "regular_purchase";
    static final String KEY_SERVICE = "service_non_inventory";
    static final String KEY_CATEGORY_ID = "category_id";
    static final String TAG = "Item";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "item";
    static final int DATABASE_VERSION = 1;
    
    static final String DATABASE_CREATE =
            "create table item (_id integer primary key autoincrement, "
            + "upc text, "
            + "name text not null, "
            + "qty_desired integer, "
            + "refill_point integer, "
            + "purchase_occurance String, "
            + "regular_purchase integer, "
            + "service_non_inventory integer, "
            + "category_id integer not null, "
            + "FOREIGN KEY(category_id) REFERENCES budget_category(_id));";
    
    // Constructor
    public Item(DBAdapter adapter) {
    	this.db = adapter;
    	this.new_item = true;
    }
    
    // Getters/Setters
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQty_desired() {
		return qty_desired;
	}

	public void setQty_desired(int qty_desired) {
		this.qty_desired = qty_desired;
	}

	public int getRefill_point() {
		return refill_point;
	}

	public void setRefill_point(int refill_point) {
		this.refill_point = refill_point;
	}

	public String getPurchase_occurance() {
		return purchase_occurance;
	}

	public void setPurchase_occurance(String purchase_occurance) {
		this.purchase_occurance = purchase_occurance;
	}

	public boolean isRegular_purchase() {
		return regular_purchase;
	}

	public void setRegular_purchase(boolean regular_purchase) {
		this.regular_purchase = regular_purchase;
	}

	public boolean isService_non_inventory() {
		return service_non_inventory;
	}

	public void setService_non_inventory(boolean service_non_inventory) {
		this.service_non_inventory = service_non_inventory;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public boolean isNew_item() {
		return new_item;
	}

	public void setNew_item(boolean new_item) {
		this.new_item = new_item;
	}
    
    
	//---find an item by upc, or set the item to a new item with this upc
    public boolean getItemByUpc(String find_upc) throws SQLException 
    {
    	db.open();
        Cursor mCursor =
                db.exec.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_UPC,
                KEY_NAME, KEY_QTY_DESIRED, KEY_REFILL_POINT, KEY_PURCHASE_OCCURANCE, KEY_REGULAR_PURCHASE,
                KEY_SERVICE, KEY_CATEGORY_ID}, KEY_UPC + "='" + find_upc + "'", null,
                null, null, null, null);
        
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            
            id = mCursor.getInt(0);
            upc = mCursor.getString(1);
            name = mCursor.getString(2);
            qty_desired = mCursor.getInt(3);
            refill_point = mCursor.getInt(4);
            purchase_occurance = mCursor.getString(5);
            regular_purchase = mCursor.getInt(6) == 1 ? true : false;
            service_non_inventory = mCursor.getInt(7) == 1 ? true : false;
            category_id = mCursor.getInt(8);
            new_item = false;
        } else {
        	upc = find_upc;
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
    		initialValues.put(KEY_UPC, upc);
	        initialValues.put(KEY_NAME, name);
	        initialValues.put(KEY_QTY_DESIRED, qty_desired);
	        initialValues.put(KEY_REFILL_POINT, refill_point);
	        initialValues.put(KEY_PURCHASE_OCCURANCE, purchase_occurance);
	        initialValues.put(KEY_REGULAR_PURCHASE, regular_purchase);
	        initialValues.put(KEY_SERVICE, service_non_inventory);
	        initialValues.put(KEY_CATEGORY_ID, category_id);
	        
	        db.open();
	        db.exec.insert(DATABASE_TABLE, null, initialValues);
	        db.close();
	        new_item = false;
	        
	        return true;
    	} else {
    		// Update exisiting record
    		ContentValues args = new ContentValues();
    		args.put(KEY_UPC, upc);
    		args.put(KEY_NAME, name);
    		args.put(KEY_QTY_DESIRED, qty_desired);
    		args.put(KEY_REFILL_POINT, refill_point);
    		args.put(KEY_PURCHASE_OCCURANCE, purchase_occurance);
	        args.put(KEY_REGULAR_PURCHASE, regular_purchase);
	        args.put(KEY_SERVICE, service_non_inventory);
	        args.put(KEY_CATEGORY_ID, category_id);
            
	        db.open();
	        db.exec.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);
	        db.close();
	        
            return true;
    	}
    }
    
    //---retrieves all the items---
    public Cursor getAllItems()
    {
    	db.open();
        Cursor c = db.exec.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_UPC,
                KEY_NAME, KEY_QTY_DESIRED, KEY_REFILL_POINT, KEY_PURCHASE_OCCURANCE, KEY_REGULAR_PURCHASE,
                KEY_SERVICE, KEY_CATEGORY_ID}, null, null, null, null, null);
        db.close();
        return c;
    }
}
