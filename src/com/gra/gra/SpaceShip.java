package com.gra.gra;

import android.util.Log;

public class SpaceShip {
	private float x;
	private float y;
	private int height;
	private int width;
	private int speed = 3;
	
	public SpaceShip(float x, float y, int height, int width){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void move(){
		this.x += speed;
	}
	
	public boolean checkCollision(float x, float y){
		Log.d("SpaceShip", "X : " + this.x);
		Log.d("SpaceShip", "Y : " + this.y);
		Log.d("SpaceShip", "collision X : " + x);
		Log.d("SpaceShip", "collision Y : " + y);
//		if(Math.pow(Math.pow(x - this.x,2) + Math.pow(y - this.y,2),0.5) <= this.width){
//			return true;
//		}
		//return false;
		if(x >= this.x - this.width/2 && x <= this.x + this.width/2 && y >= this.y - this.height/2 && y <= this.y + this.height/2) return true;
		return false;
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
	
	
}
