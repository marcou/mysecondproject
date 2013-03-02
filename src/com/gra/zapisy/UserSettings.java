package com.gra.zapisy;

import java.io.Serializable;

import android.util.Log;

import com.gra.R;

/**
 * 
 * @author Szpada
 *
 * Klasa zawiera pola z optionsa ktore wybra³ gracz
 */
public class UserSettings implements Serializable {
	//number wybranej postaci
	private int character ;
	//numer wybranej planety
	private int earth;
	//typ sterowania
	private boolean accelerometer;
	//przezroczyste przyciski
	private boolean invisibleButtons;
	//postep gracza
	private int progress;
	//maksymalny postep
	private int maxProgress;
	//liczba rozegranych gier
	private int gamesPlayed;
	//statystyki postaci 			|BITMAP|SPEED|LIFE|UPGRADE|
	private  int[][] playerStats= {{R.drawable.jez1,1,1,1},{R.drawable.jez2,3,1,1},{R.drawable.jez3,1,3,1},{R.drawable.jez4,2,2,3},{R.drawable.jez3,2,5,5},{R.drawable.jez4,5,3,5}};
	//statystyki planety			|BITMAP|GRAVITY|SIZE|
	private  int[][] earthStats= {{R.drawable.ziemia1,1,1},{R.drawable.ziemia2,5,5},{R.drawable.ziemia3,1,3},{R.drawable.ziemia4,2,1},{R.drawable.ziemia1,4,1},{R.drawable.ziemia2,3,3}};
	//informacje o acziwmentach
	private AchievementsHolder achievements;
	//wynik gracza
	private long score;
	
	public UserSettings(){
		character = 0;
		earth = 0;
		accelerometer = false;
		invisibleButtons = true;
		progress = 1000;
		maxProgress = 1000;
		gamesPlayed = 0;
		score = 0;
		achievements = new AchievementsHolder();
	}
	
	public UserSettings(int character, int earth, boolean acelerometer,
			boolean invisibleButtons, int progress, int maxProgress) {
		this.character = character;
		this.earth = earth;
		this.accelerometer = acelerometer;
		this.invisibleButtons = invisibleButtons;
		this.progress = progress;
		this.maxProgress = maxProgress;
		achievements = new AchievementsHolder();
	}

	public int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}

	public int getEarth() {
		return earth;
	}

	public void setEarth(int earth) {
		this.earth = earth;
	}

	public boolean isAccelerometer() {
		return accelerometer;
	}

	public void setAccelerometer(boolean accelerometer) {
		this.accelerometer = accelerometer;
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
	
	//Metoda dodajaca rozegrana gre (tylko keidy gracz zginie liczba gier jest inkrementowana
	public void addGamesPlayed(){
		gamesPlayed++;
	}
	
	//Metoda zwracaj¹ca tablice z bitmapami postaci/planet oraz wlasciwowsciami dla danej postaci/planety
	public int[][] getImages(boolean player){
		int[][] images;
		int length = 2;
		//prog do 20 : 2 jeze| 2 planety
		if(progress < 20){}
		//prog do 100 : 3 jeze| 2 planety
		else if(progress < 100) {
			if(player)length = 3;
			else length = 2;
		}
		//prog do 500 : 4 jeze| 3 planety
		else if(progress < 500){
			if(player)length = 4;
			else length = 3;
		}
		//prog do 1000 : 5 jezy| 3 planety
		else if(progress < 1000){
			if(player)length = 5;
			else length = 4;
		}
		//prog 1000 (MAX) : 6 jezy| 4 planety
		else {
			if(player)length = 6;
			else length = 6;
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
				Log.d("UserSettings", "Tekstura : " + earthStats[i][0]);
			}
		}
		return images;
	}

	public AchievementsHolder getAchievements() {
		return achievements;
	}

	public void setAchievements(AchievementsHolder achievements) {
		this.achievements = achievements;
		
	}
	
	public void countProgress(){
		progress = 0;
		//punkty za achievementy (710)
		if(achievements.isUpgradeNovice()) 			progress += 10;
		if(achievements.isDuckHunter()) 			progress += 10;
		if(achievements.isiSeeDeadPeople()) 		progress += 10;
		if(achievements.isUpgradeApprentice()) 		progress += 20;
		if(achievements.isLover()) 					progress += 20;
		if(achievements.isWalkingDead()) 			progress += 20;
		if(achievements.isUpgradeAdept()) 			progress += 40;
		if(achievements.isCasanova()) 				progress += 40;
		if(achievements.isCollector()) 				progress += 40;
		if(achievements.isUpgradeMaster()) 			progress += 100;
		if(achievements.isTheSecretAchievement()) 	progress += 200;
		if(achievements.isAlienInvasion()) 			progress += 100;
		
		//punkty za rozegrane gry (190)
		if(gamesPlayed > 1900) progress += 190;
		else progress += gamesPlayed/10;
		
		//punkty za ranking? (100) ranking-sranking kurwa
	}

	public int[][] getPlayerStats() {
		return playerStats;
	}

	public void setPlayerStats(int[][] playerStats) {
		this.playerStats = playerStats;
	}

	public int[][] getEarthStats() {
		return earthStats;
	}

	public void setEarthStats(int[][] earthStats) {
		this.earthStats = earthStats;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	
}
