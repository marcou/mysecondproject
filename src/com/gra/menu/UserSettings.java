package com.gra.menu;

import java.util.ArrayList;
import java.util.List;

import com.gra.R;

/**
 * 
 * @author Szpada
 *
 * Klasa zawiera pola z optionsa ktore wybra� gracz
 */
public class UserSettings {
	//number wybranej postaci
	private int character = 0;
	//numer wybranej planety
	private int erath = 0;
	//typ sterowania
	private boolean acelerometer = false;
	//przezroczyste przyciski
	private boolean invisibleButtons = true;
	//postep gracza
	private int progress = 400;
	//maksymalny postep
	private int maxProgress = 1000;
	//statystyki postaci 	|SPEED|LIFE|UPGRADE|
	private int[][] playerStats = {{R.drawable.jez1,1,1,1},{R.drawable.jez2,2,1,1},{R.drawable.jez3,1,3,1},{R.drawable.jez4,2,1,3},{R.drawable.jez3,5,1,1},{R.drawable.jez4,3,4,1}};
	//statystyki planety	|GRAVITY|SIZE|
	private int[][] earthStats = {{R.drawable.ziemia1,1,1},{R.drawable.ziemia2,2,1},{R.drawable.ziemia3,1,3},{R.drawable.ziemia1,2,1},{R.drawable.ziemia2,4,1},{R.drawable.ziemia3,3,3}};
	
	public UserSettings(){
	}
	
	public UserSettings(int character, int erath, boolean acelerometer,
			boolean invisibleButtons, int progress, int maxProgress) {
		this.character = character;
		this.erath = erath;
		this.acelerometer = acelerometer;
		this.invisibleButtons = invisibleButtons;
		this.progress = progress;
		this.maxProgress = maxProgress;
	}

	public int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}

	public int getErath() {
		return erath;
	}

	public void setErath(int erath) {
		this.erath = erath;
	}

	public boolean isAcelerometer() {
		return acelerometer;
	}

	public void setAcelerometer(boolean acelerometer) {
		this.acelerometer = acelerometer;
	}

	public boolean isInvisibleButtons() {
		return invisibleButtons;
	}

	public void setInvisibleButtons(boolean invisibleButtons) {
		this.invisibleButtons = invisibleButtons;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}
	
	public int[][] getImages(boolean player){
		int[][] images;
		int length = 2;
		if(progress < 20){}
		else if(progress < 100) {
			if(player)length = 3;
			else length = 2;
		}
		else if(progress < 500){
			if(player)length = 4;
			else length = 3;
		}
		else if(progress < 1000){
			if(player)length = 5;
			else length = 3;
		}
		else {
			if(player)length = 6;
			else length = 4;
		}
		if(player)images = new int[length][4];
		else images = new int[length][3];
		
		for(int i = 0; i < length; i++){
			//zwracamy tablice danych gracza
			if(player){
				for(int j = 0; j < 4; j++){
					images[i][j] = playerStats[i][j];
				}
			}
			//zwracamy tablice danych planety
			else{
				for(int j = 0; j < 3; j++){
					images[i][j] = earthStats[i][j];
				}
			}
		}
		return images;
	}
}