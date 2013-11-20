package com.example.budgetapp;

import com.example.budgetapp.databaseclasses.InventoryItem;
import com.example.budgetapp.databaseclasses.Item;

import android.os.Bundle;
import android.app.Activity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class AddInventoryActivity extends Activity {
	Item dbItem;
	InventoryItem dbInventory;
	
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
		dbInventory = new InventoryItem();
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
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
	
	public void onClickBack(View v){
		onBackPressed();
	}

}
