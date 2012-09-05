package com.gra.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gra.R;
import com.gra.zapisy.SaveContainer;
import com.gra.zapisy.SaveService;

public class MainMenu extends Activity {
	
	private SaveService saver;
	SaveContainer savedstate;
	private boolean canresume = false;
	
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
       
        
        
        Button PlayGameButton = (Button)findViewById(R.id.Resume);
        
        
        PlayGameButton.setOnClickListener(new OnClickListener() {
        	
        	//@Override
			public void onClick(View v) {
        		Intent PlayGameIntent = new Intent(MainMenu.this,Options.class);
        		if (canresume) {
        			PlayGameIntent.putExtra("RESUME", true);
        			PlayGameIntent.putExtra("SAVE", savedstate);
        			Log.d("MainMenu","save przekazany");
        		}
        		else {
        			PlayGameIntent.putExtra("RESUME", false);
        			Log.d("MainMenu","nie ma sava");
        		}
        			
        		
        		startActivity(PlayGameIntent);
        	}
        });
        
        
        
        Button OptionsButton = (Button)findViewById(R.id.StartNew);
        OptionsButton.setOnClickListener(new OnClickListener() {
        	
        	//@Override
			public void onClick(View v) {
        		Intent NewGameIntent = new Intent(MainMenu.this,Options.class);
        		NewGameIntent.putExtra("RESUME", false);
        		startActivity(NewGameIntent);
        	}
        });
        


 
//        Button CreditsButton = (Button)findViewById(R.id.Credits);
//        CreditsButton.setOnClickListener(new OnClickListener() {
//        	
//        	@Override
//			public void onClick(View v) {
//        		Intent CreditsIntent= new Intent(Menu.this,Credits.class);
//        		startActivity(CreditsIntent);
//        	}
//        });
    }

private void readState() {
	saver = new SaveService(MainMenu.this);
	saver.existing();
    savedstate = saver.readLastState();
    canresume = (savedstate!=null);
	}

protected void onResume() {
	super.onResume();
	readState();
	Log.d("MainMenu","resume menu");
	

}

}