package com.gra.menu;

import com.gra.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;

public class OptionsTab2 extends Activity{
	
	private Gallery player;
	//private Gallery background;
	private Gallery earth;
	
	private ImageView playerImage;
	private ImageView earthImage;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2layout);
        
        //Obrazek gracza
        playerImage = (ImageView) findViewById(R.id.player);
        earthImage = (ImageView) findViewById(R.id.earth);
        
        //Galeria textur gracza
        player = (Gallery) findViewById(R.id.gallery2);
        player.setAdapter(new ImageAdapter(this, 200, 200));
        player.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				playerImage.setImageResource(((ImageAdapter)player.getAdapter()).getImageId(position));
			}
	
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
	
	
		});
        
      //Galeria textur ziemi
        earth = (Gallery) findViewById(R.id.gallery1);
        earth.setAdapter(new ImageAdapter(this, 200, 200));
        earth.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				earthImage.setImageResource(((ImageAdapter)earth.getAdapter()).getImageId(position));
			}
	
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
	
	
		});
    }
}

