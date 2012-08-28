package com.gra.gra;

import android.graphics.Paint;

/**
 * 
 * @author Szpada
 * 
 * klasa przechowujaca dane z o poszczegolnych obiektach (do zapisu stanu gry)
 *
 */
public class Unit {
	/*
	 * ZMIENNE DLA KAZDEGO OBIEKTU
	 */
		//pozycja
		private float x;
		private float y;
		
		//kat
		private int angle;
		
		//predkosc
		private double speed;
		
		//masa
		private int mass;
		
		//promien
		private int radius;
		
		//klatka animacji
		private int currentFrame;
	/*
	 * PLAYER
	 */
		
		//mnozniki (upgradeowe) do ziemi
		private double earth_gravity_multiplier;
		private double earth_radius_multiplier; 	
		
		private long timer;
		private long earth_timer;
		private long armagedon_timer;
		private long money_rain_timer;
		
		//flaga ktorej ustawienie na true sprawia ze zmienuly sie statystyki ziemi
		private boolean earth_stats_changed;
		
		private double current_jump_power;
		
		private int earth_radius;	//promien okregu po jakim ma siê poruszaæ nasz obiekt
		private boolean on_ground;	//czy dotyka ziemi
		
		private long points;		//punkty gracza
		private int multiplier;		//mnoznik punktow
		
		private boolean armagedon;
		private boolean money_rain;
		
		private int life;			//zycie gracza MAX = 3 smierc przy Life = 0
		
		private double sucking_range;	//ile od gracza moze znajdowac sie moneta zeby ja zassal
	
		
	/*
	 * EARTH
	 */
		private double gravity;	//stala grawitacyjna
		private boolean suck_my_stats;
		
	/*
	 * FLYING OBJECT
	 */
		private int life_timer;
		
		//zmienne dodatkowe dla podanych nizej obiektow
		/*
		 * Asteroid
		 */
		private int size;
		private int basic_radius;
		
		/*
		 * Money
		 */
		//zmienna points jest wyzej (w playerze)
		
		/*
		 * Upgrade
		 */
		private upgradeType type;

		/****************************************************************
		 * 						KONSTRUKTORY							*
		 ****************************************************************/
		
		//PLAYER
		
		public Unit(float x, float y, int angle, double speed, int mass,
				int radius, int currentFrame, double earth_gravity_multiplier,
				double earth_radius_multiplier, long timer, long earth_timer,
				long armagedon_timer, long money_rain_timer,
				boolean earth_stats_changed, double current_jump_power,
				boolean on_ground, long points, int multiplier,
				boolean armagedon, boolean money_rain, int life,
				double sucking_range) {
			this.x = x;
			this.y = y;
			this.angle = angle;
			this.speed = speed;
			this.mass = mass;
			this.radius = radius;
			this.currentFrame = currentFrame;
			this.earth_gravity_multiplier = earth_gravity_multiplier;
			this.earth_radius_multiplier = earth_radius_multiplier;
			this.timer = timer;
			this.earth_timer = earth_timer;
			this.armagedon_timer = armagedon_timer;
			this.money_rain_timer = money_rain_timer;
			this.earth_stats_changed = earth_stats_changed;
			this.current_jump_power = current_jump_power;
			this.on_ground = on_ground;
			this.points = points;
			this.multiplier = multiplier;
			this.armagedon = armagedon;
			this.money_rain = money_rain;
			this.life = life;
			this.sucking_range = sucking_range;
		}
		
		//ASTEROIDA
		
		public Unit(float x, float y, int angle, double speed, int mass,
				int radius, int currentFrame, int size, int basic_radius, int life_timer) {
			this.x = x;
			this.y = y;
			this.angle = angle;
			this.speed = speed;
			this.mass = mass;
			this.radius = radius;
			this.currentFrame = currentFrame;
			this.size = size;
			this.basic_radius = basic_radius;
			this.life_timer = life_timer;
		}

		//MONEY
		public Unit(float x, float y, int angle, double speed, int mass,
				int radius, int currentFrame, long points, int life_timer,
				int size) {
			this.x = x;
			this.y = y;
			this.angle = angle;
			this.speed = speed;
			this.mass = mass;
			this.radius = radius;
			this.currentFrame = currentFrame;
			this.points = points;
			this.life_timer = life_timer;
			this.size = size;
		}

		//UPGRADE
		public Unit(float x, float y, int angle, double speed, int mass,
				int radius, int currentFrame, int life_timer, upgradeType type) {
			this.x = x;
			this.y = y;
			this.angle = angle;
			this.speed = speed;
			this.mass = mass;
			this.radius = radius;
			this.currentFrame = currentFrame;
			this.life_timer = life_timer;
			this.type = type;
		}

		public Unit(float x, float y, int angle, double speed, int mass,
				int radius, int currentFrame, long timer, boolean suck_my_stats, double gravity) {
			this.x = x;
			this.y = y;
			this.angle = angle;
			this.speed = speed;
			this.mass = mass;
			this.radius = radius;
			this.currentFrame = currentFrame;
			this.timer = timer;
			this.suck_my_stats = suck_my_stats;
			this.gravity = gravity;
		}

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		public int getAngle() {
			return angle;
		}

		public void setAngle(int angle) {
			this.angle = angle;
		}

		public double getSpeed() {
			return speed;
		}

		public void setSpeed(double speed) {
			this.speed = speed;
		}

		public int getMass() {
			return mass;
		}

		public void setMass(int mass) {
			this.mass = mass;
		}

		public int getRadius() {
			return radius;
		}

		public void setRadius(int radius) {
			this.radius = radius;
		}

		public int getCurrentFrame() {
			return currentFrame;
		}

		public void setCurrentFrame(int currentFrame) {
			this.currentFrame = currentFrame;
		}

		public double getEarth_gravity_multiplier() {
			return earth_gravity_multiplier;
		}

		public void setEarth_gravity_multiplier(double earth_gravity_multiplier) {
			this.earth_gravity_multiplier = earth_gravity_multiplier;
		}

		public double getEarth_radius_multiplier() {
			return earth_radius_multiplier;
		}

		public void setEarth_radius_multiplier(double earth_radius_multiplier) {
			this.earth_radius_multiplier = earth_radius_multiplier;
		}

		public long getTimer() {
			return timer;
		}

		public void setTimer(long timer) {
			this.timer = timer;
		}

		public long getEarth_timer() {
			return earth_timer;
		}

		public void setEarth_timer(long earth_timer) {
			this.earth_timer = earth_timer;
		}

		public long getArmagedon_timer() {
			return armagedon_timer;
		}

		public void setArmagedon_timer(long armagedon_timer) {
			this.armagedon_timer = armagedon_timer;
		}

		public long getMoney_rain_timer() {
			return money_rain_timer;
		}

		public void setMoney_rain_timer(long money_rain_timer) {
			this.money_rain_timer = money_rain_timer;
		}

		public boolean isEarth_stats_changed() {
			return earth_stats_changed;
		}

		public void setEarth_stats_changed(boolean earth_stats_changed) {
			this.earth_stats_changed = earth_stats_changed;
		}

		public double getCurrent_jump_power() {
			return current_jump_power;
		}

		public void setCurrent_jump_power(double current_jump_power) {
			this.current_jump_power = current_jump_power;
		}

		public int getEarth_radius() {
			return earth_radius;
		}

		public void setEarth_radius(int earth_radius) {
			this.earth_radius = earth_radius;
		}

		public boolean isOn_ground() {
			return on_ground;
		}

		public void setOn_ground(boolean on_ground) {
			this.on_ground = on_ground;
		}

		public long getPoints() {
			return points;
		}

		public void setPoints(long points) {
			this.points = points;
		}

		public int getMultiplier() {
			return multiplier;
		}

		public void setMultiplier(int multiplier) {
			this.multiplier = multiplier;
		}

		public boolean isArmagedon() {
			return armagedon;
		}

		public void setArmagedon(boolean armagedon) {
			this.armagedon = armagedon;
		}

		public boolean isMoney_rain() {
			return money_rain;
		}

		public void setMoney_rain(boolean money_rain) {
			this.money_rain = money_rain;
		}

		public int getLife() {
			return life;
		}

		public void setLife(int life) {
			this.life = life;
		}

		public double getSucking_range() {
			return sucking_range;
		}

		public void setSucking_range(double sucking_range) {
			this.sucking_range = sucking_range;
		}

		public double getGravity() {
			return gravity;
		}

		public void setGravity(double gravity) {
			this.gravity = gravity;
		}

		public boolean isSuck_my_stats() {
			return suck_my_stats;
		}

		public void setSuck_my_stats(boolean suck_my_stats) {
			this.suck_my_stats = suck_my_stats;
		}

		public int getLife_timer() {
			return life_timer;
		}

		public void setLife_timer(int life_timer) {
			this.life_timer = life_timer;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getBasic_radius() {
			return basic_radius;
		}

		public void setBasic_radius(int basic_radius) {
			this.basic_radius = basic_radius;
		}

		public upgradeType getType() {
			return type;
		}

		public void setType(upgradeType type) {
			this.type = type;
		}
		
}