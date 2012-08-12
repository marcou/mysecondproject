package com.gra.minigra;

import java.util.List;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * @author Szpada
 * 
 * klasa kula przechowujaca wszystkie zmienne:
 * predkosc X
 * predkosc Y
 * wielkosc
 * sprezystosc
 * pozycje X
 * pozycje Y
 * zycie
 * i inne wlasciwosci
 */
public class Ball{

	private MiniGame1View view;
	
	private float X_position;	//pozycja X
	private float Y_position;	//pozycja Y
	
	private float radius;		//promien
	private float elasticity;	//elastycznosc 0.0 - 1.0
	private float mass;			//masa kulki
	private float gravity;		//grawitacja oddzialujaca na kulke
	
	private float X_speed;		//predkosc w plaszczyznie X
	private float Y_speed;		//predkosc w plaszczyznie Y
	
	private int life;			//zycie kulki
	
	private List<Ball> balls;
	private Paint paint = new Paint();
	
	public Ball(MiniGame1View gameView, List<Ball> balls, float x_position, float y_position, float x_speed, float y_speed, float radius, float mass, float elasticity, int life){
		
		this.view = gameView;
		
		this.X_position = x_position;
		this.Y_position = y_position;
		
		this.radius = radius;
		this.elasticity = elasticity;
		this.mass = mass;
		
		this.X_speed = x_speed;
		this.Y_speed = y_speed;
		
		this.life = life;
		
		this.balls = balls;
		
    	this.paint.setColor(Color.RED);
	}
	public void onDraw(Canvas canvas) {
		canvas.drawCircle(this.X_position, this.Y_position, this.radius, paint);
		update();
	}
    
    private void update(){
    	if(this.life < 1){
    		balls.remove(this);
    	}
    	
		X_position += X_speed;
		Y_position += Y_speed;
    	
	    if(Y_position + radius < 800.0){
	    	Y_speed += gravity;
	    }
	    if(Y_speed == 0){
	    	X_speed *= 0.9;
	    }
	    
	    if(X_position + radius > 480.0) {
	    	X_position = 480.0f - radius;
	        X_speed *= -1.0 * elasticity;
	    }
	    else if(X_position - radius < 0.0) {
	        X_position = radius;
	        X_speed *= -1.0 * elasticity;
	    }
	 
	    if(Y_position + radius > 800.0) {
	    	Y_position = 800.0f - radius;
	        Y_speed *= -1.0 * elasticity;
	        if(-Y_speed < gravity){
	        	Y_speed = 0;
	        }
	    }
	    else if(Y_position - radius < 0.0) {
	    	Y_position = radius;
	        Y_speed *= -1.0 * elasticity;
	    }
    }
    
    public void setGarvity(float gravity){
    	this.gravity = gravity;
    }
    
	public boolean checkCollision(int x, int y){	
		if( (Math.pow(Math.pow(this.X_position - x, 2) + Math.pow(this.Y_position - y, 2),0.5) <= this.radius) ){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean checkBallCollision(float x, float y, float radius){
		if((Math.pow(Math.pow(this.X_position - x, 2) + Math.pow(this.Y_position - y, 2),0.5) <= this.radius + radius) ){
			return true;
		}
		return false;
	}
	
	public void resolveCollision(Ball ball1, Ball ball2){
		
	}
	
	public float getX(){
		return this.X_position;
	}
	public float getY(){
		return this.Y_position;
	}
	public float getRadius(){
		return this.radius;
	}
}
