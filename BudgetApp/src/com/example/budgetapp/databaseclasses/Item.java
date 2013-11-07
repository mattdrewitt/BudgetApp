package com.example.budgetapp.databaseclasses;

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
            "create table BudgetItem (_id integer primary key autoincrement, "
            + "upc text, "
            + "name text not null, "
            + "qty_desired integer, "
            + "refill_point integer, "
            + "purchase_occurance String, "
            + "regular_purchase integer, "
            + "service_non_inventory integer, "
            + "category_id integer not null, "
            + "FOREIGN KEY category_id REFERENCES BudgetCategory _id);";
}
