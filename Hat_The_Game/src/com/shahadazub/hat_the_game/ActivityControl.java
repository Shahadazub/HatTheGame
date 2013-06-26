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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ActivityControl extends Activity implements OnClickListener {
	
	String LOG_TAG = "ControlLog";
	SharedPreferences PrefPlayers;
	SharedPreferences PrefWords;
	SharedPreferences WordsToGuess;
	SharedPreferences WordsGuessed;
	SharedPreferences Points;
	int Player;
	int Team;
	int ControlPlayer;
	int ControlTeam;
	int NumberOfPlayers;
	int NumberOfTeams;
	int NumberOfWordsGuessed;
	int Turn;
	int TeamScore;
	String NowPlayer;
	String NowTeam;
	String NextPlayer;
	String NextTeam;
	String[] Words;
	Context context;
	TableLayout Scroll;
	CheckBox[] NewBox;
	Boolean Final;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.control);
        
        context = ActivityControl.this;
        
        Scroll = (TableLayout)findViewById(R.id.ScrollControl);
        Log.d(LOG_TAG, "We got the Scroll");
        
        GetPrefs();
        Log.d(LOG_TAG, "We got the Prefs");
        
        NewBox = new CheckBox[NumberOfWordsGuessed];
        for (int j = 0; j < NumberOfWordsGuessed; j++) {
			BuildTheScreen(Words[j], j);        
        }
        Log.d(LOG_TAG, "We have built the screen");
        
        AddLastButton();
        Log.d(LOG_TAG, "We have got the Button");
        
        Alert("Передайте телефон игроку " + NextPlayer + " из команды " + NextTeam + " для проверки отгаданных слов");
        
        if (Final){
        	Alert("Все слова закончились!");
        }
    }
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.control, menu);
        return true;
    }
    
    
    public void GetPrefs(){
    	
    	PrefPlayers = getSharedPreferences("Players", MODE_PRIVATE);
        WordsGuessed = getSharedPreferences("WordsGuessed", MODE_PRIVATE);
        Points = getSharedPreferences("Points", MODE_PRIVATE);
        Log.d(LOG_TAG, "We got prefFiles opened");
        
        Final = Points.getBoolean("Final", false);
        
        NumberOfPlayers = PrefPlayers.getInt("NumberOfPlayers", 0);
        NumberOfTeams = PrefPlayers.getInt("NumberOfTeams", 0);
        Turn = PrefPlayers.getInt("NumberOfTurn", 0);
        Log.d(LOG_TAG, "NumberOfPlayers = " + NumberOfPlayers +  " NumberOfTeams = " + NumberOfTeams + " NumberOfTurn = " + Turn );
        
        Team = Turn % NumberOfTeams;
        Player = ( Team * 2 ) + ( ( Turn / NumberOfTeams ) % 2 );
        int NOD = Player % 2; 
        NowPlayer = PrefPlayers.getString("Player_" + String.valueOf(NOD) +"_Team_" + String.valueOf(Team), "Que conyo que no existe");
        Log.d(LOG_TAG, "Player_" + String.valueOf(NOD) +"_Team_" + String.valueOf(Team));
        
        ControlTeam = ( Turn + 1 ) % NumberOfTeams;
        ControlPlayer = ( ControlTeam * 2 ) + ( ( ( Turn + 1 ) / NumberOfTeams ) % 2 );
        int NODControl = ControlPlayer % 2; 
        NextPlayer = PrefPlayers.getString("Player_" + String.valueOf(NODControl) +"_Team_" + String.valueOf(ControlTeam), "Que conyo que no existe");
        NextTeam = PrefPlayers.getString("Team_" + String.valueOf(ControlTeam), "Que conyo que no existe");
        Log.d(LOG_TAG, "NextPlayer_" + String.valueOf(NODControl) +"_Team_" + String.valueOf(ControlTeam));
        
        
        NumberOfWordsGuessed = WordsGuessed.getInt("NumberOfWordsGuessed", 995);
        Words = new String[NumberOfWordsGuessed];
        for (int i = 0; i < NumberOfWordsGuessed; i++) {			
        	Words[i] = WordsGuessed.getString("Word_" + i, "Que conyo que no existe");        	
		}
        
        TeamScore = Points.getInt("Team_" + Team, 991);
        
    }
    
    
    public void BuildTheScreen(String Word, int i){
    	
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
		NewText.setText(Word);
		NewText.setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 7));
		NewText.setId( 200 + i );
		Log.d(LOG_TAG, "second text created but not added");
		
		
		NewBox[i] = new CheckBox(this);
		NewBox[i].setText("");
		NewBox[i].setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 2));
		NewBox[i].setId( 300 + i );
		NewBox[i].setChecked(false);
		NewBox[i].setEnabled(true);
		
		
		NewRow.addView(NewBeforeText);
		NewRow.addView(NewText);
		NewRow.addView(NewBox[i]);
		Log.d(LOG_TAG, "texts are added");
				
		
		
		Scroll.addView(NewRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow added to the Scroll");
		
		
    	
    }
    
    
    
    public void Alert(String s){
		
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("");
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
    
    
    public void AddLastButton(){
    	
    	TableRow ButRow = new TableRow(this);
		ButRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow created and got Params");
		
		ButRow.setGravity(Gravity.CENTER_HORIZONTAL);
				
				Button ButGo = new Button(this);
				ButGo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				ButGo.setText("Подтвердить");
				ButGo.setId(54321);
				ButGo.setOnClickListener(this);
				
		ButRow.addView(ButGo);
		Scroll.addView(ButRow);
    	
    	
    }




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		CorrectTheScore();
		
		ToTheNextTurn();
		
		Intent intent = new Intent();
		intent.setClass(context, ActivityScore.class);
		startActivity(intent);
		
	}
    
	
	public void ToTheNextTurn(){
		
		Editor ed = PrefPlayers.edit();
		Turn++;
		ed.putInt("NumberOfTurn", Turn);
		ed.commit();
		
	}
	
	public void CorrectTheScore(){
		
		for (int i = 0; i < NumberOfWordsGuessed; i++) {
			
			if (NewBox[i].isChecked()) {
				
			}else{
				TeamScore = TeamScore - 2;
				
				Editor ed = Points.edit();
				ed.putInt("Team_" + Team, TeamScore);
				ed.commit();
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
}
