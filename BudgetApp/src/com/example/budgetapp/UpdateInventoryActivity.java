package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.InventoryItem;
import com.example.budgetapp.databaseclasses.Item;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_inventory);
		context = getApplicationContext();
		
		// Initialize DB Items
		dbItem = new Item(MainActivity.db);
		dbCategory = new BudgetCategory(MainActivity.db);
		dbInventory = new InventoryItem(MainActivity.db);
		
		// Setup ui element objects
		editUpc = (EditText)findViewById(R.id.editUPC);
		editName = (EditText)findViewById(R.id.editUpdateName);
		editDesiredQty = (EditText)findViewById(R.id.editDesiredQty);
		editQtyRemaining = (EditText)findViewById(R.id.editQtyRemaining);
		textViewRemaining = (TextView)findViewById(R.id.textViewRemaining);
		seekBarQuantity = (SeekBar)findViewById(R.id.seekBarQuantity);
		spinnerUpdateCategory = (Spinner)findViewById(R.id.spinnerUpdateCategory);
		
		// Set num fields to nums only
		editDesiredQty.setInputType(InputType.TYPE_CLASS_NUMBER);
		editQtyRemaining.setInputType(InputType.TYPE_CLASS_NUMBER);
		
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
			
		    if (!dbItem.isNew_item() ) {
		    	dbInventory.getInventoryByItemId(dbItem.getId());
		    	if(!dbInventory.isNew_item()) {
		    		displayItem();
		    	} else {
		    		Toast.makeText(this, "Item does not exist!", Toast.LENGTH_SHORT).show();
					// GOTO ADD WITH TOAST
		    	}
			} else {
				Toast.makeText(this, "Item does not exist!", Toast.LENGTH_SHORT).show();
				// GOTO ADD WITH TOAST
			}
		}
		
		// UPC focus lost
		editUpc.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus && !editUpc.getText().toString().equals("")) {
					dbItem.getItemByUpc(editUpc.getText().toString());
					if (!dbItem.isNew_item() ) {
				    	dbInventory.getInventoryByItemId(dbItem.getId());
				    	if(!dbInventory.isNew_item()) {
				    		displayItem();
				    	} else {
				    		Toast.makeText(context, "Item does not exist!", Toast.LENGTH_SHORT).show();
							// GOTO ADD WITH TOAST
				    	}
					} else {
						Toast.makeText(context, "Item does not exist!", Toast.LENGTH_SHORT).show();
						// GOTO ADD WITH TOAST
					}
				}
			}
			
		});
		
		// QTY Focus lost
		editQtyRemaining.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(!editDesiredQty.getText().toString().equals("") && !editQtyRemaining.getText().toString().equals(""))
					calcPercent();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		// QTY Focus lost
		editDesiredQty.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(!editDesiredQty.getText().toString().equals("") && !editQtyRemaining.getText().toString().equals(""))
					calcPercent();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		seekBarQuantity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
				textViewRemaining.setText("Quantity remaining: " + s.getProgress() + "%");
				if(!editDesiredQty.getText().toString().equals(""))
					calcRemaining(progress);
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
		
		editDesiredQty.setText(String.valueOf(dbItem.getQty_desired()));
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
	
	public void calcPercent() {
		int percent; 
		double desired, remaining;
		desired = Double.parseDouble(editDesiredQty.getText().toString());
		remaining = Double.parseDouble(editQtyRemaining.getText().toString());
		
		percent = Math.round((float)(remaining / desired * 100));
		
		if(percent > 100)
			percent = 100;
		
		seekBarQuantity.setProgress(percent);
	}
	
	public void calcRemaining(int progress) {
		double percent, desired, remaining;
		
		desired = Double.parseDouble(editDesiredQty.getText().toString());
		remaining = Double.parseDouble(editQtyRemaining.getText().toString());
		
		if(desired >= remaining || progress < 80) {
			percent = (double)seekBarQuantity.getProgress() / 100;
			
			remaining = desired * percent;
			
			editQtyRemaining.setText(String.valueOf(Math.round(remaining)));
		} else {
			seekBarQuantity.setProgress(100);
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
	
	public void onClickBack(View v) {
		onBackPressed();
	}
	
	public void onClickSave(View v) {
		if(!editUpc.getText().toString().equals("") && !editName.getText().toString().equals("") &&
			spinnerUpdateCategory.getSelectedItemPosition() != 0 && !editDesiredQty.getText().toString().equals("") &&
			!editQtyRemaining.getText().toString().equals("")) {
			dbItem.setName(editName.getText().toString());
			dbItem.setCategory_id(categoryArray[spinnerUpdateCategory.getSelectedItemPosition()-1].getId());
			dbItem.setQty_desired(Integer.parseInt(editDesiredQty.getText().toString()));
			
			dbInventory.setQoh(Integer.parseInt(editQtyRemaining.getText().toString()));
			dbInventory.setPercent_remaining(seekBarQuantity.getProgress());
			
			if(dbItem.saveItem()) {
				if(dbInventory.saveItem()) {
					Toast.makeText(this, "Inventory Item Updated Successfully", Toast.LENGTH_SHORT).show();
					onBackPressed();
				} else {
					Toast.makeText(this, "Inventory Item failed to save!", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Item failed to save!", Toast.LENGTH_SHORT).show();
			}
		} else {
			String errors = "Please fill in the following fields:\n";
			if(editUpc.getText().toString().equals("")) {
				errors += "- UPC\n";
			}
			if(editName.getText().toString().equals("")) {
				errors += "- Name\n";
			}
			if(spinnerUpdateCategory.getSelectedItemPosition() == 0) {
				errors += "- Category\n";
			}
			if(editDesiredQty.getText().toString().equals("")) {
				errors += "- Desired Qty\n";
			}
			if(editQtyRemaining.getText().toString().equals("")) {
				errors += "- Remaining Qty";
			}
			Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
		}
	}

}
