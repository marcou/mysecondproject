package com.gra.gra;

import java.util.ArrayList;
import java.util.List;

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
	
	private List<FlyingObject> objects;
	
	private static double gravity_const = 4.5;	//stala grawitacji ktora ma sprawidz ze grawitacja 
												//bedzie bardziej "miodowa". zwiekszenie tego wspolczynnika
												//zmniejsza grawitacje. Ustaw na 1.0 jesli nie chcesz zeby mial wplyw na cokolwiek
	private float x;
	private float y;
	
	private double speed;
	private double x_speed;		//predkosc w plaszczyznie X
	private double y_speed;		//predkosc w plaszczyznie Y
	
	private int mass;			//masa obiektu
	private int radius;			//promien naszego obiektu
	private double angle;		//kat - godzina 6 to 90stopni, 12 270
	
	private float earth_x;		//pozycja X srodka ziemi
	private float earth_y;		//pozycja Y srodka ziemi
	private int earth_radius;	//promien okregu po jakim ma siê poruszaæ nasz obiekt
	private boolean on_ground = false;	//czy dotyka ziemi
	
	private Paint paint;
	
	private int life = 1;
	
	public FlyingObject(GameView view, List<FlyingObject> objects, float x, float y, double speed, int angle, int mass, int radius){
		this.view = view;
		this.objects = objects;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.angle = angle;
		this.mass = mass;
		this.radius = radius;
		
		x_speed = Math.cos(Math.toRadians(angle)) * speed;
		y_speed = Math.sin(Math.toRadians(angle)) * speed;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	
	public void onDraw(Canvas canvas){
		update();
		canvas.drawCircle(x, y, radius, paint);
	}
	
	//tu odbywa sie animacja sprajta i walenie konia
	public void update(){
		//sprawdzamy czy obiekt "zyje"
		if(life < 1){
			//jesli nie to go usuwamy
			objects.remove(this);
		}
	}
	
	public void set_earth(float x, float y, int radius){
		this.earth_x = x;
		this.earth_y = y;
		this.earth_radius = radius;
		
	}
	
	//metoda obslugujaca ruch obiektu
	public void move(){
		//zmieniamy pozycje x i y o odpowiednie przesuniecia x_speed, y_speed
		x += x_speed;
		y += y_speed;
		//kat ladowania jest odbiciem lustrzanym kata pod jakim podrozuje obiekt
		double landing_angle = 180 + angle;

		//jezeli obiekt znajduje sie na ziemi to przestaje dzialac sila grawitacji
		if(Math.pow((Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2)),0.5) < this.radius + this.earth_radius){
			on_ground = true;
			this.x = (float) (		Math.cos(Math.toRadians(landing_angle)) * (this.radius + this.earth_radius) + this.earth_x		);
			this.y = (float) (		Math.sin(Math.toRadians(landing_angle)) * (this.radius + this.earth_radius) + this.earth_y		);
		}
	}
	
	public void resolveGravity(double gravity, int mass, int radius){
		this.earth_radius = radius;
		double distance = (Math.pow(this.earth_x - this.x,2) + Math.pow(this.earth_y - this.y,2));//odleglosc do kwadratu
		double power = (gravity * mass * this.mass)/(distance * gravity_const);	//wzor na sile grawitacji
		//obliczamy kat beta (miedzy prosta wyznaczona przez srodek obiektu i ziemie a prosta predkosci obiektu
		
		distance = Math.pow(distance, 0.5);	//prawdziwa odleglosc
		
		double cos_beta =  Math.abs((earth_x - x)/distance);
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
		double landing_angle = 0;// = 180 + beta;
		
		//pierwsza cwairtka
		if(this.x >= 240 && this.y >= 400){
			landing_angle = 180 + beta;
		}
		//druga cwiartka
		else if(this.x < 240 && this.y >= 400){
			landing_angle = 360 - beta;
		}
		//trzecia cwiartka
		else if(this.x < 240 && this.y < 400){
			landing_angle = beta;
		}
		//czwarta cwiartka
		else if(this.x >= 240 && this.y < 400){
			landing_angle = 180 - beta;
		}
		beta = landing_angle;
		
		//skladowa X grawitacji
		double x_power = Math.cos(Math.toRadians(beta)) * power;
		//skladowa Y grawitacji
		double y_power = Math.sin(Math.toRadians(beta)) * power;
		
		x_speed = x_speed + x_power;//* 0.999 + x_power;
		y_speed = y_speed + y_power;//* 0.999 + y_power;
		
		angle = beta;
		
		move();
	}
	
	public boolean checkCollision(float x, float y, int radius){
		if(Math.pow(Math.pow(x - this.x,2) + Math.pow(y - this.y,2),0.5) <= this.radius + radius){
			return true;
		}
		return false;
	}
	
	
	public void resolveCollision(FlyingObject object){
		//=====================================================================================================
		//metoda abstrakcyja - bedzie roziwazywana w ramach konkretnych obiektow dziedziczacych po FlyingObject
		//=====================================================================================================
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

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
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

	public List<FlyingObject> getObjects() {
		return objects;
	}

	public void setObjects(List<FlyingObject> objects) {
		this.objects = objects;
	}

	public int getLife() {
		return life;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
}
