package com.example.budgetapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.budgetapp.databaseclasses.Budget;
import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.InventoryItem;
import com.example.budgetapp.databaseclasses.Item;
import com.example.budgetapp.databaseclasses.PurchaseItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.text.InputType;
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
	PurchaseItem dbPurchase;
	
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
		
		// Initialize DB items
		dbItem =  new Item(MainActivity.db);
		dbInventory = new InventoryItem(MainActivity.db);
		dbCategory = new BudgetCategory(MainActivity.db);
		dbPurchase = new PurchaseItem(MainActivity.db);
		
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
		
		//Set num fields to nums only
		editCost.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		editQty.setInputType(InputType.TYPE_CLASS_NUMBER);
		editQtyDesired.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		// Populate Category List
		initializeCategoryList();
		
		// Get the UPC if it was sent to us
		String upc = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    upc = extras.getString("upc");
		}
		
		if(upc != "") {
			dbItem.getItemByUpc(upc);
			
			if (!dbItem.isNew_item()) {
				displayItem();
				dbInventory.getInventoryByItemId(dbItem.getId());
			} else {
				seekBarRefillPoint.setMax(100);
				seekBarRefillPoint.setProgress(100);
				textViewRefill.setText("Refill point: 100%");
			}
		}
		
		editUpc.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus && !editUpc.getText().toString().equals("")) {
					dbItem.getItemByUpc(editUpc.getText().toString());
					if (!dbItem.isNew_item()) {
						displayItem();
					} else {
						//displayItem();
						seekBarRefillPoint.setMax(100);
						seekBarRefillPoint.setProgress(100);
						textViewRefill.setText("Refill point: 100%");
					}
				}
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
	
	public void displayItem() {
		editName.setText(dbItem.getName());
		
		editQtyDesired.setText(String.valueOf(dbItem.getQty_desired()));
		checkRegular.setChecked(dbItem.isRegular_purchase());
		checkService.setChecked(dbItem.isService_non_inventory());
		
		// Set occurance spinner to proper position
		Resources res = getResources();
		String[] occurance = res.getStringArray(R.array.purchaseOccurance);
		spinnerPurchaseOccurance.setSelection(Arrays.asList(occurance).indexOf(dbItem.getPurchase_occurance()));
		
		// Set category spinner to proper position
		List<Integer> categoryIds = new ArrayList<Integer>(); 
		categoryIds.add(0);
		for(BudgetCategory b : dbCategory.getCategoriesList()) {
			categoryIds.add(b.getId());
		}
		spinnerCategory.setSelection(categoryIds.indexOf(dbItem.getCategory_id()));
		
		seekBarRefillPoint.setMax(100);
		seekBarRefillPoint.setProgress(dbItem.getRefill_point());
		textViewRefill.setText("Refill point: " + dbItem.getRefill_point() + "%");
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
			dbItem.setRefill_point(seekBarRefillPoint.getProgress());
			dbItem.setService_non_inventory(checkService.isChecked());
			dbItem.setCategory_id(categoryArray[spinnerCategory.getSelectedItemPosition()-1].getId());
			
			dbItem.setRegular_purchase(checkRegular.isChecked());
			if(checkRegular.isChecked()) {
				dbItem.setPurchase_occurance(spinnerPurchaseOccurance.getSelectedItem().toString());
			}
			
			if(dbItem.saveItem()) {
				dbInventory.setItem_id(dbItem.getId());
				dbInventory.setPercent_remaining(100);
				dbInventory.setQoh(dbInventory.getQoh() + Integer.parseInt(editQty.getText().toString()));
				
				if(dbInventory.saveItem()) {
					Budget dbBudget = new Budget(MainActivity.db);
					dbBudget.getCurrentBudget();
					
					dbPurchase.setBudget_id(dbBudget.getId());
					dbPurchase.setCost_per(Double.parseDouble(editCost.getText().toString()));
					dbPurchase.setItem_id(dbItem.getId());
					dbPurchase.setQty_purchased(Integer.parseInt(editQty.getText().toString()));
					
					if(dbPurchase.saveItem()) {
						// Yay it worked, go back to the inventory screen!
						Toast.makeText(this, "Inventory Item Saved Successfuly", Toast.LENGTH_SHORT).show();
						onBackPressed();
					} else {
						Toast.makeText(this, "Purchase failed to save!", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "Inventory Item failed to save!", Toast.LENGTH_SHORT).show();
				}
				
			} else {
				Toast.makeText(this, "Item failed to save!", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
