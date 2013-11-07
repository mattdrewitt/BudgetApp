package com.example.budgetapp.databaseclasses;

public class InventoryItem {
	private int id; 
	private int item_id;
	private int qoh; 
	private int percent_remaining;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_ITEM_ID = "item_id";
    static final String KEY_QOH = "qoh";
    static final String KEY_PERCENT_REMAINING = "percent_remaining";
    static final String TAG = "InventoryItem";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "inventory_item";
    static final int DATABASE_VERSION = 1;
    
    
    static final String DATABASE_CREATE =
            "create table InventoryItem (_id integer primary key autoincrement, "
            + "item_id integer not null, "
            + "qoh integer not null, "
            + "percentage_remaining integer, "
            + "FOREIGN KEY item_id REFERENCES Item _id);";
}
