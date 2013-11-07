package com.example.budgetapp;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class InventoryActivity extends Activity implements OnClickListener {
	int buttonClicked;
	DialogFragment newFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inventory, menu);
		return true;
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		//check we have a valid result
		if (scanningResult != null) {
			// Get content from Intent Result
			String scanContent = scanningResult.getContents();
			if(scanContent != null) {
				switch(buttonClicked) {
				case R.id.btnAddInventory:
					Intent add = new Intent(this, AddInventoryActivity.class);
					add.putExtra("upc", scanContent);
					startActivity(add);
					break;
				case R.id.btnUpdateInventory:
					Intent update = new Intent(this, UpdateInventoryActivity.class);
					update.putExtra("upc", scanContent);
					startActivity(update);
					break;
				}
			}
		}
		else {
			//invalid scan data or scan canceled
			Toast toast = Toast.makeText(getApplicationContext(), 
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnAddInventory || v.getId() == R.id.btnUpdateInventory) {
			//create dialog fragment when button is clicked 
			newFragment = new ScannerFragment();
			newFragment.show(getFragmentManager(), "missiles");
			
			// Set int for button tracking
			buttonClicked = v.getId();
		}
	}
	
	public void onClickScanner(View v) {
		newFragment.dismiss();
		
		// Instantiate ZXing integration class
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		
		// Start scanning
		scanIntegrator.initiateScan();
	}
	
	public void onClickManual(View v) {
		newFragment.dismiss();
		
		switch(buttonClicked) {
		case R.id.btnAddInventory:
			startActivity(new Intent(this, AddInventoryActivity.class));
			break;
		case R.id.btnUpdateInventory:
			startActivity(new Intent(this, UpdateInventoryActivity.class));
			break;
		}
	}
}
