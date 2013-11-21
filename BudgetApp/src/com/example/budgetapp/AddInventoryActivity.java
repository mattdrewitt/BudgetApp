package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.InventoryItem;
import com.example.budgetapp.databaseclasses.Item;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class AddInventoryActivity extends Activity {
	Item dbItem;
	InventoryItem dbInventory;
	BudgetCategory dbCategory;
	
	BudgetCategory[] categoryArray;
	List<String> spinnerList;
	String[] spinnerArray;
	
	TextView textViewRefill;
	EditText editUpc;
	EditText editName;
	EditText editCost;
	EditText editQty;
	EditText editQtyDesired;
	Spinner spinnerCategory;
	Spinner spinnerPurchaseOccurance;
	CheckBox checkRegular;
	CheckBox checkService;
	SeekBar seekBarRefillPoint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_inventory);
		
		dbItem =  new Item(MainActivity.db);
		dbInventory = new InventoryItem(MainActivity.db);
		dbCategory = new BudgetCategory(MainActivity.db);
		
		String upc = "";
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    upc = extras.getString("upc");
		}
		
		if(upc != "") {
			dbItem.getItemByUpc(upc);
		}
		
		// Setup ui element objects
		editUpc = (EditText)findViewById(R.id.editUpc);
		editName = (EditText)findViewById(R.id.editName);
		spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
		editCost = (EditText)findViewById(R.id.editCost);
		editQty = (EditText)findViewById(R.id.editQty);
		spinnerPurchaseOccurance = (Spinner)findViewById(R.id.spinnerPurchaseOccurance);
		spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
		editQtyDesired = (EditText)findViewById(R.id.editQtyDesired);
		checkRegular = (CheckBox)findViewById(R.id.checkRegular);
		checkService = (CheckBox)findViewById(R.id.checkService);
		seekBarRefillPoint = (SeekBar)findViewById(R.id.seekBarRefillPoint);
		textViewRefill = (TextView)findViewById(R.id.textViewRefill);
		
		// Populate Category List
		initializeCategoryList();
		
		editUpc.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus && !editUpc.getText().equals("") )
					dbItem.getItemByUpc(editUpc.getText().toString());
			}
			
		});
		
		checkRegular.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton c, boolean arg1) {
				if(c.isChecked()) {
					spinnerPurchaseOccurance.setVisibility(1);
				} else {
					spinnerPurchaseOccurance.setVisibility(-1);
				}
					
			}
		});
		
		if (!dbItem.isNew_item()) {
			editName.setText(dbItem.getName());
			// Set category stuff
			editQtyDesired.setText(dbItem.getQty_desired());
			checkRegular.setChecked(dbItem.isRegular_purchase());
			checkService.setChecked(dbItem.isService_non_inventory());
			
			seekBarRefillPoint.setMax(100);
			seekBarRefillPoint.setProgress(dbItem.getRefill_point());
			textViewRefill.setText("Refill point: " + dbItem.getRefill_point() + "%");
			
		} else {
			seekBarRefillPoint.setMax(100);
			seekBarRefillPoint.setProgress(100);
			textViewRefill.setText("Refill point: 100%");
		}
		
		seekBarRefillPoint.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar s, int arg1, boolean arg2) {
				textViewRefill.setText("Refill point: " + s.getProgress() + "%");
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});
		
		editUpc.setText(upc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
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
			spinnerCategory.setAdapter(spinnerArrayAdapter);
		}
	}
	
	public void onClickBack(View v) {
		onBackPressed();
	}
	
	public void onClickSave(View v) {
		if(!editUpc.getText().equals("") && !editName.getText().equals("") && !editCost.getText().equals("") &&
			!editQty.getText().equals("") && !editQtyDesired.getText().equals("") &&
			spinnerCategory.getSelectedItemPosition() != 0) {
			
			dbItem.setUpc(editUpc.getText().toString());
			dbItem.setName(editName.getText().toString());
			dbItem.setQty_desired(Integer.parseInt(editQtyDesired.getText().toString()));
			dbItem.setPurchase_occurance(spinnerPurchaseOccurance.getSelectedItem().toString());
			dbItem.setRefill_point(seekBarRefillPoint.getProgress());
			dbItem.setService_non_inventory(checkService.isChecked());
			dbItem.setRegular_purchase(checkRegular.isChecked());
			dbItem.setCategory_id(categoryArray[spinnerCategory.getSelectedItemPosition()-1].getId());
			
			if(dbItem.saveItem()) {
				dbInventory.setItem_id(dbItem.getId());
				dbInventory.setPercent_remaining(100);
				dbInventory.setQoh(Integer.parseInt(editQty.getText().toString()));
				
				if(dbInventory.saveItem()) {
					
				}
				Toast.makeText(this, "Inventory Item Saved Successfuly", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
