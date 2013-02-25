package com.gra.gra;

import java.util.Random;

import android.util.Log;

public class SpaceShip {
	private float x;
	private float y;
	private int height;
	private int width;
	private int speed = 2;
	private float angle;
	private float scale = 0.2f;
	
	//Obrasz w ktorym generowany jest statek
	private int area_x;
	private int area_y;
	private int area_w;
	private int area_h;
	
	//losowa potrzebna do obliczania kata
	private Random rand;
	
	public SpaceShip(float x, float y, int height, int width){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.angle = 0.0f;
		this.rand = new Random();
	}
	
	public void move(){
		this.x += speed * Math.cos(Math.toRadians(angle));
		this.y += speed * Math.sin(Math.toRadians(angle));
		this.angle += 0.4f;//rand.nextFloat();
	}
	
	public boolean checkCollision(float x, float y){
		if(x >= this.x - (this.width * this.scale)/2 && x <= this.x + (this.width* this.scale)/2 && y >= this.y - (this.height* this.scale)/2 && y <= this.y + (this.height* this.scale)/2) return true;
		return false;
	}
	
	
public void calculateProperties(){
    	
    	Random rand = new Random();
    	
    	int interval;
    	
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
    }
    
	public void setBounds(int x, int y, int w, int h){
		this.area_x = x + 60;
		this.area_y = y + 60;
		this.area_w = w - 60;
		this.area_h = h - 60;
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

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public float getAngle() {
		return angle + 270.0f;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public int getID(){
		return 23;
	}

	public int getCurrentFrame() {
		return 0;
	}
	
}
