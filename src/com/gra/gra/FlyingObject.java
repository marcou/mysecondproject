package com.gra.gra;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * 
 * @author Szpada 
 *
 *	Klasa reprezentujaca wszystkie obiekty latajace - monety, asteroidy i upgrady. W tym
 *	miejscu bedzie skodzony ich ruch i reakcja na grawitacje natomiast juz poszczegolne obiekty beda
 *	dziedziczyly po niej i mialy jakies swoje specjalne wlasciwosci.
 *
 *	Kolizja z graczem wspolna dla wszystkich ale rozwiazywana indywidualnie wedlug klas :
 *	asteroida - zabiera zycie. 
 *	moneta - zwiekszanie punktow gracza.
 *	upgrade - zmiana wlasciwosci gracza, ziemi i innych gowien.
 */
public class FlyingObject {
	private GameView view;
	
	private float x;
	private float y;
	
	private double speed;
	private double x_speed;		//predkosc w plaszczyznie X
	private double y_speed;		//predkosc w plaszczyznie Y
	
	private int mass;			//masa obiektu
	private int radius;			//promien naszego obiektu
	private int angle;			//kat - godzina 6 to 90stopni, 12 270
	
	private float earth_x;		//pozycja X srodka ziemi
	private float earth_y;		//pozycja Y srodka ziemi
	private int earth_radius;	//promien okregu po jakim ma siê poruszaæ nasz obiekt
	private boolean on_ground = false;	//czy dotyka ziemi
	
	private Paint paint;
	
	public FlyingObject(GameView view, float x, float y, double speed, int angle, int mass, int radius){
		this.view = view;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.angle = angle;
		this.mass = mass;
		this.radius = radius;
		
		x_speed = Math.cos(Math.toRadians(angle)) * speed;
		y_speed = Math.sin(Math.toRadians(angle)) * speed;
		
		paint = new Paint();
		paint.setColor(Color.RED);
	}
	
	public void onDraw(Canvas canvas){
		canvas.drawCircle(x, y, radius, paint);
	}
	
	public void set_earth(float x, float y, int radius){
		this.earth_x = x;
		this.earth_y = y;
		this.earth_radius = radius;
		
	}
	
	//metoda obslugujaca ruch obiektu
	public void move(){
		//znajac kat rozbijamy predkosc na skladowe x i y a nastepnie wykonujemy ruch
		//x_speed = Math.cos(Math.toRadians(angle)) * speed;
		//y_speed = Math.sin(Math.toRadians(angle)) * speed;
		x += x_speed;
		y += y_speed;
		
		//jezeli obiekt znajduje sie na ziemi to przestaje dzialac sila grawitacji
		if(Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5) < this.radius + this.earth_radius){
			on_ground = true;
			this.x = (float) (		Math.cos(Math.toRadians(180 + this.angle)) * (this.radius + this.earth_radius) + this.earth_x		);
			this.y = (float) (		Math.sin(Math.toRadians(180 + this.angle)) * (this.radius + this.earth_radius) + this.earth_y		);
		}
	}
	
	public void resolveGravity(double gravity, int mass, int radius){
		this.earth_radius = radius;
		double distance = (Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2));//odleglosc do kwadratu
		double power = (gravity * mass * this.mass)/(distance);	//wzor na sile grawitacji
		//obliczamy kat beta (miedzy prosta wyznaczona przez srodek obiektu i ziemie a prosta predkosci obiektu
		
		distance = Math.pow(distance, 0.5);	//prawdziwa odleglosc
		
		double cos_beta =  Math.abs((earth_x - x)/distance);
//		if(earth_x - x < 0){
//			cos_beta =  (x - earth_x)/distance;
//		}
		double beta = Math.toDegrees(Math.acos((cos_beta)));
		
		/*
		 * 	|	3cw	|	4cw	|
		 * 	|-------|-------|
		 * 	|	2cw	|	1cw	|
		 * 
		 */
		
		/*
		 * w cwiarcte :
		 * 
		 * I	-	beta = 180 + alfa
		 * II	-	beta = 360 - alfa
		 * III	-	beta = alfa
		 * IV	- 	beta = 180 - alfa
		 */
		
		//pierwsza cwairtka
		if(this.x > 240 && this.y >= 400){
			beta = beta + 180;
		}
		//druga cwiartka
		else if(this.x < 240 && this.y >= 400){
			beta = 360 - beta;
		}
		//czwarta cwiartka
		else if(this.x >= 240 && this.y < 400){
			beta = 180 - beta;
		}
		
		//skladowa X grawitacji
		double x_power = Math.cos(Math.toRadians(beta)) * power;
		//skladowa Y grawitacji
		double y_power = Math.sin(Math.toRadians(beta)) * power;
		
		x_speed += x_power;
		y_speed += y_power;
		
		
		//trzecia cwiartka
		angle = (int)beta;
		Log.d("FO", "kat : " + angle);
		
		move();
	}

	public GameView getView() {
		return view;
	}

	public void setView(GameView view) {
		this.view = view;
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

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getX_speed() {
		return x_speed;
	}

	public void setX_speed(double x_speed) {
		this.x_speed = x_speed;
	}

	public double getY_speed() {
		return y_speed;
	}

	public void setY_speed(double y_speed) {
		this.y_speed = y_speed;
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

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public float getEarth_x() {
		return earth_x;
	}

	public void setEarth_x(float earth_x) {
		this.earth_x = earth_x;
	}

	public float getEarth_y() {
		return earth_y;
	}

	public void setEarth_y(float earth_y) {
		this.earth_y = earth_y;
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

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
}
