package com.gra.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gra.R;
import com.gra.options.Options;
import com.gra.zapisy.SaveContainer;
import com.gra.zapisy.SaveService;
import com.gra.zapisy.UserSettings;

public class MainMenu extends Activity {
	
	private TextView score;
	private SaveService saver;
	SaveContainer savedstate;
	UserSettings settings;
	private boolean canresume = false;
	private boolean canUpdate = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.mainmenu);

        readState();
        Log.d("MainMenu","tworzenie menu");
        
        //grupa wyboru punktow
        RadioGroup radioButtons = (RadioGroup) findViewById(R.id.mainMenu_radioButtons);
        //zmiana wybranego progu punktowego z ktorym gracz startuje
        radioButtons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
		        case R.id.mainMenu_radioButton1:
		        	GameStart.points = 0;
		        	break;
		        case R.id.mainMenu_radioButton2:
		        	GameStart.points = 1000;
		        	break;
		        case R.id.mainMenu_radioButton3:
		        	GameStart.points = 10000;
		        	break;
		        }
			}
		});
        
        CheckBox music = (CheckBox) findViewById(R.id.music);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

     	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
     		   if(isChecked) settings.setMusic(false);
     		   else settings.setMusic(true);
     	   }
     	});
        
        CheckBox fx = (CheckBox) findViewById(R.id.fx);
        fx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

     	   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
     		   if(isChecked) settings.setFx(false);
     		   else settings.setFx(true);
     	   }
     	});
        
        TextView scoreTitle = (TextView) findViewById(R.id.ScoreTitle);
        
        score = (TextView) findViewById(R.id.Score);
        score.setTextColor(Color.GREEN);
        score.setText("0");
        if(settings != null){
        	score.setText(Long.toString(settings.getScore()));
        }
        
        canUpdate = true;
        
        score.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        scoreTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        
        Button ShowOptions = (Button)findViewById(R.id.Options);
        ShowOptions.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent ShowOptionsIntent = new Intent(MainMenu.this,Options.class);
				startActivity(ShowOptionsIntent);
			}
		});
        
        Button showScoreButton = (Button)findViewById(R.id.mainmenu_highscore);
        showScoreButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent showScoreIntent = new Intent(MainMenu.this,HighScoreMenu.class);
				startActivity(showScoreIntent);
			}
		});
        
        Button PlayGameButton = (Button)findViewById(R.id.Resume);
        PlayGameButton.setOnClickListener(new OnClickListener() {
        	
        	//@Override
			public void onClick(View v) {
        		Intent PlayGameIntent = new Intent(MainMenu.this,GameStart.class);
        		if (canresume) {
        			PlayGameIntent.putExtra("RESUME", true);
        			PlayGameIntent.putExtra("SAVE", savedstate);
        			Log.d("MainMenu","save przekazany");
        		}
        		else {
        			PlayGameIntent.putExtra("RESUME", false);
        			Log.d("MainMenu","nie ma sava");
        		}
        		//passing the settings to the main game screen
        		PlayGameIntent.putExtra("SETTINGS", settings);

        		startActivity(PlayGameIntent);
        	}
        });
  
        Button startNew = (Button)findViewById(R.id.StartNew);
        startNew.setOnClickListener(new OnClickListener() {
        	
        	//@Override
			public void onClick(View v) {
        		Intent NewGameIntent = new Intent(MainMenu.this,GameStart.class);
        		NewGameIntent.putExtra("RESUME", false);
        		NewGameIntent.putExtra("SETTINGS", settings);
        		startActivity(NewGameIntent);
        	}
        });
    }

	private void readState() {
		saver = new SaveService(MainMenu.this);
		saver.existing();
	    savedstate = saver.readLastState();
	    settings = saver.readSettings("user_settings");
	//    if (settings == null) settings = new UserSettings(); //in case it's a first game or something -> przeniesione do gameview
	    canresume = (savedstate!=null);
	    if(settings != null && canUpdate){ //dodane zeby sie aktualizowal hiscore przy wyjsciu do menu
        	score.setText(Long.toString(settings.getScore()));
        	Log.d("MainMenu","hiscore: " + Long.toString(settings.getScore()));
        }
	}
	
	protected void onResume() {
		super.onResume();
		readState();
		Log.d("MainMenu","resume menu");
	}
}