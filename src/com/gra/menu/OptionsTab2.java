package com.gra.menu;



import com.gra.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class OptionsTab2 extends Activity{
	
	private Gallery player;
	//private Gallery background;
	private Gallery earth;
	
	private ImageView playerImage;
	private ImageView earthImage;
	
	private ProgressBar playerSpeed;
	private ProgressBar playerLife;
	private ProgressBar playerUpgrade;
	
	private ProgressBar earthGravity;
	private ProgressBar earthSize;
	
	private int[][] playerStats = {{1,1,1},{2,1,1},{1,3,1},{2,1,3},{5,1,1},{3,4,1}};
	
	private int[][] earthStats = {{1,1},{2,1},{1,3},{2,1},{4,1},{3,3}};
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2layout);
        
        //Wlasciwosci wybranej postaci
        playerSpeed = (ProgressBar) findViewById(R.id.playerSpeedProgressBar);
        playerLife = (ProgressBar) findViewById(R.id.playerLifeProgressBar);
        playerUpgrade = (ProgressBar) findViewById(R.id.playerUpgradeProgressBar);
        
        playerSpeed.setMax(5);
        playerLife.setMax(5);
        playerUpgrade.setMax(5);
        
        //Wlasciwosci wybranej planety
        earthGravity = (ProgressBar) findViewById(R.id.earthGravityProgressBar);
        earthSize = (ProgressBar) findViewById(R.id.earthSizeProgressBar);
        
        earthGravity.setMax(5);
        earthSize.setMax(5);
        
        //Obrazek gracza
        playerImage = (ImageView) findViewById(R.id.player);
        earthImage = (ImageView) findViewById(R.id.earth);
        
        //Galeria textur gracza
        player = (Gallery) findViewById(R.id.gallery2);
        player.setAdapter(new ImageAdapter(this, 100, 100, R.drawable.jez1, R.drawable.jez2, R.drawable.jez3, R.drawable.jez4, R.drawable.moneta, R.drawable.moneta));
        player.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				playerImage.setImageResource(((ImageAdapter)player.getAdapter()).getImageId(position));
				playerSpeed.setProgress(playerStats[position][0]);
				playerLife.setProgress(playerStats[position][1]);
				playerUpgrade.setProgress(playerStats[position][2]);
			}
	
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
	
	
		});
      //Galeria textur ziemi
        earth = (Gallery) findViewById(R.id.gallery1);
        earth.setAdapter(new ImageAdapter(this, 100, 100, R.drawable.ziemia1, R.drawable.ziemia2, R.drawable.ziemia3, R.drawable.moneta));
        earth.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				earthImage.setImageResource(((ImageAdapter)earth.getAdapter()).getImageId(position));
				earthGravity.setProgress(earthStats[position][0]);
				earthSize.setProgress(earthStats[position][1]);
			}
	
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
	
	
		});
    }
}

