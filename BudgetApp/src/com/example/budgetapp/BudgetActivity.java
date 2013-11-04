package com.example.budgetapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BudgetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.budget, menu);
		return true;
	}

}
