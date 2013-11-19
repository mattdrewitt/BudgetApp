package com.example.budgetapp.databaseclasses;

import java.math.BigDecimal;

public class PurchaseItem {
	private int id;
	private BigDecimal cost_per; 
	private int qty_purchased;
	private int item_id;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_COST_PER = "cost_per";
    static final String KEY_QTY_PURCHASED = "qty_purchase";
    static final String KEY_ITEM_ID = "item_id";
    static final String TAG = "PurchaseItem";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "purchase_item";
    static final int DATABASE_VERSION = 1;
    
    static final String DATABASE_CREATE =
            "create table purchase_item (_id integer primary key autoincrement, "
            + "cost_per integer not null, "
            + "qty_purchase integer not null, "
            + "item_id integer, "
            + "FOREIGN KEY(item_id) REFERENCES item(_id));";
}
