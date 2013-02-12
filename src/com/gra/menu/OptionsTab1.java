package com.gra.menu;

import com.gra.R;
import com.gra.zapisy.SaveService;
import com.gra.zapisy.UserSettings;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OptionsTab1 extends Activity{
	
	private ImageView controlls;
	
	//zapis settingsow
	SaveService saver;
	
	UserSettings savedSettings;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1layout);
        
        saver = new SaveService(OptionsTab1.this);
        loadSettings();
        
        controlls = (ImageView) findViewById(R.id.tab1_controllImage);
        
        //przycisk przezroczstych przyciskow
        final CheckBox transparentControlls = (CheckBox) findViewById(R.id.tab1_transparentControlls);
        
        //grupa wyboru sterowania 
        RadioGroup radioButtons = (RadioGroup) findViewById(R.id.radioGroup1);
        
        //sterowanie akcelerometrem
        RadioButton accelerometer = (RadioButton) findViewById(R.id.tab1_accelerometer);
        //sterowanie przyciskami
        RadioButton controllsButtons = (RadioButton) findViewById(R.id.tab1_controllButton);
        
        //zmiana wybranego sterowania
        radioButtons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
		        case R.id.tab1_accelerometer:
		        	transparentControlls.setEnabled(false);
		        	controlls.setImageResource(R.drawable.sterowanie2);
		        	break;
		        case R.id.tab1_controllButton:
		        	transparentControlls.setEnabled(true);
		        	controlls.setImageResource(R.drawable.sterowanie1);
		        	break;
		        }
			}
		});
    }
	
	protected void onPause() {
    	super.onPause();
    	Log.d("OptionsTab", "MYonPause is called");
    	saveState();
	}
	
	

	private void loadSettings() {
		 UserSettings saved = saver.readSettings("user_settings");
		 if (saved==null) {
			 saved = new UserSettings();	
		 }
		 savedSettings=saved;
		 
	
	}
	
	private void saveState() {
        saver.saveSettings(savedSettings, "user_settings");
        
	}
}
