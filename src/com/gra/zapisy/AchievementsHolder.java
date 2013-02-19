package com.gra.zapisy;

import java.io.Serializable;

import com.gra.gra.upgradeType;

/**
 * 
 * @author Szpada
 *
 * Klasa przechowujaca skladowe achievementow - jest uzupelniana podczas gry i wykorzystywana
 * do aktualizacji progressu i achievementow w opcjach. Wszystkei metody dodajace skladowe achievementow
 * zwracaja achievementType (enum) dzieki czemu mozna w grze od razu wyswietlic info ze zdobytym achievemenetem 
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
	
	//zmienna pomocnicza do smierci (gracz moze umrzec tylko raz podczas rozgrywki)
	private boolean playerAlreadyDead;
	
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
		
		playerAlreadyDead = false;
	}

	public achievementType addUpgrade(upgradeType type){
		//achievementType zwracany przez metode
		achievementType acvType = null;
		//collector
		allUpgrades[type.ordinal()] = true;
		collector = true;
		for(int i = 0; i < upgradeType.values().length; i++){
			if(allUpgrades[i] == false){
				collector = false;
				break;
			}
		}
		//jesli upgrade jest serduszkiem dodaj skladowa achievementu HEART
		if(type == upgradeType.life){
			acvType = addHeart();
		}
		
		oneGameUpgrades++;
		if(oneGameUpgrades % 10 == 0) upgrades10++;
		if(oneGameUpgrades % 20 == 0) upgrades20++;
		if(oneGameUpgrades % 30 == 0) upgrades30++;
		//Upgrade Novice 
		if(oneGameUpgrades >= 5){
			upgradeNovice = true;
			acvType = achievementType.novice;
		}
		//Upgrade Apprentice 
		if(upgrades10 >= 2){
			upgradeApprentice = true;
			acvType = achievementType.apprentice;
		}
		//Upgrade Adept 
		if(upgrades20 >= 3){
			upgradeAdept = true;
			acvType = achievementType.adept;
		}
		//Upgrade Master 
		if(upgrades30 >= 4){
			upgradeMaster = true;
			acvType = achievementType.master;
		}
		return acvType;
	}
	
	public achievementType addDeath(){
		//achievementType zwracany przez metode
		achievementType acvType = null;
		if(!playerAlreadyDead){
			deaths++;
			//I see dead people
			if(deaths >= 100){
				iSeeDeadPeople = true;
				acvType = achievementType.isdp;
			}
			//Walking dead
			if(deaths >= 1000){
				walkingDead = true;
				acvType = achievementType.dead;
			}
			playerAlreadyDead = true;
		}
		return acvType;
	}
	
	public achievementType addHeart(){
		//achievementType zwracany przez metode
		achievementType acvType = null;
		hearts++;
		if(hearts >= 50){
			lover = true;
			acvType = achievementType.lover;
		}
		if(hearts >= 250){
			casanova = true;
			acvType = achievementType.casanova;
		}
		return acvType;
	}
	
	public achievementType addAlien(){
		//achievementType zwracany przez metode
		achievementType acvType = null;
		aliens++;
		if(aliens >= 5){
			alienInvasion = true;
			acvType = achievementType.alien;
		}
		return acvType;
	}
	
	public achievementType addDuck(){
		//achievementType zwracany przez metode
		achievementType acvType = null;
		duck++;
		if(duck >= 10){
			duckHunter = true;
			acvType = achievementType.duck;
		}
		return acvType;
	}
	
	//wyczysc tymczasowe dane jak : liczbe upgradow zebranych w danej grze, tablice
	//zebranych upgradow i zmienna mowiaca o tym czy gracz zyje. 
	//Metoda powinna byc wywolana po ZAKONCZENIU gry.
	public void clearTemporaryData(){
		oneGameUpgrades = 0;
		for(int i = 0; i < upgradeType.values().length; i++){
			allUpgrades[i] = false;
		}
		playerAlreadyDead = false;
	}

	public boolean isUpgradeNovice() {
		return upgradeNovice;
	}

	public void setUpgradeNovice(boolean upgradeNovice) {
		this.upgradeNovice = upgradeNovice;
	}

	public boolean isUpgradeApprentice() {
		return upgradeApprentice;
	}

	public void setUpgradeApprentice(boolean upgradeApprentice) {
		this.upgradeApprentice = upgradeApprentice;
	}

	public boolean isUpgradeAdept() {
		return upgradeAdept;
	}

	public void setUpgradeAdept(boolean upgradeAdept) {
		this.upgradeAdept = upgradeAdept;
	}

	public boolean isUpgradeMaster() {
		return upgradeMaster;
	}

	public void setUpgradeMaster(boolean upgradeMaster) {
		this.upgradeMaster = upgradeMaster;
	}

	public boolean isWalkingDead() {
		return walkingDead;
	}

	public void setWalkingDead(boolean walkingDead) {
		this.walkingDead = walkingDead;
	}

	public boolean isLover() {
		return lover;
	}

	public void setLover(boolean lover) {
		this.lover = lover;
	}

	public boolean isCasanova() {
		return casanova;
	}

	public void setCasanova(boolean casanova) {
		this.casanova = casanova;
	}

	public boolean isAlienInvasion() {
		return alienInvasion;
	}

	public void setAlienInvasion(boolean alienInvasion) {
		this.alienInvasion = alienInvasion;
	}

	public boolean isCollector() {
		return collector;
	}

	public void setCollector(boolean collector) {
		this.collector = collector;
	}

	public boolean isDuckHunter() {
		return duckHunter;
	}

	public void setDuckHunter(boolean duckHunter) {
		this.duckHunter = duckHunter;
	}

	public boolean isTheSecretAchievement() {
		return theSecretAchievement;
	}

	public void setTheSecretAchievement(boolean theSecretAchievement) {
		this.theSecretAchievement = theSecretAchievement;
	}

	public boolean isiSeeDeadPeople() {
		return iSeeDeadPeople;
	}

	public void setiSeeDeadPeople(boolean iSeeDeadPeople) {
		this.iSeeDeadPeople = iSeeDeadPeople;
	}

	public int getOneGameUpgrades() {
		return oneGameUpgrades;
	}

	public void setOneGameUpgrades(int oneGameUpgrades) {
		this.oneGameUpgrades = oneGameUpgrades;
	}

	public int getUpgrades10() {
		return upgrades10;
	}

	public void setUpgrades10(int upgrades10) {
		this.upgrades10 = upgrades10;
	}

	public int getUpgrades20() {
		return upgrades20;
	}

	public void setUpgrades20(int upgrades20) {
		this.upgrades20 = upgrades20;
	}

	public int getUpgrades30() {
		return upgrades30;
	}

	public void setUpgrades30(int upgrades30) {
		this.upgrades30 = upgrades30;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getHearts() {
		return hearts;
	}

	public void setHearts(int hearts) {
		this.hearts = hearts;
	}

	public int getAliens() {
		return aliens;
	}

	public void setAliens(int aliens) {
		this.aliens = aliens;
	}

	public boolean[] getAllUpgrades() {
		return allUpgrades;
	}

	public void setAllUpgrades(boolean[] allUpgrades) {
		this.allUpgrades = allUpgrades;
	}

	public int getDuck() {
		return duck;
	}

	public void setDuck(int duck) {
		this.duck = duck;
	}

	public boolean isPlayerAlreadyDead() {
		return playerAlreadyDead;
	}

	public void setPlayerAlreadyDead(boolean playerAlreadyDead) {
		this.playerAlreadyDead = playerAlreadyDead;
	}
	
	
}
