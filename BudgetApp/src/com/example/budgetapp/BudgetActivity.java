package com.example.budgetapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class BudgetActivity extends Activity implements OnClickListener {

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

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnViewBudget:
			startActivity(new Intent(this, ViewBudgetActivity.class));
			break;
		case R.id.btnEditBudget:
			startActivity(new Intent(this, EditBudgetActivity.class));
			break;
		case R.id.btnEditCategories:
			startActivity(new Intent(this, EditCategoriesActivity.class));
			break;
		}
	}
	
	public void onClickBack(View v){
		onBackPressed();
	}

}
