package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.InventoryItem;
import com.example.budgetapp.databaseclasses.ShoppingList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class ViewInventoryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_inventory);
		
		// Get the inventory and create a list from all of the titles of the items
		setupListView();
		
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
	
	// Create ArrayList to hold parent Items and Child Items
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    
	
	public void setupListView(){
		ExpandableListView listview1 = (ExpandableListView)findViewById(R.id.expandableListView1);

		listview1.setDividerHeight(2);
		listview1.setGroupIndicator(null);
		listview1.setClickable(true);
		
		parentItems.clear();
		childItems.clear();
        MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        listview1.setAdapter(adapter);
        
        //loop through listInventoryItems and add names as parent items 
		//Going to do this for each shoppingList item that we have. 
		InventoryItem dbInventory = new InventoryItem(MainActivity.db);

        if(dbInventory.getAllInventory()) {
        	for(InventoryItem item : dbInventory.getItemsList()) { 		
            	parentItems.add(item.getItem().getName());
                ArrayList<String> child = new ArrayList<String>();
                child.add("QTY: " +   String.valueOf(item.getQoh()) );
                child.add("Remaining: " + String.valueOf(item.getPercent_remaining()) + "%");
                child.add("UPC: " + String.valueOf(item.getItem().getUpc()));
                childItems.add(child);
        		
        	}
		}
        
        // Create the Adapter
        adapter = new MyExpandableAdapter(parentItems, childItems);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        
        // Set the Adapter to expandableList
        listview1.setAdapter(adapter);
        //listview1.setOnChildClickListener((OnChildClickListener) this);
        listview1.setVisibility(View.VISIBLE);
	}
	
	
}
