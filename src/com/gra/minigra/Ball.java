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

	private GameView view;
	
	private float velocity;		//predkosc kulki
	private float X_speed;		//predkosc w plaszczyznie X
	private float Y_speed;		//predkosc w plaszczyznie Y
	private float X_position;	//pozycja X
	private float Y_position;	//pozycja Y
	private float direction = 0;	//kierunek (stopnie 0 - 360)
	
	private float radius;		//promien
	private float elasticity;	//elastycznosc 0.0 - 1.0
	private float mass;			//masa kulki
	private float gravity;		//grawitacja oddzialujaca na kulke
	
	
	
	private int life;			//zycie kulki
	
	private List<Ball> balls;
	private Paint paint = new Paint();
	
	public Ball(GameView gameView, List<Ball> balls, float x_position, float y_position, float velocity, float direction, float radius, float mass, float elasticity, int life){
		
		this.view = gameView;
		
		this.velocity = velocity;
		this.direction = direction;
		this.X_position = x_position;
		this.Y_position = y_position;
		
		this.X_speed = (float)Math.cos(Math.toRadians(direction)) * this.velocity;
		this.Y_speed = (float)Math.sin(Math.toRadians(direction)) * this.velocity;
		
		this.radius = radius;
		this.elasticity = elasticity;
		this.mass = mass;
		
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
    	//X_speed = (float)Math.cos(direction) * velocity;
    	//Y_speed = (float)Math.sin(direction) * velocity;
    	X_position += X_speed;
    	Y_position += Y_speed;
    	
    	/*
    	 * kolizja ze scianami
    	 */
    	
    	//lewa
    	if(X_position - radius < 0){
    		X_position = radius;							//pozycja X
    		direction = 180 - direction;					//kierunek wedlug zasady kat odbicia = kat padania
    		velocity *= elasticity;							//predkosc pomniejszona o elastycznosc
    		
    		X_speed = (float)Math.cos(Math.toRadians(direction)) * velocity;//wyliczenie predkosci w plaszczyznie X na nowo
        	Y_speed = (float)Math.sin(Math.toRadians(direction)) * velocity;//wyliczenie predkosci w plaszczyznie Y na nowo
    	}
    	//prawa
    	else if(X_position + radius > 480){
    		X_position = 480 - radius;
    		direction = 180 - direction;
    		velocity *= elasticity;
    		
    		X_speed = (float)Math.cos(Math.toRadians(direction)) * velocity;
        	Y_speed = (float)Math.sin(Math.toRadians(direction)) * velocity;
    	}
    	//gorna
    	if(Y_position - radius < 0){
    		Y_position = radius;
    		direction = 360 - direction;
    		velocity *= elasticity;
    		
    		X_speed = (float)Math.cos(Math.toRadians(direction)) * velocity;
        	Y_speed = (float)Math.sin(Math.toRadians(direction)) * velocity;
    	}
    	//dolna
    	else if(Y_position + radius > 800){
    		Y_position = 800 - radius;	
    		direction = 360 - direction;
    		velocity *= elasticity;		
    		
    		X_speed = (float)Math.cos(Math.toRadians(direction)) * velocity;
        	Y_speed = (float)Math.sin(Math.toRadians(direction)) * velocity;
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
//		Log.d("Ball 1", "x = " + this.X_position + " y = " + this.Y_position + " radius = " + this.radius);
//		Log.d("Ball 2", "x = " + x + " y = " + y + " radius = " + radius);
		if((Math.pow(Math.pow(this.X_position - x, 2) + Math.pow(this.Y_position - y, 2),0.5) <= this.radius + radius) ){
			Log.d("Ball", "collision true");
			return true;
		}
		//Log.d("Ball", "collision false");
		return false;
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
	public float getDirection(){
		return this.direction;
	}
	public float getVelocity() {
		return velocity;
	}
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	public float getX_speed() {
		return X_speed;
	}
	public void setX_speed(float x_speed) {
		X_speed = x_speed;
	}
	public float getY_speed() {
		return Y_speed;
	}
	public void setY_speed(float y_speed) {
		Y_speed = y_speed;
	}
	public float getX_position() {
		return X_position;
	}
	public void setX_position(float x_position) {
		X_position = x_position;
	}
	public float getY_position() {
		return Y_position;
	}
	public void setY_position(float y_position) {
		Y_position = y_position;
	}
	public float getElasticity() {
		return elasticity;
	}
	public void setElasticity(float elasticity) {
		this.elasticity = elasticity;
	}
	public float getMass() {
		return mass;
	}
	public void setMass(float mass) {
		this.mass = mass;
	}
	public float getGravity() {
		return gravity;
	}
	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public List<Ball> getBalls() {
		return balls;
	}
	public void setBalls(List<Ball> balls) {
		this.balls = balls;
	}
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public void setDirection(float direction) {
		this.direction = direction;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public void copyBall(Ball ball){
		this.direction = ball.direction;
		this.elasticity = ball.direction;
		this.gravity = ball.gravity;
		this.life = ball.life;
		this.mass = ball.mass;
		this.radius = ball.radius;
		this.velocity = ball.velocity;
		this.X_position = ball.X_position;
		this.Y_position = ball.Y_position;
		this.X_speed = ball.X_speed;
		this.Y_speed = ball.Y_speed;
	}
	public void setColor(int color_value){
		this.paint.setColor(color_value);
	}
}
