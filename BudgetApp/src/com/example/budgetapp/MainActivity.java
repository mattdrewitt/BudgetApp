package com.example.budgetapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.budgetapp.databaseclasses.DBAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {
	public static DBAdapter db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//this.deleteDatabase("MyDB");
		db = new DBAdapter(this);
		
//		try {
//            String destPath = "/data/data/" + getPackageName() +
//                "/databases";
//            File f = new File(destPath);
//            if (!f.exists()) {            	
//            	f.mkdirs();
//                f.createNewFile();
//            	
//            	//---copy the db from the assets folder into 
//            	// the databases folder---
//                CopyDB(getBaseContext().getAssets().open("mydb"),
//                    new FileOutputStream(destPath + "/MyDB"));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
