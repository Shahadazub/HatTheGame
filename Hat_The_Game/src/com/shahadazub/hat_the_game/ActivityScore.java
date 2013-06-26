package com.shahadazub.hat_the_game;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ActivityScore extends Activity implements OnClickListener {
	
	String LOG_TAG = "ScoreLog";
	SharedPreferences PrefPlayers;
	SharedPreferences Points;
	SharedPreferences Settings;
	SharedPreferences WordsForGame;
	SharedPreferences WordsToGuess;
	int Player;
	int Team;
	int ControlPlayer;
	int ControlTeam;
	int NumberOfPlayers;
	int NumberOfTeams;
	int NumberOfWordsGuessed;
	int Turn;
	int Round;
	int[] TeamScore;
	int Winner = 543;
	int NumberOfWordsToGuessPerPlayer;
	
	String[] Teams;
	Context context;
	TableLayout Scroll;
	CheckBox[] NewBox;
	TextView TextScore;
	
	Boolean Final;
	Boolean GuessWords;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        
        context = ActivityScore.this;
        
        Scroll = (TableLayout)findViewById(R.id.ScrollScore);
        TextScore = (TextView)findViewById(R.id.TextTitleScore);
        
        GetPrefs();
        Log.d(LOG_TAG, "Prefs got");
        
        for (int i = 0; i < NumberOfTeams; i++) {
			BuildTheScreen(Teams[i], TeamScore[i], i);
		}
        
        AddLastButton();
        
        if (Final){
			TextScore.setText("Победила команда " + Teams[Winner] + " со счетом:");
			TextScore.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		} else {
			TextScore.setText("СЧЕТ:");
			TextScore.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 80);
		}
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.score, menu);
        return true;
    }
    
    
    public void GetPrefs(){
    	int BestScore = -200;
    	
    	PrefPlayers = getSharedPreferences("Players", MODE_PRIVATE);
        Points = getSharedPreferences("Points", MODE_PRIVATE);
        Settings = getSharedPreferences("Settings", MODE_PRIVATE);
        WordsForGame = getSharedPreferences("WordsForGame", MODE_PRIVATE);
	    WordsToGuess = getSharedPreferences("WordsToGuess", MODE_PRIVATE);
        Log.d(LOG_TAG, "We got prefFiles opened");
        
             
        NumberOfTeams = PrefPlayers.getInt("NumberOfTeams", 0);
        NumberOfPlayers = PrefPlayers.getInt("NumberOfPlayers", 0);
        Turn = PrefPlayers.getInt("NumberOfTurn", 0);
        Log.d(LOG_TAG, " NumberOfTeams = " + NumberOfTeams + " NumberOfTurn = " + Turn );
        Round = PrefPlayers.getInt("Round", 0);
        GuessWords = Settings.getBoolean("GuessWords", true);
        Final = Points.getBoolean("Final", false);
        
        
        Teams = new String[NumberOfTeams];
        TeamScore = new int[NumberOfTeams];
        Log.d(LOG_TAG, "Teams and TeamScore");
        for (int i = 0; i < NumberOfTeams; i++) {       
        	Log.d(LOG_TAG, "Cicle started" + PrefPlayers.getString("Team_" + String.valueOf(i), "Que conyo que no existe"));
        	Teams[i] = PrefPlayers.getString("Team_" + String.valueOf(i), "Que conyo que no existe");
        	Log.d(LOG_TAG, "Team_" + String.valueOf(i) + " is " + Teams[i]);
        	TeamScore[i] = Points.getInt("Team_" + String.valueOf(i), 997);
        	Log.d(LOG_TAG, "Score is " + TeamScore[i]);
        	
        	
        	if (TeamScore[i] >  BestScore){
        		BestScore = TeamScore[i];
        		Winner = i;
        	}
        	
		}
        
        
        
    }
    
    
    
    public void BuildTheScreen(String TeamToPut, int Score, int i){
    	
    	TableRow NewRow = new TableRow(this);
		
		
		
		NewRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow created and got Params");
		
		NewRow.setGravity(Gravity.CENTER_VERTICAL);
		NewRow.setId(100 + i);
		
		TextView NewBeforeText = new TextView(this);
		NewBeforeText.setText("");
		NewBeforeText.setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		Log.d(LOG_TAG, "first text created but not added");
		
				
		TextView NewText = new TextView(this);
		NewText.setText(TeamToPut);
		NewText.setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 7));
		NewText.setId( 200 + i );
		NewText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		Log.d(LOG_TAG, "second text created but not added");
		
		
		TextView NewTextScore = new TextView(this);
		NewTextScore.setText(String.valueOf(Score));
		NewTextScore.setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 2));
		NewTextScore.setId( 300 + i );
		NewTextScore.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		Log.d(LOG_TAG, "third text created but not added");
		
		
		
		
		NewRow.addView(NewBeforeText);
		NewRow.addView(NewText);
		NewRow.addView(NewTextScore);
		Log.d(LOG_TAG, "texts are added");
				
		
		
		Scroll.addView(NewRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow added to the Scroll");
		
		
    }
    
    
    public void AddLastButton(){
    	
    	TableRow ButRow = new TableRow(this);
		ButRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow created and got Params");
		
		ButRow.setGravity(Gravity.CENTER_HORIZONTAL);
				
				Button ButGo = new Button(this);
				ButGo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				if (Final & GuessWords){
					ButGo.setText("В меню");
				} else  if (Final & Round == 3){
					ButGo.setText("В меню");
				} else {
					ButGo.setText("Далее");
				}
				ButGo.setId(54322);
				ButGo.setOnClickListener(this);
				
		ButRow.addView(ButGo);
		Scroll.addView(ButRow);
    	
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (GuessWords){
			if (Final){
				Intent intent = new Intent();
				intent.setClass(context, ActivityMainMenu.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(context, ActivityPlay.class);
				startActivity(intent);
			}
		} else {
			if (Final & Round == 3){
				Intent intent = new Intent();
				intent.setClass(context, ActivityMainMenu.class);
				startActivity(intent);
			} else if (Final & Round != 3) {
				
				NextRoundTramites();
								
				
			} else {
				Intent intent = new Intent();
				intent.setClass(context, ActivityPlay.class);
				startActivity(intent);
			}
		}
	}
	
	public void onBackPressed(){
		
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
	
	
	public void NextRoundTramites(){
		
		Editor ed = PrefPlayers.edit();
		Editor ed2 = Points.edit();
		Editor ed3 = WordsToGuess.edit();
		
		Round++;
	    ed.putInt("Round", Round);
		ed.commit();
	    
	    ed2.putBoolean("Final", false);
		ed2.commit();
		
	    if (GuessWords == true) {
        	NumberOfWordsToGuessPerPlayer = Settings.getInt("NumberOfWordsToGuessPerPlayer", 5);
        } else{
        	NumberOfWordsToGuessPerPlayer = 5;
        	}
	    
	    for (int i = 0; i < (NumberOfPlayers * NumberOfWordsToGuessPerPlayer); i++) {
			ed3.putString("Word_" + i, WordsForGame.getString("Word_" + i, "No tengo ni uno de este conyo"));
			ed3.commit();
		}
	    
	    if (Round == 2){
	    	Alert("Теперь персонажей надо показывать, а не рассказывать");
	    } else if (Round == 3){
	    	Alert("Теперь персонажей надо загадывать ОДНИМ словом");
	    }
		
	}
	
	
	public void Alert(String s){
		
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("");
		ad.setMessage(s);
		ad.setPositiveButton("Далее", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ActivityPlay.class);
				startActivity(intent);
				dialog.cancel();
			}
		}); 
		ad.show();
	}
}
