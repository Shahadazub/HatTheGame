package com.shahadazub.hat_the_game;

import com.shahadazub.hat_the_game.R.drawable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.Theme;
import android.net.MailTo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ActivityCountPlayers extends Activity implements OnClickListener {
	
	public String PlayerName;
	EditText EditName;
	TableLayout Scroll;
	LinearLayout.LayoutParams NewRowParams;
	LinearLayout.LayoutParams NewTextParams;
	LinearLayout.LayoutParams NewButParams;
	Button ButAddPlayer;
	Button ButGoNext;
	String LOG_TAG = "myLog";
	int i = 0;
	String DeletedItems = "DeletedPlayerName";
	int j = 0;
	SharedPreferences SavedPlayers;
	String PlayerString = "Player_";
	String[] PlayerNames = new String[100];
	String MOD = "Количество игроков должно быть четным";
	String Quatro = "Количество игроков должно быть не меньше 4";
	AlertDialog.Builder ad;
	Context context;

	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	  	
    	
    	super.onCreate(savedInstanceState);
               
        
        setContentView(R.layout.activity_count_players);
        
        context = ActivityCountPlayers.this;
        
        ButAddPlayer = (Button)findViewById(R.id.ButAddPlayer);
        ButAddPlayer.setOnClickListener(this);
        
        ButGoNext = (Button)findViewById(R.id.ButToTeams);
        ButGoNext.setOnClickListener(this);
        
        EditName = (EditText)findViewById(R.id.EditPlayerName);
        Scroll = (TableLayout)findViewById(R.id.ScrollLayout);
        Scroll.removeAllViews();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_count_players, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		if (v.getId() == R.id.ButAddPlayer)  {
			
			Log.d(LOG_TAG, "AddPlayer started");
			AddPlayer();
			
		}else if (v.getId() == R.id.ButToTeams) {
			
			Log.d(LOG_TAG, "We begin to save names");
			SavedPlayers = getSharedPreferences("Players", MODE_PRIVATE);
			
			Log.d(LOG_TAG, "Preferences created");
			Editor ed = SavedPlayers.edit();
			ed.clear();
			int counter = 0;
			Log.d(LOG_TAG, "Editor created");
			
			for (int sp = 0; sp < i; sp++) {
				Log.d(LOG_TAG, "We looped sp = " + sp);
				if (PlayerNames[sp] != DeletedItems){			
					
					
					ed.putString(PlayerString + String.valueOf(counter), PlayerNames[sp]);
					Log.d(LOG_TAG, "one string puted " + PlayerString + String.valueOf(counter) + " " + PlayerNames[sp]);
					ed.commit();
					counter++;
				
				}
				
			}
			
			
			if (counter%2 != 0){
				Alert(MOD);
				return;
			}else if (counter < 4){
				Alert(Quatro);
				return;
			}
			ed.putInt("NumberOfPlayers", counter);
			ed.commit();
			
			Intent intent = new Intent();
			intent.setClass(this, ActivityTeams.class);
			startActivity(intent);
			
			
		}else{
			int DynamicId = 100 + v.getId() - 300;
			Log.d(LOG_TAG, "pushed id is " + DynamicId);
			TableRow RemRow = (TableRow)findViewById(DynamicId);
			Log.d(LOG_TAG, "We found that Button");
			Scroll.removeView(RemRow);
			Log.d(LOG_TAG, "We deleted it");
			PlayerNames[v.getId() - 300] = DeletedItems;
			
			
			
		}
			
		
		
			
		
				
		
	}
	
	public void AddPlayer (){
		
		PlayerName = EditName.getText().toString();
		Log.d(LOG_TAG, "Player name is got from EditText  strat" + PlayerName + "stop" );
		
		if (EditName.length() == 0){
			Log.d(LOG_TAG, "I call the alert");
			Alert("Вы не ввели имя игрока");
			return;
		}
			
		TableRow NewRow = new TableRow(this);
		
		
		
		NewRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow created and got Params");
		
		NewRow.setGravity(Gravity.CENTER_VERTICAL);
		NewRow.setId(100 + i);
		
		TextView NewBeforeText = new TextView(this);
		NewBeforeText.setText("");
		NewBeforeText.setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));
		
		Log.d(LOG_TAG, "first text created but not added");
		
		
		
		TextView NewText = new TextView(this);
		NewText.setText(PlayerName);
		NewText.setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 7));
		NewText.setId( 200 + i );
		Log.d(LOG_TAG, "second text created but not added");
		
		
		
		ImageButton NewBut = new ImageButton(this);		
		NewBut.setImageDrawable(getResources().getDrawable(R.drawable.deleteplayer));
		NewBut.setLayoutParams(new LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 2));
		NewBut.setId( 300 + i );
		NewBut.setOnClickListener(this);
		NewBut.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		
		
		NewRow.addView(NewBeforeText);
		NewRow.addView(NewText);
		NewRow.addView(NewBut);
		Log.d(LOG_TAG, "texts are added");
				
		
		
		Scroll.addView(NewRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Log.d(LOG_TAG, "TableRow added to the Scroll");
		
		
		Log.d(LOG_TAG, "Edit Name is now blanck");
		
		PlayerNames[i] = PlayerName;
		Log.d(LOG_TAG, "Array got a name");
		
		EditName.setText("");
		EditName.setHint("Следующий игрок");
		
		i++;
				
		
	}
	
	
	
	public void Alert(String s){
		
		ad = new AlertDialog.Builder(context);
		ad.setTitle("");
		ad.setMessage(s);
		ad.setPositiveButton("Назад", new DialogInterface.OnClickListener() {
			
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
}
