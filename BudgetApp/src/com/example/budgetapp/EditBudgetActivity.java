package com.example.budgetapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class EditBudgetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_budget);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_budget, menu);
		return true;
	}
	
	
	public void onClickBack(View v){
		onBackPressed();
	}

}
