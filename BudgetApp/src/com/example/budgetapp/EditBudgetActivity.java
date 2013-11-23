package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class EditBudgetActivity extends Activity {
	List<LinearLayout> layouts;
	LinearLayout topLayout;
	BudgetCategory[] categoryArray;
	List<String> spinnerList;
	String[] spinnerArray;
	
	BudgetCategory dbCategory;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_budget);
		
		dbCategory = new BudgetCategory(MainActivity.db);
		
		topLayout = (LinearLayout)findViewById(R.id.scrollLayout);
		layouts = new ArrayList<LinearLayout>();
		
		
		if (dbCategory.getAllCategories()) {
			categoryArray = dbCategory.getCategoriesList().toArray( new BudgetCategory[ dbCategory.getCategoriesList().size() ] );
			spinnerList = new ArrayList<String>(); 
			spinnerList.add("Select a Category...");
			for(BudgetCategory b : dbCategory.getCategoriesList()) {
				spinnerList.add(b.getTitle());
			}
			
			spinnerArray = spinnerList.toArray( new String[ spinnerList.size() ] );
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_budget, menu);
		return true;
	}
	
	public void onClickBack(View v) {
		onBackPressed();
	}
	
	public void onClickAdd(View v) {
		// Initialize Objects
		LinearLayout topRow = new LinearLayout(this);
		LinearLayout newLayout = new LinearLayout(this);
		Spinner spinnerCategory = new Spinner(this);
		EditText editAmount = new EditText(this);
		EditText editId = new EditText(this);
		Button button = new Button(this);
		TextView label = new TextView(this);
		TextView line = new TextView(this);
		
		// Set tags
		spinnerCategory.setTag("category");
		editAmount.setTag("amount");
		editId.setTag("id");
		
		// Set layout formatting
		label.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.8f));
		editAmount.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.35f));
		button.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.85f));
		line.setBackgroundColor(Color.BLACK);
		line.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
		
		// Set values of views
		label.setText("Amount: $");
		button.setText("×");
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		spinnerCategory.setAdapter(spinnerArrayAdapter);
		
		// Setup containing layout for first row
		topRow.setOrientation(LinearLayout.HORIZONTAL);
		topRow.addView(label);
		topRow.addView(editAmount);
		topRow.addView(button);
		
		// Setup entire container
		newLayout.setOrientation(LinearLayout.VERTICAL);
		newLayout.addView(topRow);
		newLayout.addView(spinnerCategory);
		newLayout.addView(line);
		
		// Button click
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((View)v.getParent().getParent()).setVisibility(View.GONE);
			}
		});
		
		// Add to final layout and tracking collection
		layouts.add(newLayout);
		topLayout.addView(newLayout);
	}
}
