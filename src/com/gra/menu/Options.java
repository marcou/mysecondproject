package com.gra.menu;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.gra.R;
 
public class Options extends TabActivity {
	
	public static UserSettings settings;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.options);
 
        settings = new UserSettings();
        
        
        TabHost tabHost = getTabHost();
 
        TabSpec controlls = tabHost.newTabSpec("Controlls");
        // setting Title and Icon for the Tab
        controlls.setIndicator("Controlls", getResources().getDrawable(R.drawable.jez));
        Intent controllsIntent = new Intent(this, OptionsTab1.class);
        controlls.setContent(controllsIntent);
 
        TabSpec progress = tabHost.newTabSpec("Progress");
        progress.setIndicator("Progress", getResources().getDrawable(R.drawable.heart));
        Intent progressIntent = new Intent(this, OptionsTab2.class);
        progress.setContent(progressIntent);
 
        TabSpec other = tabHost.newTabSpec("Achievements");
        other.setIndicator("Achievements", getResources().getDrawable(R.drawable.moneta));
        Intent otherIntent = new Intent(this, OptionsTab3.class);
        other.setContent(otherIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(controlls); // Adding photos tab
        tabHost.addTab(progress); // Adding songs tab
        tabHost.addTab(other); // Adding videos tab
        
    }
}