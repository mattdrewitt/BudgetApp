package com.example.budgetapp;



import java.util.ArrayList;
import java.util.Map;

import com.example.budgetapp.databaseclasses.ShoppingList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Spinner;

public class ShoppingListActivity extends Activity{
	private static boolean justCreated; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list);

		
		
		
		Spinner purchaseOccuranceSp =  (Spinner)findViewById(R.id.spinnerShopping);
	    //ArrayAdapter<String> mAdapter;
	    //mAdapter= new ArrayAdapter<String>(this, R.layout.spinner_item, R.array.purchaseOccurance );
	    //purchaseOccuranceSp.setAdapter(mAdapter);
		
		
		purchaseOccuranceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//When an item is selected we want to pull items from the DB that have 
				//this selection. 
				 String item = parent.getItemAtPosition(position).toString();
				 setupListView(item);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
			
		});
		
		

		
	}
	
	// Create ArrayList to hold parent Items and Child Items
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    
	
	public void setupListView(String itemSelected){
		ExpandableListView listview1 = (ExpandableListView)findViewById(R.id.expandableListView1);

		listview1.setDividerHeight(2);
		listview1.setGroupIndicator(null);
		listview1.setClickable(true);
		
		parentItems.clear();
		childItems.clear();
        MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        listview1.setAdapter(adapter);
        
		ShoppingList shop = new ShoppingList(MainActivity.db);
		ArrayList<ShoppingList> shoppingItems = shop.getShoppingList(itemSelected);
        //loop through shoppingItems and add names as parent items 
		//Going to do this for each shoppingList item that we have. 
        for(ShoppingList item :shoppingItems){	
        	parentItems.add(item.getItem().getName());
            ArrayList<String> child = new ArrayList<String>();
            child.add(item.getItem().getQty_desired() + "");
            child.add("What Youve got left: " + item.getInv().getQoh());
            int qtyNeeded = item.getItem().getQty_desired() - item.getInv().getQoh();
            child.add("What you need: " + qtyNeeded);
            child.add("Percent Remaining: ");
            childItems.add(child);
        }

		
        // Create the Adapter
        adapter = new MyExpandableAdapter(parentItems, childItems);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        
        // Set the Adapter to expandableList
        listview1.setAdapter(adapter);
        //listview1.setOnChildClickListener((OnChildClickListener) this);
        listview1.setVisibility(View.VISIBLE);
	}


	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_list, menu);
		return true;
	}
	
	public void onClickBack(View v){
		onBackPressed();
	}

}
