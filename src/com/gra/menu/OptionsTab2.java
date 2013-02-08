package com.gra.menu;



import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gra.R;
import com.gra.zapisy.SaveService;

public class OptionsTab2 extends Activity{
	
	//zapis settingsow
	SaveService saver;
	
	UserSettings savedSettings;
	
	//pozycja w galerii ziemi
	private int earthPosition;
	//pozycja w galerii gracza
	private int playerPosition;
	//galeria tekstur gracza
	private Gallery player;
	//galeria tekstur planety
	private Gallery earth;

	//obrazki gracza i planety
	private ImageView playerImage;
	private ImageView earthImage;
	
	//paski zzawierajace wlasciwosci gracza (MAX 5)
	private ProgressBar playerSpeed;
	private ProgressBar playerLife;
	private ProgressBar playerUpgrade;
	
	//paski zaweirajace wlasciwosci ziemi (MAX 5)
	private ProgressBar earthGravity;
	private ProgressBar earthSize;
	
	//pasek postepu gracza w grze
	private ProgressBar progress;
	
	//statystyki postaci 	|SPEED|LIFE|UPGRADE|
	private int[][] playerStats;
	//statystyki planety	|GRAVITY|SIZE|
	private int[][] earthStats;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2layout);
        
        saver = new SaveService(OptionsTab2.this);
        loadSettings();
        
        //postep gracza
        progress = (ProgressBar) findViewById(R.id.tab1_playerProgress);
        progress.setMax(Options.settings.getMaxProgress());
        setValue(progress, Options.settings.getProgress(), progress.getMax());
        
        
        
        //lista grafik wraz z wlasciwosciami
        playerStats = Options.settings.getImages(true);
        earthStats = Options.settings.getImages(false);
        
        //wybrana planeta oraz postac na podstawie poprzednich ustawien
//        earthPosition = Options.settings.getEarth();
//        playerPosition = Options.settings.getCharacter();
        
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
        
        //tworzenie adaptera dla obrazkow jeza
        ImageAdapter playerAdapter = new ImageAdapter(this, 100, 100);
        //czyscimy tablice i zwiekszamy ja do rozmiarow odpowiadajacych liczbie obrazkow z settingsa dla danego progu punktowego
        playerAdapter.clearImageIds(playerStats.length);
        for(int i = 0; i < playerStats.length; i++){
        	//zapelnianie tablicy obrazkami
        	playerAdapter.addImage(i, playerStats[i][0]);
        }
        
        //podpiecie adaptera pod galerie
        player.setAdapter(playerAdapter);
        //ustawienie galeri na pozycji ktora wybral gracz
        player.setSelection(playerPosition);
        player.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				//playerImage.setImageResource(((ImageAdapter)player.getAdapter()).getImageId(position));
				playerImage.setImageResource(playerStats[position][0]);
				playerSpeed.setProgress(playerStats[position][1]);
				playerLife.setProgress(playerStats[position][2]);
				playerUpgrade.setProgress(playerStats[position][3]);
				playerPosition = position;
			}
	
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
        
        //Galeria textur ziemi
        earth = (Gallery) findViewById(R.id.gallery1);
        
        ImageAdapter earthAdapter = new ImageAdapter(this, 100, 100);
        //czyscimy tablice i zwiekszamy ja do rozmiarow odpowiadajacych liczbie obrazkow z settingsa dla danego progu punktowego
        earthAdapter.clearImageIds(earthStats.length);
        for(int i = 0; i < earthStats.length; i++){
        	//dodajemy nowe obrazk ido adapter
        	earthAdapter.addImage(i, earthStats[i][0]);
        }
        
        //podpeicie adaptera do galerii planet
        earth.setAdapter(earthAdapter);//, R.drawable.ziemia1, R.drawable.ziemia2, R.drawable.ziemia3));
        
        //ustawienie na wybrana przez gracza planete
        earth.setSelection(earthPosition);
        earth.setOnItemSelectedListener(new OnItemSelectedListener() {
	
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				//earthImage.setImageResource(((ImageAdapter)earth.getAdapter()).getImageId(position));
				earthImage.setImageResource(earthStats[position][0]);
				earthGravity.setProgress(earthStats[position][1]);
				earthSize.setProgress(earthStats[position][2]);
				earthPosition = position;
			}
	
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
    }
	


	//metoda nadajaca kolor paska postepu w zaleznosci od postepu
	public void setValue(ProgressBar bar, int value, int max){
		final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
        ShapeDrawable pgDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null,null));
        int myColor = Color.RED;
        double div = (double)value/(double)max;
        if(div > 0.9) myColor = Color.BLUE;
        else if(div > 0.5) myColor = Color.GREEN;
        else if(div > 0.25) myColor = Color.YELLOW;
        pgDrawable.getPaint().setColor(myColor);
        ClipDrawable pg = new ClipDrawable(pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        bar.setProgressDrawable(pg);   
        bar.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));
        bar.setProgress(value);
	}
	
	public int getEarthSize(){
		return earthStats[earthPosition][2];
	}
	
	public int getEarthGravity(){
		return earthStats[earthPosition][1];
	}
	
	public int getPlayerSpeed(){
		return playerStats[playerPosition][1];
	}
	
	public int getPlayerLife(){
		return playerStats[playerPosition][2];
	}
	
	public int getPlayerUpgrade(){
		return playerStats[playerPosition][3];
	}
	
	
	protected void onPause() {
    	super.onPause();
    	Log.d("OptionsTab", "MYonPause is called");
    	saveState();
	}
	
	

	private void loadSettings() {
//		saver = new SaveService(OptionsTab2.this);
		  new UserSettings();
		 UserSettings saved = saver.readSettings("user_settings");
		 if (saved==null) {
			 saved = new UserSettings();	
		 }
		 playerPosition=saved.getCharacter();
		 earthPosition=saved.getEarth();
	
	}
	
	private void saveState() {
//		saver = new SaveService(OptionsTab2.this);
//		UserSettings savedSettings = new UserSettings();
		savedSettings.setCharacter(playerPosition);
		savedSettings.setEarth(earthPosition);
        saver.saveSettings(savedSettings, "user_settings");
        Log.d("OptionsTab", "SAVED SETTINGS");
        
	}
	
}

