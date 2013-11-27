package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.R.drawable;
import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.BudgetItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
	List<Integer> categoryIds;
	List<String> spinnerList;
	String[] spinnerArray;
	
	BudgetCategory dbCategory;
	BudgetItem dbBudget;
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_budget);
		
		context = getApplicationContext();
		
		dbCategory = new BudgetCategory(MainActivity.db);
		dbBudget = new BudgetItem(MainActivity.db);
		
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
		
		categoryIds = new ArrayList<Integer>(); 
		categoryIds.add(0);
		for(BudgetCategory b : dbCategory.getCategoriesList()) {
			categoryIds.add(b.getId());
		}
		
		dbBudget.getAllItemsByBudget(MainActivity.dbBudget.getId());
		
		for(BudgetItem b : dbBudget.getItemsList()) {
			addBudgetItem(b.getId(), b.getTarget_total(), b.getCategory_id());
		}
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
	
	public void onClickSubmit(View v) {
		boolean success = true;
		for(LinearLayout l : layouts) {
			int id = 0;
			int amount = 0;
			int category_id = 0;
			boolean delete = false;
			
			// If the element has been hidden it was deleted
			delete = l.getVisibility() == View.GONE;
			
			// Get the id if it exists
			EditText editId = (EditText)l.findViewWithTag("id");
			if(!editId.getText().toString().equals("")) {
				id = Integer.parseInt(editId.getText().toString());
			}
			
			// Get the amount
			EditText editAmount = (EditText)l.findViewWithTag("amount");
			if(!editAmount.getText().toString().equals("")) {
				amount = Integer.parseInt(editAmount.getText().toString());
			} else if(!delete) {
				Toast.makeText(this, "Please ensure all amounts are filled in!", Toast.LENGTH_LONG).show();
				success = false;
				break;
			}
			
			// Get the category
			Spinner spinnerCategory = (Spinner)l.findViewWithTag("category");
			if(spinnerCategory.getSelectedItemPosition() != 0) {
				category_id = categoryArray[spinnerCategory.getSelectedItemPosition()-1].getId();
			} else if(!delete) {
				Toast.makeText(this, "Please ensure all categories are chosen!", Toast.LENGTH_LONG).show();
				success = false;
				break;
			}
			
			BudgetItem dbItem = new BudgetItem(MainActivity.db);
			
			if(delete && id != 0) {
				dbItem.deleteItem(id);
			} else if (!delete) {
				if(id != 0)
					dbItem.getItem(id);
				
				 
				dbItem.setCategory_id(category_id);
				dbItem.setTarget_total(amount);
				dbItem.setBudget_id(MainActivity.dbBudget.getId());
				dbItem.saveItem();
			}
		}
		
		if(success)
			onBackPressed();
	}
	
	public void onClickAdd(View v) {
		addBudgetItem(0,0,0);
		Button submit = (Button)findViewById(R.id.buttonSubmit);

	}
	
	public void addBudgetItem(int id, int amount, int category) {
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
		editAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
		button.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.90f));
		line.setBackgroundColor(Color.BLACK);
		line.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
		editId.setVisibility(View.GONE);
		
		// Set values of displays
		label.setText("Amount: $");
		button.setText("Remove");
		button.setBackgroundResource(R.drawable.blue_button);
		button.setTextColor(Color.WHITE);
		//button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		/*
		 *      android:id="@+id/buttonSubmit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:background="@drawable/blue_button"
                android:drawableLeft="@drawable/ic_action_upload"
                android:onClick="onClickSubmit"
                android:text="Submit Budgets"
                android:textColor="#FFFFFF"
                android:textSize="18dp"
                android:textStyle="bold" 
                android:enabled="false"/>
		 */
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		spinnerCategory.setAdapter(spinnerArrayAdapter);
		
		// Set values of item
		if(id > 0)
			editId.setText(String.valueOf(id));
		if(amount > 0)
			editAmount.setText(String.valueOf(amount));
		if(category > 0)
			spinnerCategory.setSelection(categoryIds.indexOf(category));
		
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
		newLayout.addView(editId);
		
		// Button click
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!((EditText)((View)v.getParent().getParent()).findViewWithTag("id")).getText().toString().equals(""))
					((View)v.getParent().getParent()).setVisibility(View.GONE);
				else {
					layouts.remove(v.getParent().getParent());
					topLayout.removeView(((View)v.getParent().getParent()));
				}
			}
		});
		
		spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int pos, long arg3) {
				if(pos > 0) {
					for(LinearLayout l : layouts) {
						if(l != v.getParent().getParent()) {
							Spinner cat = (Spinner)(l.findViewWithTag("category"));
							if(cat.getSelectedItemPosition() == pos) {
								Toast.makeText(context, "This category is already used!\nPlease add amount to existing item.", Toast.LENGTH_LONG).show();
								((Spinner)(v.getParent())).setSelection(0);
								break;
							}
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		// Add to final layout and tracking collection
		layouts.add(newLayout);
		topLayout.addView(newLayout);
	}
}
