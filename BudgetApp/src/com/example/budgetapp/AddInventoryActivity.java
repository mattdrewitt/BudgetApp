package com.example.budgetapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddInventoryActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_inventory);
		
		String upc = "";
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    upc = extras.getString("upc");
		}
		
		EditText editUpc = (EditText)findViewById(R.id.editUpc);
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
