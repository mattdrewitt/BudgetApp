package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.InventoryItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewInventoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_inventory);
		
		ListView listInventoryItems = (ListView)findViewById(R.id.listInventoryItems);
		InventoryItem dbInventory = new InventoryItem(MainActivity.db);
		
		// Get the inventory and create a list from all of the titles of the items
		if(dbInventory.getAllInventory()) {
			List<String> inventoryList = new ArrayList<String>();
			for(InventoryItem i : dbInventory.getItemsList()) {
				inventoryList.add(i.getItem().getName() + "\nQty: " + String.valueOf(i.getQoh()) + 
						" Remaining: " + String.valueOf(i.getPercent_remaining()) + "\nUPC: " + String.valueOf(i.getItem().getUpc()));
			}
			String[] item_list = inventoryList.toArray( new String[ inventoryList.size() ] );
			
			ArrayAdapter<String> inventoryAdapter = new ArrayAdapter<String>(
	                this, android.R.layout.simple_list_item_1, item_list);
			listInventoryItems.setAdapter(inventoryAdapter);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_inventory, menu);
		return true;
	}

	public void onClickBack(View v){
		onBackPressed();
	}
}
