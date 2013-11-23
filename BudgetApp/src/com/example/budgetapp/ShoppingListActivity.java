package com.example.budgetapp;



import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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
		justCreated = true;
		purchaseOccuranceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//When an item is selected we want to pull items from the DB that have 
				//this selection. 
				 String item = parent.getItemAtPosition(position).toString();
				 if(justCreated){
					 
					 justCreated = false;
				 }
				 else{
					 setupListView();
				 }

				
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
    
	public void generateShoppingList(String item){
		//make a map up of top values and children 
		Map<String, ArrayList<String>> testingList = null;
		ArrayList<String> items = new ArrayList<String>();
		items.add("QTY: 10");
		items.add("Cost: $20 per");
		testingList.put("Item1", items);
		testingList.put("Item2", items);
		testingList.put("Item3", items);
		
		
	}
	
	public void setupListView(){
		ExpandableListView listview1 = (ExpandableListView)findViewById(R.id.expandableListView1);
		listview1.setDividerHeight(2);
		listview1.setGroupIndicator(null);
		listview1.setClickable(true);
		
		 // Set the Items of Parent
        setGroupParents();
        // Set The Child Data
        setChildData();
		
        // Create the Adapter
        MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems, childItems);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        
        // Set the Adapter to expandableList
        listview1.setAdapter(adapter);
        //listview1.setOnChildClickListener((OnChildClickListener) this);
        listview1.setVisibility(View.VISIBLE);
	}

	
	 // method to add parent Items
    public void setGroupParents() 
    {
        parentItems.add("Fruits");
        parentItems.add("Flowers");
        parentItems.add("Animals");
        parentItems.add("Birds");
    }
    
 // method to set child data of each parent
    public void setChildData() 
    {

        // Add Child Items for Fruits
        ArrayList<String> child = new ArrayList<String>();
        child.add("Apple");
        child.add("Mango");
        child.add("Banana");
        child.add("Orange");
        
        childItems.add(child);

        // Add Child Items for Flowers
        child = new ArrayList<String>();
        child.add("Rose");
        child.add("Lotus");
        child.add("Jasmine");
        child.add("Lily");
        
        childItems.add(child);

        // Add Child Items for Animals
        child = new ArrayList<String>();
        child.add("Lion");
        child.add("Tiger");
        child.add("Horse");
        child.add("Elephant");
        
        childItems.add(child);

        // Add Child Items for Birds
        child = new ArrayList<String>();
        child.add("Parrot");
        child.add("Sparrow");
        child.add("Peacock");
        child.add("Pigeon");
        
        childItems.add(child);
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
