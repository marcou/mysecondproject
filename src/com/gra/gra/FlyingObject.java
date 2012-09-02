package com.gra.gra;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 
 * @author Szpada 
 *
 *	Superklasa reprezentujaca wszystkie obiekty latajace - monety, asteroidy i upgrady. W tym
 *	miejscu bedzie skodzony ich ruch i reakcja na grawitacje natomiast juz poszczegolne klasy beda
 *	dziedziczyly po niej i mialy jakies swoje specjalne wlasciwosci.
 *
 *	Kolizja z graczem wspolna dla wszystkich ale rozwiazywana indywidualnie wedlug klas :
 *	asteroida - zabiera zycie. 
 *	moneta - zwiekszanie punktow gracza.
 *	upgrade - zmiana wlasciwosci gracza, ziemi i innych gowien.
 */
public abstract class FlyingObject {
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
	
	private int life_timer = 10;	//po jakim czasie od uderzenia w ziemie obiekt zniknie
	
	//dane potrzebne do wysweitlania bitmapy
	private int width;
	private int height;
	
	private int columns = 1;
	private int rows = 1;
	
	private Bitmap bmp;
	
	private int currentFrame = 0;
	private int frames;
	
	public FlyingObject(GameView view, List<FlyingObject> objects, float x, float y, double speed, double angle, int mass, int radius){
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
		
		int srcX = 0;
    	int srcY = 0;
    	int row;
    	
    	srcX = (currentFrame % (this.columns)) * this.width;
        row = currentFrame / (this.rows + 1);
        srcY = row * this.height;
        
        Rect src = new Rect(srcX, srcY, srcX + this.width, srcY + this.height);
		Rect dst = new Rect((int)x - width/2, (int)y - width * 2/5, (int)x + width /2, (int)y + width * 2/5);
		canvas.drawBitmap(bmp, src, dst, paint);
		
		currentFrame++;
		
		if(this.currentFrame > this.frames){
			currentFrame = 0;
		}
	}
	
	//tu odbywa sie animacja sprajta i walenie konia
	public abstract void update();
	
	public void set_earth(float x, float y, int radius){
		this.earth_x = x;
		this.earth_y = y;
		this.earth_radius = radius;
		
	}
	
	//metoda obslugujaca ruch na ziemi - turlanie sie albo przyciaganie do gracza monet
	public void moveOnGround(int speed){
		angle += speed;
		if(angle >= 360){
			this.angle = angle - 360;
		}
		else if(angle <= 0){
			this.angle = 360 + angle;
		}
		this.x = (float) (		Math.cos(Math.toRadians(angle)) * (this.radius + this.earth_radius) + this.earth_x		);
		this.y = (float) (		Math.sin(Math.toRadians(angle)) * (this.radius + this.earth_radius) + this.earth_y		);
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
			angle = landing_angle;
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
		double x_power = Math.cos(Math.toRadians(beta)) * (power/3);	//-> dwukrotne spowolnienie (za szybko chodzi)
		//skladowa Y grawitacji
		double y_power = Math.sin(Math.toRadians(beta)) * (power/3);
		
		//wersja bez wytracania predkosci
		
		//x_speed += x_power;
		//y_speed += y_power;
		
		//wersja z wytracaniem predkosci
		x_speed = x_speed * 0.999 + x_power;//+=x_speed + x_power;//*= 0.999 + x_power;
		y_speed = y_speed * 0.999 + y_power;//+=y_speed + y_power;//*= 0.999 + y_power;
		
		angle = beta;
		
		move();
	}
	
	public boolean checkCollision(float x, float y, int radius){
		if(Math.pow(Math.pow(x - this.x,2) + Math.pow(y - this.y,2),0.5) <= this.radius + radius){
			return true;
		}
		return false;
	}
	
	
	public abstract void resolveCollision(FlyingObject object); 	
	//=====================================================================================================
	//metoda abstrakcyja - bedzie roziwazywana w ramach konkretnych obiektow dziedziczacych po FlyingObject
	//=====================================================================================================


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

	public int getLife_timer() {
		return life_timer;
	}

	public void setLife_timer(int life_timer) {
		this.life_timer = life_timer;
	}
	public void setBmpData(Bitmap bmp, int columns, int rows){
		this.bmp = bmp;
		this.width = bmp.getWidth()/columns;
		this.height = bmp.getHeight()/rows;
		this.columns = columns;
		this.rows = rows;
		this.frames = (columns * rows) -1;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getFrames() {
		return frames;
	}

	public void setFrames(int frames) {
		this.frames = frames;
	}

	public void setBmpData(int a_columns, int a_rows) {
		this.columns = a_columns;
		this.rows = a_rows;
		this.frames = (a_columns * a_rows) - 1;
	}
	
}
