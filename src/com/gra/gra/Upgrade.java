package com.gra.gra;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

enum upgradeType{speed, low_gravity, high_gravity, tiny_player, huge_player};

public class Upgrade extends FlyingObject {

	private String tag;
	
	private Paint paint;
	
	private long time;	//czas przez jaki upgrade dziala
	
	private static int mass = 20;
	private static int radius = 10;
	
	//MODYFIKATORY STATSOW 
	//przez nie przemnazamy wszystkei statystyki (ustawinie 1.0 nie zmienia ich wcale)
	private double earth_gravity = 1.0;	// mno¿nik grawitacji
	private double earth_radius = 1.0;		
	
	private double player_jump_power = 1.0;
	private double player_point_multiplier = 1;
	private double player_speed = 1;
	private double player_radius = 1;
	private boolean armagedon; 
	
	public Upgrade(GameView view, List<FlyingObject> objects, float x, float y, double speed, int angle, upgradeType type) {
		super(view, objects, x, y, speed, angle, mass, radius);
		
		this.paint = new Paint();
		paint.setColor(Color.GREEN);
		
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
		}
	}
	
	@Override
	public void resolveCollision(FlyingObject object){
		//kolizja upgradeu z asteroida lub graczem
		if(object instanceof Asteroid || object == null){
			super.getObjects().remove(this);
		}
		//kolizja upgradeu z pieniedzmi
		else if(object instanceof Money){
			
		}
	}
	
	@Override
	public void onDraw(Canvas canvas){
		update();
		canvas.drawText(this.tag, super.getX() - super.getRadius(), super.getY() - super.getRadius() - 16, paint);
		canvas.drawCircle(super.getX(), super.getY(), super.getRadius(), paint);
	}

	public void update(){
		//jesli obiekt dotyka ziemi usun go po czasie "life_timer"
		if(super.isOn_ground()){
			super.setLife_timer(super.getLife_timer() - 1);
			if(super.getLife_timer() < 0){
				super.setLife(0);
			}
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
	
}