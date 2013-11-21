package com.example.budgetapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.example.budgetapp.databaseclasses.Budget;
import com.example.budgetapp.databaseclasses.DBAdapter;
import com.example.budgetapp.databaseclasses.Item;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	public static DBAdapter db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DBAdapter(this);
		
		Budget dbBudget = new Budget(db);
		
		// This will either initialize the budget, or get the current to ensure we have one created
		dbBudget.getCurrentBudget();
	}

	public void CopyDB(InputStream inputStream, 
    OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
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
