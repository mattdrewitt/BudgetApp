package com.example.budgetapp;

import com.example.budgetapp.databaseclasses.BudgetCategory;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditCategoriesActivity extends Activity {
	BudgetCategory dbCategory;
	Spinner spinnerExistingCategories;
	Button btnNewCategory;
	Button btnSaveCategory;
	EditText editCategoryName;
	EditText editCategoryDescription;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_categories);
		
		dbCategory = new BudgetCategory(MainActivity.db);
		
		spinnerExistingCategories = (Spinner)findViewById(R.id.spinnerExistingCategories);
		btnNewCategory = (Button)findViewById(R.id.btnNewCategory);
		btnSaveCategory = (Button)findViewById(R.id.btnSaveCategory);
		editCategoryName = (EditText)findViewById(R.id.editCategoryName);
		editCategoryDescription = (EditText)findViewById(R.id.editCategoryDescription);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_categories, menu);
		return true;
	}
	
	public void onClickBack(View v) {
		onBackPressed();
	}
	
	public void onClickSave(View v) {
		if(!editCategoryName.getText().equals("") && !editCategoryDescription.getText().equals("")) {
			dbCategory.setTitle(editCategoryName.getText().toString());
			dbCategory.setDescription(editCategoryDescription.getText().toString());
			
			if(dbCategory.saveCategory()) {
				Toast.makeText(this, "Category Saved Successfuly", Toast.LENGTH_SHORT).show();
				btnSaveCategory.setText("Update");
				btnNewCategory.setVisibility(1);
			}
			
			
		}
	}

}
