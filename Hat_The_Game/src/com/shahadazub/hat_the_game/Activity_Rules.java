package com.shahadazub.hat_the_game;

import java.text.BreakIterator;

import com.shahadazub.hat_the_game.R.layout;
import com.shahadazub.hat_the_game.R.string;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class Activity_Rules extends Activity implements TabListener, OnClickListener {

    
    static int StringNumber = 1;
    Intent  intent = new Intent();
    static boolean HardCore = true; 
    static TextView Rules;
    public Button ButBack, ButMenu, ButForward;
    ScrollView LayoutRules;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Rules = (TextView)findViewById(R.id.TextRules);
        ButBack = (Button)findViewById(R.id.ButBack);
        ButMenu = (Button)findViewById(R.id.ButMenu);
        ButForward = (Button)findViewById(R.id.ButForward);
        LayoutRules = (ScrollView)findViewById(R.id.Scroll);
        
        ButBack.setOnClickListener(this);
        ButMenu.setOnClickListener(this);
        ButForward.setOnClickListener(this);
        
        
        
        final ActionBar BarWords = getActionBar();
        
        BarWords.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        BarWords.setDisplayShowHomeEnabled(false);
        
        BarWords.setDisplayShowTitleEnabled(false);
        
        Tab tabadder = BarWords.newTab();
        tabadder.setText("Хардкор");
        tabadder.setTabListener(this);
        BarWords.addTab(tabadder);
        
        tabadder = BarWords.newTab();
        tabadder.setText("Просто");
        tabadder.setTabListener(this);
        BarWords.addTab(tabadder);
        
        TextChange(HardCore,StringNumber);
        
        
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rules, menu);
        return true;
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		switch (tab.getPosition()){
		case 0:
			HardCore = true;
			break;
		case 1:
			HardCore = false;
			break;
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		switch (tab.getPosition()){
		case 0:
			HardCore = true;
			TextChange(HardCore,StringNumber);
			break;
		case 1:
			HardCore = false;
			TextChange(HardCore,StringNumber);
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
			StringNumber = StringNumber - 1;
			TextChange(HardCore,StringNumber);
			break;
		case R.id.ButForward:
			StringNumber = StringNumber + 1;
			TextChange(HardCore,StringNumber);
			break;
		case R.id.ButMenu:
			intent.setClass(this, ActivityMainMenu.class);
			startActivity(intent);
			break;
		}
	}
	
	
	public void TextChange (boolean h, int s){
		if (h){
				switch (s){
					case 1:
						Rules.setText(R.string.Rules_names_need);
						ButForward.setEnabled(true);
						ButBack.setEnabled(false);
						break;
					case 2:
						Rules.setText(R.string.Rules_names_prepare);
						ButForward.setEnabled(true);
						ButBack.setEnabled(true);
						break;
					case 3:
						Rules.setText(R.string.Rules_names_game);
						ButForward.setEnabled(false);
						ButBack.setEnabled(true);
						break;		
				}
				
		}else{
				switch (s){
				case 1:
					Rules.setText(R.string.Rules_words_need);
					ButForward.setEnabled(true);
					ButBack.setEnabled(false);
					break;
				case 2:
					Rules.setText(R.string.Rules_words_prepare);
					ButForward.setEnabled(true);
					ButBack.setEnabled(true);
					break;
				case 3:
					Rules.setText(R.string.Rules_words_game);
					ButForward.setEnabled(false);
					ButBack.setEnabled(true);
					break;		
				}
				
		}
		LayoutRules.scrollTo(0, 0);
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
