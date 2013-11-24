package com.example.budgetapp.databaseclasses;


import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.TextView;

public class ShoppingList {
	
	//format nicely in here... 
	ArrayList<ShoppingListItem> shoppingItems;
	private DBAdapter db;
	

    static final String DATABASE_NAME = "BudgetAppDB";
    
	private class ShoppingListItem{
		private TextView detail;
		private TextView label;
	}
	
	
	private InventoryItem shop_inv; 
	private Item shop_item;
	private ArrayList<ShoppingList> shoppingList;
	//use this list to build array 
	
    //---retrieves all the items---
    public boolean getShoppingList()
    {
    	//INVENTORY ITEM 
       	db.open();
       	String item_table = Item.DATABASE_TABLE;
       	String inventoryitem_table = InventoryItem.DATABASE_TABLE;
       	
       	SQLiteQueryBuilder query = new SQLiteQueryBuilder();
       	query.setTables(InventoryItem.DATABASE_TABLE + ", " + Item.DATABASE_TABLE);
       	query.setTables(InventoryItem.DATABASE_TABLE + " INNER JOIN " + Item.DATABASE_TABLE
       					+ " ON " + InventoryItem.DATABASE_TABLE + "." + InventoryItem.KEY_ITEM_ID +
       					" = " + Item.DATABASE_TABLE + ".Id");
       	String Columns = InventoryItem.KEY_QOH + "." + 
       					InventoryItem.KEY_PERCENT_REMAINING
       					+ ", " + Item.DATABASE_TABLE + "." +Item.KEY_NAME 
       					+ ", " + Item.DATABASE_TABLE + "." + Item.KEY_QTY_DESIRED
       					+ ", " + Item.DATABASE_TABLE + "." + Item.KEY_REFILL_POINT
       					+ ", " + Item.DATABASE_TABLE + "." + Item.KEY_SERVICE;
       					
       //	query.TopRecords = maxRecords;
       	/*
       	 * Where Item.qty * refillPoint/100 < Item.qtyDesired 
OR
(purchaseOccurance == 'purchaseOccurance' AND
regularPurchase = true) 

       	 * */
       	/*
       	query.appendWhere( inventoryitem_table + "." +
       	 InventoryItem.KEY_QOH + " * " + item_table +
       	 "." + Item.KEY_REFILL_POINT + "/100 < " + 
       	 item_table + "." + Item.KEY_QTY_DESIRED + " AND "
       	 +item_table + "." + field2 = ?");
       	query.query(db.exec, new String[] { "field1" }, "enteredField3 BETWEEN field3 AND field4",
       	        new String[] { "enteredField1", "enteredField2"}, null, null, null);

       	// or, have a DbCommand object built
       	// for even more safety against SQL Injection attacks:
       	query.SetDbProviderFactory(
       	      DbProviderFactories.GetFactory(
       	      "System.Data.SqlClient")); 
       	DbCommand command = query.BuildCommand();
        */
    	
    }
	
	
	
	
	
	
}
