package com.gra.gra;

import android.util.Log;

public class SpaceShip {
	private float x;
	private float y;
	private int height;
	private int width;
	private int speed = 2;
	private float angle;
	private float scale = 0.4f;
	
	public SpaceShip(float x, float y, int height, int width){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		angle = 0.0f;
	}
	
	public void move(){
		this.x += speed * Math.sin(Math.toRadians(angle));
		this.y += speed * Math.cos(Math.toRadians(angle));
		this.angle += 1.0f;
	}
	
	public boolean checkCollision(float x, float y){
		if(x >= this.x - (this.width * this.scale)/2 && x <= this.x + (this.width* this.scale)/2 && y >= this.y - (this.height* this.scale)/2 && y <= this.y + (this.height* this.scale)/2) return true;
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

	public float getAngle() {
		return angle - 90.0f;
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
	
	
}
