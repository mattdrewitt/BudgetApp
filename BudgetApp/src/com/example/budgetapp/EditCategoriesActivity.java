package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	
	BudgetCategory[] categoryArray;
	List<String> spinnerList;
	String[] spinnerArray;
	
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
		
		initializeCategoryList();
		
		spinnerExistingCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int pos, long arg3) {
				if(pos > 0) {
					BudgetCategory b = categoryArray[pos-1];
					editCategoryName.setText(b.getTitle());
					editCategoryDescription.setText(b.getDescription());
					btnSaveCategory.setText("Update");
					btnNewCategory.setVisibility(View.VISIBLE);
					
					dbCategory.setNew_cat(false);
					dbCategory.setId(b.getId());
					dbCategory.setTitle(b.getTitle());
					dbCategory.setDescription(b.getDescription());
				} else {
					btnSaveCategory.setText("Add");
					btnNewCategory.setVisibility(View.INVISIBLE);
					editCategoryName.setText("");
					editCategoryDescription.setText("");
					editCategoryName.requestFocus();
					
					clearCategory();
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_categories, menu);
		return true;
	}
	
	public void initializeCategoryList() {
		if (dbCategory.getAllCategories()) {
			categoryArray = dbCategory.getCategoriesList().toArray( new BudgetCategory[ dbCategory.getCategoriesList().size() ] );
			spinnerList = new ArrayList<String>(); 
			spinnerList.add("");
			for(BudgetCategory b : dbCategory.getCategoriesList()) {
				spinnerList.add(b.getTitle());
			}
			
			spinnerArray = spinnerList.toArray( new String[ spinnerList.size() ] );
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
			spinnerExistingCategories.setAdapter(spinnerArrayAdapter);
		}
	}
	
	public void clearCategory() {
		dbCategory.setNew_cat(true);
		dbCategory.setId(0);
		dbCategory.setTitle("");
		dbCategory.setDescription("");
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
				btnNewCategory.setVisibility(View.VISIBLE);
				dbCategory.setNew_cat(false);
				
				initializeCategoryList();
				spinnerExistingCategories.setSelection(spinnerList.indexOf(dbCategory.getTitle()));
			}
		}
	}
	
	public void onClickNew(View v) {
		dbCategory = new BudgetCategory(MainActivity.db);
		clearCategory();
		btnSaveCategory.setText("Add");
		btnNewCategory.setVisibility(View.INVISIBLE);
		spinnerExistingCategories.setSelection(0);
		editCategoryName.requestFocus();
	}

}
