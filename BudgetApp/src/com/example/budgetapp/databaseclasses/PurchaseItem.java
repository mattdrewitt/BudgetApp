package com.example.budgetapp.databaseclasses;

import java.math.BigDecimal;

import com.example.budgetapp.MainActivity;

import android.content.ContentValues;

public class PurchaseItem {
	private int id;
	private double cost_per; 
	private int qty_purchased;
	private int item_id;
	private int budget_id;
	
	private DBAdapter db;
	
	static final String KEY_ROWID = "_id";
    static final String KEY_COST_PER = "cost_per";
    static final String KEY_QTY_PURCHASED = "qty_purchase";
    static final String KEY_ITEM_ID = "item_id";
    static final String KEY_BUDGET_ID = "budget_id";
    static final String TAG = "PurchaseItem";

    static final String DATABASE_NAME = "BudgetAppDB";
    static final String DATABASE_TABLE = "purchase_item";
    static final int DATABASE_VERSION = 1;
    
    static final String DATABASE_CREATE =
            "create table purchase_item (_id integer primary key autoincrement, "
            + "cost_per real not null, "
            + "qty_purchase integer not null, "
            + "budget_id integer, "
            + "item_id integer, "
            + "FOREIGN KEY(budget_id) REFERENCES budget(_id)"
            + "FOREIGN KEY(item_id) REFERENCES item(_id));";
    
    // Constructors
    public PurchaseItem(DBAdapter adapter) {
    	this.db = adapter;
    }

    
    // Getters/Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getCost_per() {
		return cost_per;
	}

	public void setCost_per(double cost_per) {
		this.cost_per = cost_per;
	}

	public int getQty_purchased() {
		return qty_purchased;
	}

	public void setQty_purchased(int qty_purchased) {
		this.qty_purchased = qty_purchased;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public int getBudget_id() {
		return budget_id;
	}

	public void setBudget_id(int budget_id) {
		this.budget_id = budget_id;
	}
	
	public boolean saveItem() {
		// Insert new record
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ITEM_ID, item_id);
		initialValues.put(KEY_COST_PER, cost_per);
		initialValues.put(KEY_QTY_PURCHASED, qty_purchased);
		initialValues.put(KEY_ITEM_ID, item_id);
		
		Budget dbBudget = new Budget(MainActivity.db);
		dbBudget.getCurrentBudget();
		initialValues.put(KEY_BUDGET_ID, dbBudget.getId());
        
        db.open();
        db.exec.insert(DATABASE_TABLE, null, initialValues);
        db.close();
        
        return true;
    }
}
