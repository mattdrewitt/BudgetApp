package com.example.budgetapp;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClickAdd(View v){
		//create dialog fragment when button is clicked 
		DialogFragment newFragment = new ScannerFragment();
	    newFragment.show(getFragmentManager(), "missiles");
		
	}
	
    public void onClickScanner(View v){
    	Toast.makeText(this, "Scanner", Toast.LENGTH_LONG).show();
    	
    }
    public void onClickManual(View v){	
    	Toast.makeText(this, "Manual", Toast.LENGTH_LONG).show();
    	
    }
}    




