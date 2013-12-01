package com.example.budgetapp.databaseclasses;

import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

public class ShoppingList {
	//format nicely in here... 
	private DBAdapter db;

    static final String DATABASE_NAME = "BudgetAppDB";
	//inventory_item - key_qoh, key_percent_remaining, item.keyname, item,key_qty_desired, key_refill point, key service
	private InventoryItem shop_inv; 
	private Item shop_item;
	
    public ShoppingList(int key_qoh, int key_percent_remaining, String key_name, int key_qty_desired, int key_refill,
    		boolean key_service, DBAdapter adapter){
    	shop_inv = new InventoryItem(db);
    	shop_item = new Item(db);
    	shop_inv.setQoh(key_qoh);
    	shop_inv.setPercent_remaining(key_percent_remaining);
    	shop_item.setName(key_name);
    	shop_item.setQty_desired(key_qty_desired);
    	shop_item.setRefill_point(key_refill);
        shop_item.setService_non_inventory(key_service);
        db = adapter;
    }
    public ShoppingList(DBAdapter adapter){
    	this.db = adapter;
    }
	
	public InventoryItem getInv() {
		return shop_inv;
	}
	
	public Item getItem() {
		return shop_item;
	}

	public void setInv(InventoryItem ii) {
		this.shop_inv = ii;
	}

	//use this list to build array 
	
    //---retrieves all the items---
    public ArrayList<ShoppingList> getShoppingList(String purchaseOccurance)
    {

    	//INVENTORY ITEM 
       	String item_table = Item.DATABASE_TABLE;
       	String inventoryitem_table = InventoryItem.DATABASE_TABLE;
       	
       	SQLiteQueryBuilder query = new SQLiteQueryBuilder();
       	query.setTables(InventoryItem.DATABASE_TABLE + ", " + Item.DATABASE_TABLE);
       	query.setTables(InventoryItem.DATABASE_TABLE + " INNER JOIN " + Item.DATABASE_TABLE
       					+ " ON " + InventoryItem.DATABASE_TABLE + "." + InventoryItem.KEY_ITEM_ID +
       					" = " + Item.DATABASE_TABLE + "._Id");

       	query.appendWhere( inventoryitem_table + "." +
       	 InventoryItem.KEY_QOH + " * " + item_table +
       	 "." + Item.KEY_REFILL_POINT + "/100 < " + 
       	 item_table + "." + Item.KEY_QTY_DESIRED + " OR ("
       	 +item_table + "." + Item.KEY_PURCHASE_OCCURANCE + " = \""+ purchaseOccurance+"\" AND "
       	 + item_table + "." + Item.KEY_REGULAR_PURCHASE + " = 1)");
       	 		
       	//have the query built now just need to query the database.
        ArrayList<ShoppingList> shoppingList = new ArrayList<ShoppingList>();
       	try{
       	db.open();
       	Cursor mCursor = query.query(db.exec, null, null, null, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0) {
        	mCursor.moveToFirst();
			do {
				//inventory_item - key_qoh, key_percent_remaining, item.keyname, item,key_qty_desired, key_refill point, key service
				ShoppingList item = new ShoppingList(mCursor.getInt(2), mCursor.getInt(3), mCursor.getString(6), mCursor.getInt(7),
													mCursor.getInt(7), mCursor.getInt(11) == 1 ? true : false, db);
				shoppingList.add(item);				
			} while(mCursor.moveToNext());
        } else {
        	db.close();
        	
        	return null;
        }
       }
       	catch(Exception ex){
            db.close();
       		System.out.println("Exception thrown: " + ex.getMessage());
       	}
       	
   
        return shoppingList;
    }
}
