package com.shahadazub.hat_the_game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import com.shahadazub.hat_the_game.R;
import com.shahadazub.hat_the_game.ActivityWords.DBHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ActivityTeams extends Activity implements OnClickListener {
	
	SharedPreferences PlayersNames;
	SharedPreferences WordsForGame;
	SharedPreferences WordsToGuess;
	SharedPreferences Points;
	SharedPreferences Settings;
	int NumberOfPlayers;
	int NumberOfTeams;
	String[] Players;
	String LOG_TAG = "MyLog";
	int[] CreatedRnd;
	TableLayout Scroll;
	String PlayerString = "Player_";
	DBHelper dbhelper;
	SQLiteDatabase db;
	public String table = "Words";
	public Cursor coursor;
	Context context;
	int NumberOfWordsToGuessPerPlayer = 5;
	Boolean GuessWords;
	int Time, Round;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        
        context = ActivityTeams.this;
        
        Scroll = (TableLayout)findViewById(R.id.MainTableTeams);
        
        GetPrefs();
        
        
        
        
        
        Players = new String[NumberOfPlayers];
        
        CreatedRnd = new int[NumberOfPlayers];
        
        for (int i = 0; i < NumberOfPlayers; i++) {
        	
        	CreatedRnd[i] = 555;
			Players[i] = PlayersNames.getString(PlayerString + String.valueOf(i), "Que conyo que no existe");
		}
                
        RandomizePlayers();
        
        // НЕ ЗАБУДЬ ЭТО УДАЛИТЬ
        for (int i = 0; i < NumberOfPlayers; i++) {
        	
        	Log.d(LOG_TAG, "CreatedRnd number " + i + " and it is " + CreatedRnd[i]);
			
		}
        
        
        BuildTheScreen();
        Log.d(LOG_TAG, "Screen is built");
        
        dbhelper = new DBHelper(this);
        Log.d(LOG_TAG, "Dbhelper is created");
		
		try{
			Log.d(LOG_TAG, "Try to created DB");
        	dbhelper.createDB();
        	Log.d(LOG_TAG, "Db created");
        }catch (IOException ioe){
        	throw new Error("Unable to create database");
        }
        		       
        try{
        	Log.d(LOG_TAG, "Try to open DB");
        	dbhelper.openDB();
        	Log.d(LOG_TAG, "DB opened");
        	
        }catch (SQLiteException sqle){
        	throw sqle;
        }
        
        
        Log.d(LOG_TAG, "trying to get words");
        GetWords();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teams, menu);
        return true;
    }
    
    
    public void RandomizePlayers(){
    	
    	int rnd;
    	
    	Editor ed = PlayersNames.edit();		
		Log.d(LOG_TAG, "Editor created");
    	
    	for (int i = 0; i < NumberOfPlayers; i++) {
    		
    		boolean Exist = false;
			
    		do {
				rnd = new Random().nextInt(NumberOfPlayers);
				Log.d(LOG_TAG, "Random int is = " + rnd);
				Exist = false;
				for (int j = 0; j < i; j++) {
					
					if (rnd == CreatedRnd[j]){
						
						Exist = true;
						Log.d(LOG_TAG, rnd + " is  exist = "+ Exist + " and its number is " + j);
					}
						
					
				}
    		
			} while (Exist == true);
    		
    		CreatedRnd[i] = rnd;
    		Log.d(LOG_TAG, rnd + " putted as a " + i + " number");
    		ed.putString(PlayerString + String.valueOf(rnd), Players[i]);
			Log.d(LOG_TAG, "one string puted " + PlayerString + String.valueOf(rnd) + " " + Players[i]);
			ed.commit();
    		
    		
		}
    	
    	
    	
    }
    
    
    public void BuildTheScreen(){
    	
    	NumberOfTeams = NumberOfPlayers / 2;
    	
    	Scroll.removeAllViews();
    	Log.d(LOG_TAG, "we removed all rows");
    	for (int i = 0; i < NumberOfTeams; i++) {
    		
    		int Calculus = ((i+1)*2)-1;
			Log.d(LOG_TAG, "CAlculus = " + Calculus);
			
			TableRow NewRow = new TableRow(this);
			NewRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			Log.d(LOG_TAG, "TableRow created and got Params");			
			NewRow.setGravity(Gravity.CENTER_VERTICAL);			
			NewRow.setId(100 + i);
			
			
			
			
				TableLayout NewTable1 = new TableLayout(this);
	    		NewTable1.setLayoutParams(new LayoutParams(0, android.widget.TableLayout.LayoutParams.MATCH_PARENT, 1));
	    		Log.d(LOG_TAG, "NewTable1 created");
	    		
			
				
				TableLayout NewTable2 = new TableLayout(this);
	    		NewTable2.setLayoutParams(new LayoutParams(0, android.widget.TableLayout.LayoutParams.MATCH_PARENT, 4));
	    		Log.d(LOG_TAG, "NewTable2 created");  		
	    		
	    		
	    		
	    		
			    		TableRow NewRow21 = new TableRow(this);
		    			NewRow21.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
		    			Log.d(LOG_TAG, "NewRow21 created");
		    			
		    			
	    		
					    		TextView NewText21 = new TextView(this);
								NewText21.setText("");
								NewText21.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
								Log.d(LOG_TAG, "first text created but not added");						
								NewRow21.addView(NewText21);
								
				    			
				    			EditText NewText23 = new EditText(this);
				    			NewText23.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				    			NewText23.setId(200 + i);				    			
				    			NewText23.setEnabled(false);
				    			NewText23.setText(PlayersNames.getString(PlayerString + String.valueOf(((i+1)*2)-1), "Que conyo que no existe"));
				    			Log.d(LOG_TAG, "NewText21 created and text is " + PlayersNames.getString("Player_" + String.valueOf(((i+1)*2) - 1), "Que conyo que no existe") + " because i = " + i + " and (i+1)*2-1 = " + (((i+1)*2)-1));
				    			NewText23.setFocusable(false);
				    			
				    			
				    			
				    			EditText NewText24 = new EditText(this);
				    			NewText24.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				    			Log.d(LOG_TAG, "NewText22 View builded");
				    			NewText24.setId(300 + i);		    			
				    			Log.d(LOG_TAG, "NewText22 id set");
				    			NewText24.setEnabled(false);
				    			Log.d(LOG_TAG, "NewText22 enabling set");
				    			NewText24.setText(PlayersNames.getString(PlayerString + String.valueOf(((i+1)*2)-2), "Que conyo que no existe"));
				    			Log.d(LOG_TAG, "NewText22 text set");
				    			Log.d(LOG_TAG, "NewText22 created");
				    			NewText24.setFocusable(false);
				    			
				    			
				    			TextView NewText22 = new TextView(this);
								NewText22.setText("");
								NewText22.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
								Log.d(LOG_TAG, "first text created but not added"); 
						
		    			
		    			TableRow NewRow22 = new TableRow(this);
		    			NewRow22.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1));
		    			Log.d(LOG_TAG, "NewRow22 created");
		    			
		    			NewRow22.addView(NewText22);
		    			
		    			
		    			
			    		NewTable2.addView(NewRow21);
			    		NewTable2.addView(NewText23);
			    		NewTable2.addView(NewText24);
			    		NewTable2.addView(NewRow22);
			    		Log.d(LOG_TAG, "Table 2 filled in");
	    		
	    		
	    		
	    		
	    		TableLayout NewTable3 = new TableLayout(this);
	    		NewTable3.setLayoutParams(new LayoutParams(0, android.widget.TableLayout.LayoutParams.MATCH_PARENT, 1));
	    		Log.d(LOG_TAG, "NewTable3 created");
	    		
	    		
	    		TableLayout NewTable4 = new TableLayout(this);
	    		NewTable4.setLayoutParams(new LayoutParams(0, android.widget.TableLayout.LayoutParams.MATCH_PARENT, 4));
	    		NewTable4.setGravity(Gravity.CENTER_VERTICAL);
	    		Log.d(LOG_TAG, "NewTable4 created");
	    		
	    		
		    			
		    			EditText NewText41 = new EditText(this);
		    			NewText41.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		    			NewText41.setId(400 + i);
		    			
		    			NewText41.setHint("Команда" + String.valueOf(i+1));
		    			
		    			
		    			Log.d(LOG_TAG, "NewText41 created");
		    			
		    			
		    			
		    		
		    		NewTable4.addView(NewText41);
		    		
		    		Log.d(LOG_TAG, "Table 4 filled in");
	    		
			
			NewRow.addView(NewTable1);
			NewRow.addView(NewTable2);
			NewRow.addView(NewTable3);
			NewRow.addView(NewTable4);
			
			
			Scroll.addView(NewRow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			Log.d(LOG_TAG, "TableRow added to the Scroll");
			
				
		}
    	
    	TableRow ButRow = new TableRow(this);
		ButRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow created and got Params");
		
		ButRow.setGravity(Gravity.CENTER_HORIZONTAL);
				
				Button ButGo = new Button(this);
				ButGo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				ButGo.setText("Начать игру");
				ButGo.setId(54323);
				ButGo.setOnClickListener(this);
				
		ButRow.addView(ButGo);
		Scroll.addView(ButRow);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Editor ed = PlayersNames.edit();
		Editor edPoints = Points.edit();
		
		ed.clear();
		
		for (int j = 0; j < NumberOfTeams; j++) {
			
			EditText FirstPlayer = (EditText)findViewById(200 + j);
			EditText SecondPlayer = (EditText)findViewById(300 + j);
			EditText TeamName = (EditText)findViewById(400 + j);
			
			if (0 == TeamName.length()){
				Alert("Вы не ввели название команды");
				return;
			}
		
		ed.putString(PlayerString + String.valueOf(0) +"_Team_" + String.valueOf(j), FirstPlayer.getText().toString());
		ed.commit();
		ed.putString(PlayerString + String.valueOf(1) +"_Team_" + String.valueOf(j), SecondPlayer.getText().toString());
		ed.commit();
		ed.putString("Team_" + String.valueOf(j), TeamName.getText().toString());
		ed.commit();
		
		edPoints.putInt("Team_" + String.valueOf(j), 0);
		edPoints.commit();
		
		
		}
		
		ed.putInt("NumberOfPlayers", NumberOfPlayers);
		ed.commit();
		ed.putInt("NumberOfTeams", NumberOfTeams);
		ed.commit();
		ed.putInt("NumberOfTurn", 0);
		ed.commit();
		ed.putInt("NumberOfWordsToGuess", NumberOfPlayers * NumberOfWordsToGuessPerPlayer);
		ed.commit();
		ed.putInt("Round", 1);
		ed.commit();
		
		
		
		Intent intent = new Intent();
		intent.setClass(this, ActivityPlay.class);
		startActivity(intent);
		
	}
	
	public void GetWords(){
		
		SQLiteDatabase str = dbhelper.getWritableDatabase();
		Log.d(LOG_TAG, "Db geted "+ str.getPath() + " " + table);
		
			
		Cursor c = str.query(table, null, null, null, null, null, null);	
		Log.d(LOG_TAG, "Cursor created");
		int f = c.getCount();	
		Log.d(LOG_TAG, "cursor got the last row");
        int ToEditText = c.getColumnIndex("ToEditText");
        
        Editor ed = WordsForGame.edit();
        Editor ed2 = WordsToGuess.edit();
        ed.clear();
        ed2.clear();
        Log.d(LOG_TAG, "Editor created");
        
        for (int h = 0; h < (NumberOfPlayers * NumberOfWordsToGuessPerPlayer); h++) {
			
		        GetRandomString(c, f);
                ed.putString("Word_" + String.valueOf(h), c.getString(ToEditText));
                ed2.putString("Word_" + String.valueOf(h), c.getString(ToEditText));
                ed.commit();
                ed2.commit();
                Log.d(LOG_TAG, " the word is " + c.getString(ToEditText) + " and the triger is " + "Word_" + String.valueOf(h) );
        }
		
	}
	
	
	public void GetRandomString (Cursor c, int f){
		
		Log.d(LOG_TAG, "Random word begun");
		c.moveToFirst();
        int i = 1;  
        
        int rand = new Random().nextInt(f-1) + 1;
        
        while (i<rand) {
			c.moveToNext();
            i++;           
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
			Log.d(LOG_TAG, "Db cheked");
			
			
			
			if (dbExist){
				Log.d(LOG_TAG, "Db exist");				
			}else {
				
				this.getWritableDatabase();
				Log.d(LOG_TAG, "getting writable database");
				try {
					Log.d(LOG_TAG, "Starting to copy DB");
					copyDB();
					Log.d(LOG_TAG, "DB is copied");
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
			
			
			Log.d(LOG_TAG, "Start copying");
			InputStream myInput = myContext.getAssets().open(DB_NAME);
			Log.d(LOG_TAG, "Input file got");
			
			String outFileName = DB_PATH + DB_NAME;
			Log.d(LOG_TAG, "Output file got");
			
			OutputStream myOutput = new FileOutputStream(outFileName);
			Log.d(LOG_TAG, "Output stream got");
			
			byte[] buffer = new byte[1024];
			Log.d(LOG_TAG, "Byte  arr created");
			int length;
			while ((length = myInput.read(buffer)) > 0){
				Log.d(LOG_TAG, "Transmitting bytes");
				myOutput.write(buffer, 0, length);			
			}
			
			myOutput.flush();
			myOutput.close();
			myInput.close();
			
		}
		
		public void openDB() throws SQLiteException{
			
			String myPath = DB_PATH + DB_NAME;
			db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			
		}
		
		public synchronized void close(){
			
			if (db != null)
				db.close();
			super.close();
			
		}
	}
	
	
	public void Alert(String s){
		
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("Ход игры");
		ad.setMessage(s);
		ad.setNeutralButton("Далее", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}); 
		ad.show();
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
	
	
	public void GetPrefs(){
			
		PlayersNames = getSharedPreferences("Players", MODE_PRIVATE);
	    WordsForGame = getSharedPreferences("WordsForGame", MODE_PRIVATE);
	    WordsToGuess = getSharedPreferences("WordsToGuess", MODE_PRIVATE);
	    Points = getSharedPreferences("Points", MODE_PRIVATE);
        Settings = getSharedPreferences("Settings", MODE_PRIVATE);
        
       
        
        NumberOfPlayers = PlayersNames.getInt("NumberOfPlayers", 0);
        Log.d(LOG_TAG, "number of players is = " + NumberOfPlayers);
       
        
        GuessWords = Settings.getBoolean("GuessWords", true);
        Round = PlayersNames.getInt("Round", 0);
        
        if (GuessWords == true) {
        	NumberOfWordsToGuessPerPlayer = Settings.getInt("NumberOfWordsToGuessPerPlayer", 5);
        	table = "Words";
        } else{
        	NumberOfWordsToGuessPerPlayer = 5;
        	table = "Names";
        	
        	
        }
        
	}
	
}
