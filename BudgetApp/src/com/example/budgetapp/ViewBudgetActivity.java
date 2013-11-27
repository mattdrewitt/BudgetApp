package com.example.budgetapp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.budgetapp.databaseclasses.BudgetCategory;
import com.example.budgetapp.databaseclasses.BudgetItem;
import com.example.budgetapp.databaseclasses.PurchaseItem;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ViewBudgetActivity extends Activity {
	TextView textCats;
	TextView textAmts;
	TextView textSum;
	TextView textRange;
	
	List<Integer> categoryIds;
	List<String> categoryList;
	
	BudgetCategory dbCategory;
	BudgetItem dbBudget;
	PurchaseItem dbPurchase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_budget);
		
		// Initialize db objects
		dbCategory = new BudgetCategory(MainActivity.db);
		dbBudget = new BudgetItem(MainActivity.db);
		dbPurchase = new PurchaseItem(MainActivity.db);
		
		textCats = (TextView)findViewById(R.id.textCats);
		textAmts = (TextView)findViewById(R.id.textAmts);
		textSum = (TextView)findViewById(R.id.textSum);
		textRange = (TextView)findViewById(R.id.textRange);
		
		// Category Arrays/Lists for convenience
		if (dbCategory.getAllCategories()) {
			categoryList = new ArrayList<String>(); 
			for(BudgetCategory b : dbCategory.getCategoriesList()) {
				categoryList.add(b.getTitle());
			};
		}
		
		categoryIds = new ArrayList<Integer>(); 
		for(BudgetCategory b : dbCategory.getCategoriesList()) {
			categoryIds.add(b.getId());
		}
		
		dbBudget.getAllItemsByBudget(MainActivity.dbBudget.getId());
		
		displayBudgetItem();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_budget, menu);
		return true;
	}
	
	public void onClickBack(View v){
		onBackPressed();
	}
	
	public void displayBudgetItem() {
		String cats = "";
		String amounts = "";
		String sum = "";
		
		int totalBudget = 0;
		int totalSpent = 0;
		
		for(BudgetItem b : dbBudget.getItemsList()) {
			String cat = categoryList.get(categoryIds.indexOf(b.getCategory_id()));
			double spent = dbPurchase.getSpendingForCategory(MainActivity.dbBudget.getId(), b.getCategory_id());
			totalBudget += b.getTarget_total();
			DecimalFormat decim = new DecimalFormat("#.##");
			Double price1 = Double.parseDouble(decim.format(spent));
			Double targetTotal = Double.parseDouble(decim.format(b.getTarget_total() ));
			cats += cat + "<br />";
			amounts += "$" + targetTotal + "<br />";
			if(spent > 0) {
				totalSpent += spent;
				cats += "<br />";
				amounts += "<font color='#FF0000'>- $" + price1 + "</font><br />";
			}
		}
		DecimalFormat decim = new DecimalFormat("#.##");
		Double priceTotal = Double.parseDouble(decim.format(totalBudget));
		Double priceSpent = Double.parseDouble(decim.format(totalSpent));
		sum += "Total Budget:   $" + priceTotal + "<br />";
		sum += "Total Spent:   <font color='#FF0000'>$" + priceSpent + "</font><br />-----------------------------<br />";
		
		double remaining = totalBudget - (totalSpent * 1.0);

		Double price2 = Double.parseDouble(decim.format(remaining));
		
		if (remaining > 0)
			sum += "Remaining:   <font color='#0794E8'>$" + price2 + "</font>";
		else
			sum += "Remaining:   <font color='#0794E8'>$" + price2 + "</font>";
		
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM, yyyy");
		textRange.setText(formatter.format(MainActivity.dbBudget.getStart_date()));
		String category = "<font color='#0794E8'>" + cats + "</font>";
		textCats.setText(Html.fromHtml(category));
		textAmts.setText(Html.fromHtml(amounts));
		textSum.setText(Html.fromHtml(sum));
	}

}
