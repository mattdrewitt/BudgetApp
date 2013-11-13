package com.example.budgetapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SeekBar;

public class UpdateInventoryActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_inventory);
		
		String upc = "";
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    upc = extras.getString("upc");
		}
		
		EditText editUpc = (EditText)findViewById(R.id.editUPC);
		editUpc.setText(upc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
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
