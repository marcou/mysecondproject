package com.gra.gra;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Szpada
 *
 *	klasa odpowiadajaca za generowanie obiektow latajacych
 */
public class Generator {
	
	private List<FlyingObject> objects = new ArrayList<FlyingObject>();
	private int area_x;
	private int area_y;
	private int area_w;
	private int area_h;
	
	private int playerUpgradeMultiplier;
	
	public Generator() {
		this.playerUpgradeMultiplier = 0;
	}
	
	//==================================	GENERATOR	=====================================
    //metoda generujaca latajace obiekty, pierwszy argument to punkty od ktorych jest
    //zalezna intensywnosc "opadow", poziom wrogow, liczba i wartosc monet itd itp drugi
    //argument jest rowny false chyba ze gracz podniesie upgrade "armagedon" ktory wlacza
    //maksymalny mozliwy (zalezny od punktow) opad wrogow
    //=======================================================================================
    public List<FlyingObject> generate(long points, boolean armagedon, boolean money_rain){
    	
    	//wyczyszczenie listy obiektow
    	this.objects.clear();
    	
    	//progi punktowe wedlug ktorych ustawiane sa fale wrogow
    	int threshold_1 = 10;
    	int threshold_2 = 50;
    	int threshold_3 = 250;
    	int threshold_4 = 1250;
    	int threshold_5 = 6000;
    	int threshold_6 = 10000;
    	int threshold_7 = 1000000;
    	
    	Random rand = new Random();
    	
    	int asteroid_count = 0;	//liczba asteroid
    	int money_count = 0;	//liczba kasy
    	//int upgrade_count = 0;	//liczba upgradeow
    	int money_asteroid_count = 0;
    	int money_asteroid_quota = 0;
    	
    	int asteroid_difficulty = 1;	//zmienna wplywajaca na wypuszczanie wiekszych asteroid
    									//przedzial wartosci :
    									//			od 0 (same male - zadnych duzych i ogromnych) 
    									//			do 10 (same ogromne i duze)
    	
    	int money_value = 1;			//zmienna wplywajaca na wartosc pieneidzy wypuszczanych w kosmos
    									//przedzial wartosci :
    									//			od 1 do miljon (maxymalna wartosc jaka moga miec monety)
    	
    	
    	if(points < threshold_1){
    		asteroid_count = 1;
    		money_count = 5;
    		//upgrade_count = 0;
    		
    		asteroid_difficulty = 1;
    		money_value = 1;
    	}
    	else if(points <threshold_2){
    		asteroid_count = 2;
    		money_count = 6;
    		//upgrade_count = 0;
    		
    		asteroid_difficulty = 1;
    		money_value = 4;
    	}
    	else if(points <threshold_3){
    		asteroid_count = 3;
    		money_count = 6;
    		//upgrade_count = playerUpgradeMultiplier/4;
    		
    		asteroid_difficulty = 2;
    		money_value = 8;
    		
    		money_asteroid_count = 1;
    		money_asteroid_quota = rand.nextInt(2) + 2;
    	}
    	else if(points <threshold_4){
    		asteroid_count = 4;
    		money_count = 10;
    		//upgrade_count = playerUpgradeMultiplier/3;
    		
    		asteroid_difficulty = 3;
    		money_value = 16;
    		
    		money_asteroid_count = 2;
    		money_asteroid_quota = rand.nextInt(2) + 2;
    	}
    	else if(points <threshold_5){
    		asteroid_count = 5;
    		money_count = 8;
    		//upgrade_count = playerUpgradeMultiplier/3;
    		
    		asteroid_difficulty = 4;
    		money_value = 32;
    		
    		money_asteroid_count = 2;
    		money_asteroid_quota = rand.nextInt(3) + 3;
    	}
    	else if(points <threshold_6){
    		asteroid_count = 6;
    		money_count = 6;
    		//upgrade_count = playerUpgradeMultiplier/3 + 1;
    		
    		asteroid_difficulty = 5;
    		money_value = 64;
    		
    		money_asteroid_count = 3;
    		money_asteroid_quota = rand.nextInt(4) + 4;
    	}
    	else if(points <threshold_7){
    		asteroid_count = 7;
    		money_count = 4;
    		//upgrade_count = playerUpgradeMultiplier/3 + 1;
    		
    		asteroid_difficulty = 7;
    		money_value = 128;
    		
    		money_asteroid_count = 3;
    		money_asteroid_quota = rand.nextInt(5) + 5;
    	}
    	else{
    		asteroid_count = 8;
    		money_count = 2;
    		//upgrade_count = playerUpgradeMultiplier/2 + 1;
    		
    		asteroid_difficulty = 10;
    		money_value = 256;
    		
    		money_asteroid_count = 4;
    		money_asteroid_quota = rand.nextInt(6) + 6;
    	}
    	
    	if(armagedon && money_rain){
    		//asteroid_count = (asteroid_count + 1) * 2;
    		//money_count = (money_count + 1) * 2;
    		money_asteroid_count = asteroid_count + money_count + 2;
    		money_asteroid_quota = rand.nextInt(10) + 10;
    		asteroid_count = 0;
    		money_count = 0;
    	}
    	else if(money_rain){
    		money_count = (money_count + 1) * 2;
    		asteroid_count = 0;
    	}
    	else if(armagedon){
    		asteroid_count = (asteroid_count + 1) * 2;
    		money_count = 0;
    	}
    	else{
    		if(asteroid_count >= 1){
        		asteroid_count = rand.nextInt(asteroid_count);
        	}
        	if(money_count >= 1){
        		money_count = rand.nextInt(money_count);
        	}
        	//if(upgrade_count >= 1){
        	//	upgrade_count = rand.nextInt(upgrade_count);
        	//}
        	if(money_asteroid_count >= 1){
        		money_asteroid_count = rand.nextInt(money_asteroid_count);
        	}
    	}
    	
//    	Log.d("Generator", "ASTEROIDY : " + asteroid_count);
//    	Log.d("Generator", "KASA : " + money_count);
//    	Log.d("Generator", "UPGREJDY : " + upgrade_count);
    	
    	//typ upgrejdu (musi zostac zainicjowany)
		upgradeType type = upgradeType.speed;
		//wlasciwosci obiektow
		Properties prop;
		
		int asteroid_size = 1;
		
    	for(int i = 0; i < asteroid_count; i++){
    		prop = calculateProperties();
    		asteroid_size = generateSize(asteroid_difficulty);
    		Asteroid asteroid = new Asteroid(objects, prop.getX(), prop.getY(), rand.nextInt(12), prop.getAngle(), 20, 5, asteroid_size);
//    		MoneyAsteroid asteroid = new MoneyAsteroid(objects, prop.getX(), prop.getY(), rand.nextInt(12), prop.getAngle(), 20, 5, asteroid_size, 10);
    		objects.add(asteroid);
    	}
    	for(int i = 0; i < money_count; i++){
    		prop = calculateProperties();
    		Money money = new Money(objects, prop.getX(), prop.getY(), rand.nextInt(12), prop.getAngle(), money_value + 1, 5);
    		objects.add(money);
    	}
		/*for(int i = 0; i < upgrade_count; i++){
			type = calculateType();
			prop = calculateProperties();
    		Upgrade upgrade = new Upgrade(objects, prop.getX(), prop.getY(), rand.nextInt(6), prop.getAngle(),type);
    		objects.add(upgrade);
		}*/
		for(int i = 0; i < money_asteroid_count; i++){
    		prop = calculateProperties();
//    		MoneyAsteroid money_asteroid = new MoneyAsteroid(objects, prop.getX(), prop.getY(), rand.nextInt(12), prop.getAngle(), 20, 5, money_asteroid_quota, money_value);
    		MoneyAsteroid money_asteroid = new MoneyAsteroid(objects, prop.getX(), prop.getY(), rand.nextInt(12), prop.getAngle(), 20, 5, 1 + money_asteroid_quota/6, money_value, money_asteroid_quota);
    		objects.add(money_asteroid);
    	}
		return objects;
    }
    
    public List<FlyingObject> generateUpgrades(long points, boolean armagedon, boolean money_rain){
    	//wyczyszczenie listy obiektow
    	this.objects.clear();
    	
    	if(armagedon || money_rain){
    		return objects;
    	}
    	//progi punktowe wedlug ktorych ustawiane sa fale wrogow
    	int threshold_1 = 10;
    	int threshold_2 = 50;
    	int threshold_3 = 250;
    	int threshold_4 = 1250;
    	int threshold_5 = 6000;
    	int threshold_6 = 10000;
    	int threshold_7 = 1000000;
    	
    	Random rand = new Random();
    	
    	int upgrade_count = 0;
    	
    	if(points < threshold_1){
    		upgrade_count = 0;
    	}
    	else if(points <threshold_2){
    		upgrade_count = 1;
    	}
    	else if(points <threshold_3){
    		upgrade_count = 1;
    	}
    	else if(points <threshold_4){
    		upgrade_count = 2;
    	}
    	else if(points <threshold_5){
    		upgrade_count = 2;
    	}
    	else if(points <threshold_6){
    		upgrade_count = 3;
    	}
    	else if(points <threshold_7){
    		upgrade_count = 3;
    	}
    	else{
    		upgrade_count = 4;
    	}
    	
    	if(upgrade_count >= 1){
    		upgrade_count = rand.nextInt(upgrade_count);
    	}
    	
    	//typ upgrejdu (musi zostac zainicjowany)
		upgradeType type = upgradeType.speed;
		//wlasciwosci obiektow
		Properties prop;
    			
    	for(int i = 0; i < upgrade_count; i++){
			type = calculateType();
			prop = calculateProperties();
    		Upgrade upgrade = new Upgrade(objects, prop.getX(), prop.getY(), rand.nextInt(6), prop.getAngle(),type);
    		objects.add(upgrade);
		}
    	
    	return objects;
    }
    
    public upgradeType calculateType(){
    	int interval;
    	Random rand = new Random();
    	interval = rand.nextInt(13);
		upgradeType type = upgradeType.speed;
		switch(interval){
		case 0:
			type = upgradeType.high_gravity;
			break;
		case 1:
			type = upgradeType.huge_player;
			break;
		case 2:
			type = upgradeType.low_gravity;
			break;
		case 3:
			type = upgradeType.speed;
			break;
		case 4:
			type = upgradeType.tiny_player;
			break;
		case 5:
			type = upgradeType.ultra_suck;
			break;
		case 6:
			type = upgradeType.armagedon;
			break;
		case 7:
			type = upgradeType.money_rain;
			break;
		case 8:
			type = upgradeType.x2;
			break;
		case 9:
			type = upgradeType.x3;
			break;
		case 10:
			type = upgradeType.x4;
			break;
		case 11:
			type = upgradeType.immortality;
			break;
		case 12:
			type = upgradeType.life;
			break;
		}
		return type;
    }
    
    public Properties calculateProperties(){
    	
    	Properties properties;
    	Random rand = new Random();
    	
    	int interval;
    	int x = 0;
    	int y = 0;
    	int angle = 0;
    	
    	interval = rand.nextInt(4);
    	
    	/**********************************************************
    	 *  z katami bedzie troche roboty bo :
    	 *  				________________
    	 *  	o - obiekt1	|				|	--> skraj mapy
    	 *  				|				|
    	 *  				|				|
    	 *  				|				|
    	 *  	o - obiekt2 |		O - ziemia
    	 *  				|				|
    	 * 					| 				|
    	 *  				|			   	|
    	 *  	o - obiekt3	|_______________|
    	 *  
    	 *  w takiej sytuacji obiekt :
    	 *  1 - powinien miec kat w przedziale 0 - 90
    	 *  2 - powinien miec kat w przedziale 315 - 45
    	 *  3 - powinien miec kat w przedziale 270 - 360
    	 *  
    	 *  stad ponizszy wzor na kat :
    	 *  				   (w zaleznosci ktora z 4 mozliwosci, w tym przykladzie x = (-79, 0) i y (0, 879))[rysunek u gory]
    	 *  kat = random(90) + - y/880 (liczba z przedzialu 0 - 1) * 90
    	 **********************************************************/
    	
		switch(interval){
		//lewa
		case 0:
			x = area_x;//rand.nextInt(80);
			y = rand.nextInt(area_h);
			angle = 90 - rand.nextInt(180);//rand.nextInt(90) - (int)((y/879.0)*90.0);
			break;
		//prawa
		case 1:
			x = area_w;//480 + rand.nextInt(80);
			y = rand.nextInt(area_h);
			angle = 90 + rand.nextInt(180);//rand.nextInt(90) + (int)((y/879.0)*90.0) + 90;
			break;
		//gora
		case 2:
			x = rand.nextInt(area_w);
			y = area_y;//- rand.nextInt(80);
			angle = rand.nextInt(180);//rand.nextInt(90) + (int)((x/559.0)*90.0);
			break;
		//dol
		case 3:
			x = rand.nextInt(area_w);
			y = area_h;//800 + rand.nextInt(80);
			angle = 180 + rand.nextInt(180);//rand.nextInt(90) - (int)((x/559.0)*90.0) + 270; 
			break;
		}
    	properties = new Properties(x, y, angle);
    	return properties;
    }
    
    private class Properties{
    	private int x;
    	private int y;
    	private int angle;
    	
    	public Properties(int x, int y, int angle){
    		this.x = x;
    		this.y = y;
    		this.angle = angle;
    	}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getAngle() {
			return angle;
		}
    }
    
    public void setBounds(int x, int y, int w, int h){
    	this.area_x = x + 60;
    	this.area_y = y + 60;
    	this.area_w = w - 60;
    	this.area_h = h - 60;
    }
    
    public int generateSize(int difficulty){
    	Random rand = new Random();
    	int size = 1;
    	switch(rand.nextInt(difficulty) + (int)(difficulty/5)){
    	case 0:
    		size = 1;
    		break;
    	case 1:
    		size = 1;
    		break;
    	case 2:
    		size = 1;
    		break;
    	case 3:
    		size = 2;
    		break;
    	case 4:
    		size = 2;
    		break;
    	case 5:
    		size = 2;
    		break;
    	case 6:
    		size = 2;
    		break;
    	case 7:
    		size = 2;
    		break;
    	case 8:
    		size = 3;
    		break;
    	case 9:
    	case 10:
    	case 11:
    		size = 3;
    		break;
    	}
    	return size;
    }

	public int getPlayerUpgradeMultiplier() {
		return playerUpgradeMultiplier;
	}

	public void setPlayerUpgradeMultiplier(int playerUpgradeMultiplier) {
		this.playerUpgradeMultiplier = playerUpgradeMultiplier;
	}
    
    
}
