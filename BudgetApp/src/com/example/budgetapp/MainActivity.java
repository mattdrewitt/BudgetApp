package com.example.budgetapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

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

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnBudget:
			startActivity(new Intent(this, BudgetActivity.class));
			break;
		case R.id.btnInventory:
			startActivity(new Intent(this, InventoryActivity.class));
			break;
		case R.id.btnShoppingList:
			startActivity(new Intent(this, ShoppingListActivity.class));
			break;
		}
	}
}
