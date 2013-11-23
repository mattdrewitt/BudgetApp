package com.example.budgetapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class EditBudgetActivity extends Activity {
	List<EditText> ids;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_budget);
		
		ids = new ArrayList<EditText>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_budget, menu);
		return true;
	}
	
	
	public void onClickBack(View v){
		//onBackPressed();
		
		if(ids.size() >= 0) {
			LinearLayout layout = (LinearLayout)findViewById(R.id.scrollLayout);
			Spinner spinner = new Spinner(this);
			EditText edit = new EditText(this);
			Button button = new Button(this);
			TextView label = new TextView(this);
			TextView line = new TextView(this);
			
			label.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.8f));
			edit.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.35f));
			button.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.85f));
			
			label.setText("Amount: $");
			button.setText("×");
			
			line.setBackgroundColor(Color.BLACK);
			line.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
			ids.add(edit);
			
			LinearLayout newLayout = new LinearLayout(this);
			newLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			newLayout.addView(label);
			newLayout.addView(edit);
			newLayout.addView(button);
			
			layout.addView(newLayout);
			layout.addView(spinner);
			layout.addView(line);
		} else {
			Toast.makeText(this, ids.get(0).getText(), Toast.LENGTH_LONG).show();
		}
	}

}
