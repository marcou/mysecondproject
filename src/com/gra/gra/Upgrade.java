package com.gra.gra;

import java.util.List;

import com.gra.R;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

enum upgradeType{speed, low_gravity, high_gravity, tiny_player, huge_player, armagedon, money_rain, ultra_suck, x2, x3, x4, immortality, life};

public class Upgrade extends FlyingObject {

	private int life_timer = 80;
	
	private String tag;
	
	private long time;	//czas przez jaki upgrade dziala
	
	private static int mass = 20;
	private static int radius = 5;
	
	//MODYFIKATORY STATSOW 
	//przez nie przemnazamy wszystkei statystyki (ustawinie 1.0 nie zmienia ich wcale)
	private double earth_gravity = 1.0;	// mno¿nik grawitacji
	private double earth_radius = 1.0;		
	
	private double player_jump_power = 1.0;
	private double player_point_multiplier = 1;
	private double player_speed = 1;
	private double player_radius = 1;
	private double player_sucking_range =1;
	private boolean player_immortality = false;
	private int player_life = 0;
	
	private boolean armagedon; 
	private boolean money_rain;
	
	private upgradeType type;
	
	public Upgrade(List<FlyingObject> objects, float x, float y, double speed, double angle, upgradeType type) {
		super(objects, x, y, speed, angle, mass, radius);
		
//		super.setBmpData(BitmapFactory.decodeResource(view.getResources(), R.drawable.package_upgrade), 1, 1);
		
		super.setLife_timer(life_timer);
		
		this.type = type;
		
		switch(type){
		case speed:
			this.player_speed = 2;
			this.time = 100;
			this.tag = "SPEED";
			break;
		case high_gravity:
			this.earth_gravity = 2.0;
			this.time = 100;
			this.tag = "HIGH GRAVITY";
			break;
		case low_gravity:
			this.earth_gravity = 0.5;
			this.time = 100;
			this.tag = "LOW GRAVITY";
			break;
		case huge_player:
			this.player_radius = 2.0;
			this.time = 100;
			this.tag = "HUGE PLAYER";
			break;
		case tiny_player:
			this.player_radius = 0.5;
			this.time = 100;
			this.tag = "TINY PLAYER";
			break;
		case ultra_suck:
			this.player_sucking_range = 4;
			this.time = 100;
			this.tag = "ULTRA SUCK";
			break;
		//time w przypadku armagedonu i money_rain oznacza przez ile generacji (generacja co jakis czas) bedzie dzialal ten upgrade
		case armagedon:
			this.armagedon = true;
			this.time = 3;
			this.tag = "ARMAGEDON";
			break;
		case money_rain:
			this.money_rain = true;
			this.time = 3;
			this.tag = "MONEY RAIN";
			break;
		case x2:
			this.player_point_multiplier = 2;
			this.time = 100;
			this.tag = "X2";
			break;
		case x3:
			this.player_point_multiplier = 3;
			this.time = 100;
			this.tag = "X3";
			break;
		case x4:
			this.player_point_multiplier = 4;
			this.time = 100;
			this.tag = "X4";
			break;
		case immortality:
			this.player_immortality = true;
			this.time = 80;
			this.tag = "IMMORTALITY";
		case life:
			this.player_life = 1;
			this.time = 80;
			this.tag = "LIFE";
		}
	}

	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja upgradeu z asteroida lub graczem
		if(object instanceof Asteroid || object instanceof GroundEnemy){
			super.getObjects().remove(this);
		}
		//kolizja upgradeu z pieniedzmi
		else if(object instanceof Money){
			
		}
	}
	
	@Override
	public void onDraw(Canvas canvas){
		update();
//		canvas.drawText(this.tag, super.getX() - super.getRadius(), super.getY() - super.getRadius() - 16, paint);
//		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
	}

	public void update(){
		//jesli obiekt dotyka ziemi usun go po czasie "life_timer"
		if(super.isOn_ground()){
			super.setLife_timer(super.getLife_timer() - 1);
			if(super.getLife_timer() < 0){
				super.setLife(0);
			}
		}
		super.setCurrentFrame(super.getCurrentFrame() + 1);
		
		if(super.getCurrentFrame() > super.getFrames()){
			super.setCurrentFrame(0);
		}
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	/********************************
	 * 			UPGRADE				*
	 ********************************/
	//settery gettery
	
	public double getEarth_gravity() {
		return earth_gravity;
	}

	public void setEarth_gravity(double earth_gravity) {
		this.earth_gravity = earth_gravity;
	}

	public double get_earth_radius() {
		return earth_radius;
	}

	public void setEarth_radius(double earth_radius) {
		this.earth_radius = earth_radius;
	}

	public double getPlayer_jump_power() {
		return player_jump_power;
	}

	public void setPlayer_jump_power(double player_jump_power) {
		this.player_jump_power = player_jump_power;
	}

	public double getPlayer_point_multiplier() {
		return player_point_multiplier;
	}

	public void setPlayer_point_multiplier(double player_point_multiplier) {
		this.player_point_multiplier = player_point_multiplier;
	}

	public double getPlayer_speed() {
		return player_speed;
	}

	public void setPlayer_speed(double player_speed) {
		this.player_speed = player_speed;
	}

	public double getPlayer_radius() {
		return player_radius;
	}

	public void setPlayer_radius(double player_radius) {
		this.player_radius = player_radius;
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

	public double getPlayer_sucking_range() {
		return player_sucking_range;
	}

	public void setPlayer_sucking_range(double player_sucking_range) {
		this.player_sucking_range = player_sucking_range;
	}

	public boolean isPlayer_immortality() {
		return player_immortality;
	}

	public void setPlayer_immortality(boolean player_immortality) {
		this.player_immortality = player_immortality;
	}

	public upgradeType getType() {
		return type;
	}

	public void setType(upgradeType type) {
		this.type = type;
	}

	public int getPlayer_life() {
		return player_life;
	}

	public void setPlayer_life(int player_life) {
		this.player_life = player_life;
	}
	
}