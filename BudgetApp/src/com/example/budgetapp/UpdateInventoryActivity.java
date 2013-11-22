package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.InventoryItem;
import com.example.budgetapp.databaseclasses.Item;
import com.example.budgetapp.databaseclasses.PurchaseItem;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class UpdateInventoryActivity extends Activity implements OnClickListener {
	EditText editUpc;
	EditText editName;
	EditText editDesiredQty;
	EditText editQtyRemaining;
	TextView textViewRemaining;
	SeekBar seekBarQuantity;
	Spinner spinnerUpdateCategory;
	
	Item dbItem;
	InventoryItem dbInventory;
	BudgetCategory dbCategory;
	
	BudgetCategory[] categoryArray;
	List<String> spinnerList;
	String[] spinnerArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_inventory);
		
		dbItem = new Item(MainActivity.db);
		dbCategory = new BudgetCategory(MainActivity.db);
		dbInventory = new InventoryItem(MainActivity.db);
		
		editUpc = (EditText)findViewById(R.id.editUPC);
		editName = (EditText)findViewById(R.id.editUpdateName);
		editDesiredQty = (EditText)findViewById(R.id.editDesiredQty);
		editQtyRemaining = (EditText)findViewById(R.id.editQtyRemaining);
		textViewRemaining = (TextView)findViewById(R.id.textViewRemaining);
		seekBarQuantity = (SeekBar)findViewById(R.id.seekBarQuantity);
		spinnerUpdateCategory = (Spinner)findViewById(R.id.spinnerUpdateCategory);
		
		String upc = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    upc = extras.getString("upc");
		}
		
		if(upc != "") {
			dbItem.getItemByUpc(upc);
			
		    if (!dbItem.isNew_item()) {
		    	dbInventory.getInventoryByItemId(dbItem.getId());
				displayItem();
			} else {
				Toast.makeText(null, "Item does not exist!", Toast.LENGTH_SHORT).show();
				// GOTO ADD WITH TOAST
			}
		}
		
		editUpc.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus && !editUpc.getText().toString().equals("")) {
					dbItem.getItemByUpc(editUpc.getText().toString());
					if (!dbItem.isNew_item()) {
						dbInventory.getInventoryByItemId(dbItem.getId());
						displayItem();
					} else {
						Toast.makeText(null, "Item does not exist!", Toast.LENGTH_SHORT).show();
						// GOTO ADD WITH TOAST
						
					}
				}
			}
			
		});
		
		seekBarQuantity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar s, int arg1, boolean arg2) {
				textViewRemaining.setText("Quantity remaining: " + s.getProgress() + "%");
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}
	
	public void displayItem() {
		editUpc.setText(dbItem.getUpc());
		editName.setText(dbItem.getName());
		
		List<Integer> categoryIds = new ArrayList<Integer>(); 
		categoryIds.add(0);
		for(BudgetCategory b : dbCategory.getCategoriesList()) {
			categoryIds.add(b.getId());
		}
		spinnerUpdateCategory.setSelection(categoryIds.indexOf(dbItem.getCategory_id()));
		
		editQtyRemaining.setText(String.valueOf(dbInventory.getQoh()));
		seekBarQuantity.setProgress(dbInventory.getPercent_remaining());
	}
	
	public void initializeCategoryList() {
		if (dbCategory.getAllCategories()) {
			categoryArray = dbCategory.getCategoriesList().toArray( new BudgetCategory[ dbCategory.getCategoriesList().size() ] );
			spinnerList = new ArrayList<String>(); 
			spinnerList.add("Select a Category...");
			for(BudgetCategory b : dbCategory.getCategoriesList()) {
				spinnerList.add(b.getTitle());
			}
			
			spinnerArray = spinnerList.toArray( new String[ spinnerList.size() ] );
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
			spinnerUpdateCategory.setAdapter(spinnerArrayAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		SeekBar seekQty = (SeekBar)findViewById(R.id.seekBarQuantity);
		
		switch(v.getId()) {
		case R.id.btnEmpty:
			seekQty.setProgress(0);
			break;
		case R.id.btnFill:
			seekQty.setProgress(100);
			break;
		}
	}
	
	public void onClickBack(View v){
		onBackPressed();
	}

}
