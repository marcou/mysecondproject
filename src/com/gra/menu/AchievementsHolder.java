package com.gra.menu;

import java.io.Serializable;

import com.gra.gra.upgradeType;

/**
 * 
 * @author Szpada
 *
 * Klasa przechowujaca skladowe achievementow - jest uzupelniana podczas gry i wykorzystywana
 * do aktualizacji progressu i achievementow w opcjach
 * 
 * Achievementy (z pedalskimi nazwami):
 * 1. Upgrade Novice - collect 5 upgrades in one game
 * 2. Upgrade Apprentice - collect 10 upgrades twice
 * 3. Upgrade Adept - collect 20 upgrades 3 times
 * 4. Upgrade Master - collect 30 upgrades 4 times
 * 5. I see dead people - die 100 times
 * 6. Walking dead - die 1000 times
 * 7. Lover - collect 50 hearts
 * 8. Casanova - collect 250 hearts
 * 9. Alien Invasion - knock down a spaceship 5 times
 * 10. Collector - collect all types of upgrades in one game
 * 11. Duck Hunter - get hit in the air 10 times
 * 12. The Secret achievement - collect all the achievements
 */
public class AchievementsHolder implements Serializable{
	//achievement zdobyty
	private boolean upgradeNovice;
	private boolean upgradeApprentice;
	private boolean upgradeAdept;
	private boolean upgradeMaster;
	private boolean iSeeDeadPeople;
	private boolean walkingDead;
	private boolean lover;
	private boolean casanova;
	private boolean alienInvasion;
	private boolean collector;
	private boolean duckHunter;
	private boolean theSecretAchievement;
	
	//ile zdobyl upgradow w 1 grze - jesli 10 to upgrades10++ itd
	private int oneGameUpgrades;
	//ile razy zdobyl 10 upgradow
	private int upgrades10;
	//ile razy zdobul 20 upgradow
	private int upgrades20;
	//ile razy zdobyl 30 upgradow
	private int upgrades30;
	
	private int deaths;
	private int hearts;
	private int aliens;
	private boolean allUpgrades[];
	private int duck;
	
	public AchievementsHolder(){
		upgradeNovice = false;
		upgradeApprentice = false;
		upgradeAdept = false;
		upgradeMaster = false;
		iSeeDeadPeople = false;
		walkingDead = false;
		lover = false;
		casanova = false;
		alienInvasion = false;
		collector = false;
		
		oneGameUpgrades = 0;
		upgrades10 = 0;
		upgrades20 = 0;
		upgrades30 = 0;
		deaths = 0;
		hearts = 0;
		aliens = 0;
		duck = 0;
		
		allUpgrades = new boolean[upgradeType.values().length];
		for(int i = 0; i < upgradeType.values().length; i++){
			allUpgrades[i] = false;
		}
	}

	public void addUpgrade(upgradeType type){
		//collector
		allUpgrades[type.ordinal()] = true;
		collector = true;
		for(int i = 0; i < upgradeType.values().length; i++){
			if(allUpgrades[i] == false){
				collector = false;
				break;
			}
		}
		oneGameUpgrades++;
		if(oneGameUpgrades % 10 == 0) upgrades10++;
		if(oneGameUpgrades % 20 == 0) upgrades20++;
		if(oneGameUpgrades % 30 == 0) upgrades30++;
		//Upgrade Novice 
		if(oneGameUpgrades >= 5) upgradeNovice = true;
		//Upgrade Apprentice 
		if(upgrades10 >= 2) upgradeApprentice = true;
		//Upgrade Adept 
		if(upgrades20 >= 3) upgradeAdept = true;
		//Upgrade Master 
		if(upgrades30 >= 4) upgradeMaster = true;
	}
	
	public void addDeath(){
		deaths++;
		//I see dead people
		if(deaths >= 100) iSeeDeadPeople = true;
		//Walking dead
		if(deaths >= 1000) walkingDead = true;
	}
	
	public void AddHeart(){
		hearts++;
		if(hearts >= 50) lover = true;
		if(hearts >= 250) casanova = true;
	}
	
	public void addAlien(){
		aliens++;
		if(aliens >= 5) alienInvasion = true;
	}
	
	public void addDuck(){
		duck++;
		if(duck >= 10) duckHunter = true;
	}
	
	public void clearTemporaryData(){
		oneGameUpgrades = 0;
		for(int i = 0; i < upgradeType.values().length; i++){
			allUpgrades[i] = false;
		}
	}
}
