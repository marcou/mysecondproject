package com.gra.menu;

import com.gra.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
public class Options extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.options);
 
        TabHost tabHost = getTabHost();
 
        
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("Controlls");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Controlls", getResources().getDrawable(R.drawable.jez));
        Intent photosIntent = new Intent(this, OptionsTab1.class);
        photospec.setContent(photosIntent);
 
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Progress");
        songspec.setIndicator("Progress", getResources().getDrawable(R.drawable.heart));
        Intent songsIntent = new Intent(this, OptionsTab2.class);
        songspec.setContent(songsIntent);
 
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("Other");
        videospec.setIndicator("Others", getResources().getDrawable(R.drawable.moneta));
        Intent videosIntent = new Intent(this, OptionsTab3.class);
        videospec.setContent(videosIntent);
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab
    }
}