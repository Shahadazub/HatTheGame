package com.shahadazub.hat_the_game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.util.Random;

import com.shahadazub.hat_the_game.R;

import android.R.array;
import android.R.integer;
import android.os.Bundle;
import android.os.Looper;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


@TargetApi(11)
public class ActivityWords extends Activity implements ActionBar.TabListener, OnClickListener {

public Boolean HardCore;
TextView Text;
public Intent intent = new Intent();
public EditText Edit1, Edit2, Edit3, Edit4, Edit5;
DBHelper dbhelper;
SQLiteDatabase db;
public String table;
String LOG_TAG = "myLog";

public Cursor c;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
                      
        setContentView(R.layout.words);
        
        dbhelper = new DBHelper(this);
		
		try{
        	dbhelper.createDB();
        	
        }catch (IOException ioe){
        	throw new Error("Unable to create database");
        }
        		       
        try{
        	dbhelper.openDB(db);
        	
        }catch (SQLiteException sqle){
        	throw sqle;
        }
        
        Edit1 = (EditText)findViewById(R.id.EditWord1);
        Edit2 = (EditText)findViewById(R.id.EditWord2);
        Edit3 = (EditText)findViewById(R.id.EditWord3);
        Edit4 = (EditText)findViewById(R.id.EditWord4);
        Edit5 = (EditText)findViewById(R.id.EditWord5);
        
              
        final ActionBar BarWords = getActionBar();
        
        BarWords.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        BarWords.setDisplayShowHomeEnabled(false);
        
        BarWords.setDisplayShowTitleEnabled(false);
        
        Tab tabadder = BarWords.newTab();
        tabadder.setText("Слова");
        tabadder.setTabListener(this);
        BarWords.addTab(tabadder);
        
        tabadder = BarWords.newTab();
        tabadder.setText("Имена");
        tabadder.setTabListener(this);
        BarWords.addTab(tabadder);
        
        
        
		
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.words, menu);
        return true;
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		switch (tab.getPosition()){
		case 1:						
			table = "Names";
			GetWords(table);			
			break;
		case 0:
			table = "Words";
			GetWords(table);			
			break;
		}		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub		
		switch (tab.getPosition()){
		case 1:						
			table = "Names";
			GetWords(table);			
			break;
		case 0:
			table = "Words";
			GetWords(table);			
			break;
		}
		
			
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.ButBack:
			intent.setClass(this, ActivityMainMenu.class);
    		startActivity(intent);
			break;
		case R.id.ButNewWords:						
	        GetWords(table);			
        break;
		}
	}
	
	public void GetWords(String table){
					
					
        SQLiteDatabase str = dbhelper.getWritableDatabase();
		Log.d("myLog", "Here we work");
        
        
		c = str.query(table, null, null, null, null, null, null);
		Log.d("myLog", "And here we are not");
		
	
		int f = c.getCount();
		
		
        
        int ToEditText = c.getColumnIndex("ToEditText");
        
        GetRandomString(c, f);
            
        Edit1.setText(c.getString(ToEditText));
        
        GetRandomString(c, f);
                
        Edit2.setText(c.getString(ToEditText));
        
        GetRandomString(c, f);
        
        Edit3.setText(c.getString(ToEditText));
        
        GetRandomString(c, f);         
        
        Edit4.setText(c.getString(ToEditText));
        
        GetRandomString(c, f);         
            
        Edit5.setText(c.getString(ToEditText));
        
        
        
	}
	
	public void GetRandomString (Cursor c, int f){
		
		
		c.moveToFirst();
        int i = 1;  
        
        int rand = new Random().nextInt(f-1) + 1;
        
        while (i<rand) {
			c.moveToNext();
            i = i + 1;           
		}
        
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public class DBHelper extends SQLiteOpenHelper{
		private static final String DB_PATH = "/data/data/com.shahadazub.hat_the_game/databases/";
		private static final String DB_NAME = "NamesAndWords.sdb";
		private SQLiteDatabase db;
		private final Context myContext;
 		
		
		public DBHelper (Context context){
			super(context, DB_NAME, null, 1);
			this.myContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
		public void createDB() throws IOException{
			boolean dbExist = checkDB();
			
			
			
			if (dbExist){
				
			}else {
				
				this.getWritableDatabase();
				try {
					copyDB();
				}catch (IOException e){
					throw new Error("errpr cppying DataBase" + e);
				}
				
			}
		}
		
		
		private boolean checkDB(){
			SQLiteDatabase checkdatabase = null;
			
			try{
				String myPath = DB_PATH + DB_NAME;
				checkdatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			}catch (SQLiteException e){
				
			}
			
			if (checkdatabase != null){
				
				checkdatabase.close();
			
			}
			
			return checkdatabase !=null ? true : false;
		}
		
		private void copyDB() throws IOException{
			
			
			
			InputStream myInput = myContext.getAssets().open(DB_NAME);
			
			String outFileName = DB_PATH + DB_NAME;
			
			OutputStream myOutput = new FileOutputStream(outFileName);
			
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0){
				myOutput.write(buffer, 0, length);			
			}
			
			myOutput.flush();
			myOutput.close();
			myInput.close();
			
		}
		
		public void openDB(SQLiteDatabase db) throws SQLiteException{
			
			String myPath = DB_PATH + DB_NAME;
			db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			
		}
		
		public synchronized void close(){
			
			if (db != null)
				db.close();
			super.close();
			
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent();
		switch (item.getItemId()){
		case com.shahadazub.hat_the_game.R.id.NewGame:
			
	        intent.setClass(this, ActivityCountPlayers.class);
	        startActivity(intent);
	        break;
		case com.shahadazub.hat_the_game.R.id.ToMainMenu:
			
	        intent.setClass(this, ActivityMainMenu.class);
	        startActivity(intent);
	        break;
		
		}
		
		return super.onOptionsItemSelected(item);
		
	}
}
