package com.gra.gra;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
/**
 * 
 * @author Szpada
 * 
 * gra w kulki, jeszcze nie wiadomo co to bedzie robilo :/
 *
 */

public class GameView extends SurfaceView{
	
	//test do przyciskow
	private boolean down = false;
	private boolean up = false;
	private long startTime=0;
	private long endTime=0;
	private long touchTime;
	
	
	private GameLoopThread thread;
	
	private List<Ball> balls = new ArrayList<Ball>();	//lista kulek
	private Earth earth;	//ziemia
	private Player player;	//gracz
	private List<FlyingObject> flyingObjects = new ArrayList<FlyingObject>();//lista obiektow latajacych 
	
	private Paint paint;
	
	private long coolDown = 0;	//co ile mozna kliknac w ekran
	private long lastClick;	//czas ostatniego klikniecia
	private boolean playermoving = false;
	private boolean clockwisedirection;
	
    public GameView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setLongClickable(true);
        //this.setOnTouchListener(this);
    	thread = new GameLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {
               //@Override
               public void surfaceDestroyed(SurfaceHolder holder) {
                      boolean retry = true;
                      thread.setRunning(false);
                      while (retry) {
                             try {
                            	 thread.join();
                                   retry = false;
                             } catch (InterruptedException e) {}
                      }
               }
               //@Override
               public void surfaceCreated(SurfaceHolder holder) {
            	   createSprites();
            	   thread.setRunning(true);
            	   thread.start();
	            }
	            //@Override
	            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
	            	
	            }
        }); 
    }
    public void createSprites(){
    	Log.d("START PROGRAMU", "=============================================");
    	Log.d("==============", "=============================================");
    	Log.d("START PROGRAMU", "=============================================");
    	paint = new Paint();
    	paint.setColor(Color.BLACK);	//x		y		mass	radius	gravity
    	earth = new Earth	(this, 		240, 	400, 	400, 	100, 	2.8);
    									//x		y		mass	radius	angle
    	player = new Player	(this, 		240, 	290, 	1, 		10, 	270);
    	player.set_earth(earth.getX(), earth.getY(), earth.getRadius());
    	player.setY(earth.getY() - earth.getRadius() - player.getRadius());
    													//x		y		speed	angle	mass	radius
    	Asteroid a1 = new Asteroid(this,flyingObjects, 10, 		10, 	1, 		90, 	10, 	10);
    	Asteroid a2 = new Asteroid(this,flyingObjects, 50, 		500, 	2, 		0, 		10, 	10);
    	Asteroid a3 = new Asteroid(this,flyingObjects, 30, 		700, 	1, 		40, 	10, 	10);
    	
    	Money m1 	= new Money	(this,	flyingObjects, 245, 	700, 	0, 		0, 		50, 	10);
    	Money m2 	= new Money	(this,	flyingObjects, 235, 	650, 	0, 		0, 		50, 	10);
    	Money m3 	= new Money	(this,	flyingObjects, 40, 		500, 	0, 		0, 		50, 	10);
    	Money m4 	= new Money	(this,	flyingObjects, 420, 	450, 	0, 		0, 		50, 	10);
    													//x		y		speed	angle	upgrade type
    	Upgrade  u1 = new Upgrade(this,flyingObjects, 100, 		10, 	0, 		0, 		upgradeType.high_gravity);
    	Upgrade  u2 = new Upgrade(this,flyingObjects, 400, 		700, 	0, 		0, 		upgradeType.low_gravity);
    	Upgrade  u3 = new Upgrade(this,flyingObjects, 700, 		400, 	0, 		0, 		upgradeType.speed);
    	
//    	flyingObjects.add(a1);
//    	flyingObjects.add(a2);
//    	flyingObjects.add(a3);
//    	
//    	flyingObjects.add(m1);
//    	flyingObjects.add(m2);
//    	flyingObjects.add(m3);
//    	flyingObjects.add(m4);
    	
    	flyingObjects.add(u1);
    	flyingObjects.add(u2);
    	flyingObjects.add(u3);
    	
    	FlyingObject fo1 = 							//x		y		speed	angle	mass	radius
    	new FlyingObject	(this, flyingObjects,	20, 	10, 	12.0, 	30, 	20, 	10);
    	//flyingObjects.add(fo1);
    	
    	FlyingObject fo2 = 							//x		y		speed	angle	mass	radius
    	   new FlyingObject	(this, flyingObjects, 	210, 	700, 	14.0, 	90, 	20, 	10);
    	//flyingObjects.add(fo2);
    	
    	FlyingObject fo3 = 							//x		y		speed	angle	mass	radius
    	   new FlyingObject	(this, flyingObjects,	430, 	200, 	13.0, 	0, 		20, 	10);   	    	
    	//flyingObjects.add(fo3);
    	
    	FlyingObject fo4 = 							//x		y		speed	angle	mass	radius
    	   new FlyingObject	(this, flyingObjects,	430, 	600, 	13.0, 	130, 	20, 	10);
    	//flyingObjects.add(fo4);
    	
    	for(int i = 0; i < flyingObjects.size(); i++){
    		flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    	}
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	canvas.drawRect(0, 0, 480, 800, this.paint);
    	
    	//informacje o graczu
    	paint.setColor(Color.GREEN);
    	canvas.drawText("X : " + player.getX(), 240, 10, paint);
    	canvas.drawText("Y : " + player.getY(), 240, 20, paint);
    	canvas.drawText("Angle : " + player.getAngle(), 240, 30, paint);
    	canvas.drawText("On_Ground : " + player.isOn_ground(), 240, 40, paint);
    	canvas.drawText("Points : " + player.getPoints(), 240, 50, paint);
    	
    	//tajmery
    	paint.setColor(Color.YELLOW);
    	canvas.drawText("Player_timer : " + player.getTimer(), 40, 10, paint);
    	canvas.drawText("Earth_timer  : " + earth.getTimer(), 40, 40, paint);
    	
    	//informacje o asteroidzie
//    	paint.setColor(Color.RED);
//    	for(int i = flyingObjects.size()-1; i >= 0; i--){
//    		canvas.drawText("X : " + flyingObjects.get(i).getX(), i * 120, 10, paint);
//        	canvas.drawText("Y : " + flyingObjects.get(i).getY(), i * 120, 20, paint);
//        	canvas.drawText("Angle : " + (int)flyingObjects.get(i).getAngle(), i * 120, 30, paint);
//        	canvas.drawText("On_Ground : " + flyingObjects.get(i).isOn_ground(), i * 120, 40, paint);
//    	}

    	paint.setColor(Color.BLACK);
    	
    	earth.onDraw(canvas);
    	player.onDraw(canvas);
    	
    	for(int i = flyingObjects.size()-1; i >= 0; i--){
    		//rysowanie obiektow
    		flyingObjects.get(i).onDraw(canvas);
    		//przesuwanie obiektow
    		if(!flyingObjects.get(i).isOn_ground()){
    			flyingObjects.get(i).resolveGravity(earth.getGravity(), earth.getMass(), earth.getRadius());
    		}
    		//sprawdzenie kolizji miedzy obiektami latajacymi
    		for(int j = i - 1; j >= 0; j--){
    			if(flyingObjects.get(i).checkCollision(flyingObjects.get(j).getX(), flyingObjects.get(j).getY(), flyingObjects.get(j).getRadius())){
    				//rozwiazanie kolizji
    				//FlyingObject temp = flyingObjects.get(i);
    				flyingObjects.get(j).resolveCollision(flyingObjects.get(i));
    				//flyingObjects.get(i).resolveCollision(flyingObjects.get(j));
    			}
    		}
    		//sprawdzenie kolizji gracza z obiektami latajacymi
    		if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius())){
    			player.resolveCollision(flyingObjects.get(i));
    			//flyingObjects.get(i).resolveCollision(null);
    		}
    	}
    	for(int i = flyingObjects.size()-1; i >= 0; i--){
			//sprawdzamy czy obiekt "zyje"
			if(flyingObjects.get(i).getLife() < 1){
				//jesli nie to go usuwamy
				flyingObjects.remove(flyingObjects.get(i));
			}
    	}
    	
    	if (playermoving) {
    		movePlayer();
    	}
    	resolveGravity();
    	//jesli statystyki ziemi zostaly zmienione zassaj je na nowo a nastepnie ustaw flage na false
    	if(earth.isSuck_my_stats()){
    		for(int i = flyingObjects.size()-1; i >= 0; i--){
    			flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    		}
    	}
    	if(player.isEarth_stats_changed()){
    		earth.resetUpgrade();
    		earth.setUpgrade(player.getEarthTimer(), player.getEarth_radius_multiplier(), player.getEarth_gravity_multiplier());
    		//wyzerowanie mnoznikow statystyk ziemi
    		player.setEarth_gravity_multiplier(1.0);
    		player.setEarth_radius_multiplier(1);
    		//ustawienie flagi blokujacej odczyt statystyk
    		player.setEarth_stats_changed(false);
    	}
    }
    
    public void resolveGravity(){
    	if(!player.isOn_ground()){
    		player.resolveGravity(earth.getGravity(), earth.getMass(), earth.getRadius());
    	}
    }
    
    public void checkCollision(){
    	for(int i = 0; i < balls.size(); i++){
    		for(int j = i + 1; j < balls.size(); j++){
    			if(balls.get(i).checkBallCollision(balls.get(j).getX(), balls.get(j).getY(), balls.get(j).getRadius())){
    				resolveCollision(balls.get(i), balls.get(j));
    			}
    		}
    	}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	//Log.d(VIEW_LOG_TAG, "Pointer Num:" + Integer.toString(event.getPointerId(0)));
    	
    	float x = event.getX();
        float y = event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
        	Log.d(VIEW_LOG_TAG, "Touch DOWN");
        	if(y > 400){
        		if(player.isOn_ground()){
        			player.jump();
        			Log.d(VIEW_LOG_TAG, "Jump pressed");
        		}
        	}
        	else{
        		playermoving = true;
	        	if(x < 240){
		        	//player.move(false);
	        		clockwisedirection = false;
		        	Log.d(VIEW_LOG_TAG, "less than");
	        	}
	        	if(x > 240){
	        		clockwisedirection = true;
		        	//player.move(true);
		        	Log.d(VIEW_LOG_TAG, "more than");
	        	}
        	}
    	 }
        
        else if(event.getAction()==MotionEvent.ACTION_POINTER_2_DOWN) {
        	Log.d(VIEW_LOG_TAG, "SECOND pointer down");
        	float xx = event.getX(1);
        	float yy = event.getY(1);
        	if(yy > 400){
        		if(player.isOn_ground()){
        			player.jump();
        			Log.d(VIEW_LOG_TAG, "Jump pressed");
        		}
        	}
        }
        
        else if(event.getAction()==MotionEvent.ACTION_UP){
    		 Log.d(VIEW_LOG_TAG, "Touch UP");
    		 if (y<400) {
    			 playermoving = false;
    			 Log.d(VIEW_LOG_TAG, "Player stopped");
    		 }
    		 //
    	 }
    	 
        else if(event.getAction()==MotionEvent.ACTION_POINTER_2_UP) {
         	Log.d(VIEW_LOG_TAG, "SECOND pointer UP");
         }
        
        else if (event.getAction()==MotionEvent.ACTION_MOVE) {
        	
        }
        
        
        else {
        	Log.d(VIEW_LOG_TAG,"Sth else Action " + Integer.toString(event.getAction()));
        	Log.d(VIEW_LOG_TAG, "Sth else Index" + Integer.toString(event.getActionIndex()));
        	 
        }
    	 
    		return super.onTouchEvent(event);		
    	}
    	

    public void movePlayer() {
    	player.move(clockwisedirection);
    }
    
    
    
    public void resolveCollision(Ball ball1, Ball ball2){
    	/*
    	 * kolizje 2D moja wersja :/
    	 */
    	float u1x = ball1.getX_speed();
    	float u1y = ball1.getY_speed();
    	
    	float u2x = ball2.getX_speed();
    	float u2y = ball2.getY_speed();
    	
    	float m1 = ball1.getMass();
    	float m2 = ball2.getMass();
    	

    	float v1x = (u1x*(m1 - m2) + 2 * m2 * u2x)/(m1 + m2);
    	float v1y = (u1y*(m1 - m2) + 2 * m2 * u2y)/(m1 + m2);
    	
    	float v2x = (u2x*(m2 - m1) + 2 * m1 * u1x)/(m1 + m2);
    	float v2y = (u2y*(m2 - m1) + 2 * m1 * u1y)/(m1 + m2);
    	
    	ball1.setX_speed(v1x);
    	ball1.setY_speed(v1y);
    	
    	ball2.setX_speed(v2x);
    	ball2.setY_speed(v2y);
    	
    	ball1.setVelocity((float)(Math.pow(Math.pow(v1x, 2) + Math.pow(v1y, 2),0.5)));
    	ball2.setVelocity((float)(Math.pow(Math.pow(v2x, 2) + Math.pow(v2y, 2),0.5)));
    	
    	if(v1y != 0){
    		ball1.setDirection((float)(Math.atan((v1x/v1y))));	//zmienic na radiany jak sie bedzie dupilo!
    	}
    	else{
    		ball1.setDirection(0);
    	}
    	if(v2y != 0){
    		ball2.setDirection((float)(Math.atan((v2x/v2y))));
    	}
    	else{
    		ball2.setDirection(0);
    	}
	}
}
    