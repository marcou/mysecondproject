package com.gra.gra;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * @author Szpada
 * 
 * gra w kulki, jeszcze nie wiadomo co to bedzie robilo :/
 *
 */

public class GameView extends SurfaceView{
	
	private Generator generator;		//generator obiektow latajacych
	
	private int default_world_timer = 40;
	private int world_timer = default_world_timer;	//timer swiata, po tym czasie (logicznym) odpalany jest generator
	
	//Pole gry (wieksze od ekranu) na ktorym generuje sie obiekty tak zeby gracz ich nie widzial (nie moga sie przeca nagle pojawiac)
	private int area_x = - 200;
	private int area_y = - 200;
	private int area_w = 680;
	private int area_h = 1000;
	
	private GameLoopThread thread;
	
	private List<Ball> balls = new ArrayList<Ball>();	//lista kulek
	private Earth earth;	//ziemia
	private Player player;	//gracz
	private List<FlyingObject> flyingObjects = new ArrayList<FlyingObject>();//lista obiektow latajacych 
	
	private Paint paint;
	
	private boolean playermoving = false;
	private boolean playerjumping = false;
	private boolean clockwisedirection;
	
	private boolean DEBUG_MODE = false;
	
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
            	   createGenerator();
            	   thread.setRunning(true);
            	   thread.start();
	            }
	            //@Override
	            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
	            	
	            }
        }); 
    }
    public void createGenerator(){
    	this.generator = new Generator(this);
    	generator.setBounds(area_x, area_y, area_w, area_h);
    }
    
    public void createSprites(){
    	Log.d("START PROGRAMU", "=============================================");
    	Log.d("==============", "=============================================");
    	Log.d("START PROGRAMU", "=============================================");
    	paint = new Paint();
    	paint.setColor(Color.BLACK);	//x		y		mass	radius	gravity
    	earth = new Earth	(this, 		240, 	400, 	2000, 	75, 	2.8);
    									//x		y		mass	radius	angle
    	player = new Player	(this, 		240, 	290, 	1, 		10, 	270);
    	player.set_earth(earth.getX(), earth.getY(), earth.getRadius());
    	player.setY((float)(earth.getY() - earth.getRadius() - player.getRadius()));
    	
    	Asteroid a1 = new Asteroid(this,flyingObjects, -50,		850, 	1, 		90, 	5, 		10,		1);
    	Asteroid a2 = new Asteroid(this,flyingObjects, -200, 	500, 	2, 		0, 		5, 		10,		1);
    	Asteroid a3 = new Asteroid(this,flyingObjects, 30, 		700, 	1, 		40, 	5, 		10,		1);
    	
    	Money m1 = new Money	(this,	flyingObjects, -100, 	-100, 	1, 		0, 		50, 	10);
    	Money m2 = new Money	(this,	flyingObjects, 235, 	650, 	0, 		0, 		50, 	10);
    	Money m3 = new Money	(this,	flyingObjects, 40, 		500, 	0, 		0, 		50, 	10);
    	Money m4 = new Money	(this,	flyingObjects, 420, 	450, 	0, 		0, 		50, 	10);
    													//x		y		speed	angle	upgrade type
    	Upgrade  u1 = new Upgrade(this,flyingObjects, 400, 		-80, 	2, 		0, 		upgradeType.armagedon);
    	Upgrade  u2 = new Upgrade(this,flyingObjects, 400, 		600, 	1, 		0, 		upgradeType.ultra_suck);
    	Upgrade  u3 = new Upgrade(this,flyingObjects, 20, 		200, 	1, 		0, 		upgradeType.money_rain);
    	
//    	flyingObjects.add(a1);
//    	flyingObjects.add(a2);
//    	flyingObjects.add(a3);
//    	
    	flyingObjects.add(m1);
//    	flyingObjects.add(m2);
//    	flyingObjects.add(m3);
//    	flyingObjects.add(m4);
//    	
//    	flyingObjects.add(u1);
//    	flyingObjects.add(u2);
    	flyingObjects.add(u3);
 
    	for(int i = 0; i < flyingObjects.size(); i++){
    		flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    	}
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	//odliczanie tajmera
    	world_timer--;
    	canvas.save();
    	//skala do testowania generatora (generuje obiekty poza ekranem)
    	if (DEBUG_MODE) 
    		{
    		canvas.scale(0.5f, 0.5f, 240, 400);
    		}
    	
    	//rysowanie tla
    	paint.setColor(Color.BLACK);
    	canvas.drawRect(-1000, -1000, 4800, 8000, this.paint);	//zmienic na canvas.drawRect(0, 0, 480, 800, this.paint);
    	
    	//zolty prostokat reprezentuje obszar ekranu
    	paint.setColor(Color.YELLOW);
    	
    	paint.setStyle(Paint.Style.STROKE);
    	
    	canvas.drawRect(-1, -1, 480, 800, paint);
    	
    	//czerwony prostokat reprezentuje obszar gry
    	paint.setColor(Color.RED);
    	canvas.drawRect(area_x, area_y, area_w, area_h, paint);
    	
    	paint.setStyle(Paint.Style.FILL_AND_STROKE);
    	
    	canvas.restore();
    	//informacje o graczu
    	paint.setColor(Color.GREEN);
    	canvas.drawText("X : " + player.getX(), 240, 10, paint);
    	canvas.drawText("Y : " + player.getY(), 240, 20, paint);
    	canvas.drawText("Angle : " + player.getAngle(), 240, 30, paint);
    	canvas.drawText("On_Ground : " + player.isOn_ground(), 240, 40, paint);
    	canvas.drawText("Points : " + player.getPoints(), 240, 50, paint);
    	canvas.drawText("Life : " + player.getLife(), 240, 60, paint);
    	canvas.drawText("ARMAGEDON TIMER : " + player.getArmagedon_timer(), 240, 70, paint);
    	canvas.drawText("MONEY RAIN TIMER : " + player.getMoney_rain_timer(), 240, 80, paint);
    	
    	//tajmery
    	paint.setColor(Color.YELLOW);
    	canvas.drawText("Player_timer : " + player.getTimer(), 40, 10, paint);
    	canvas.drawText("Earth_timer  : " + earth.getTimer(), 40, 30, paint);
    	canvas.drawText("World_timer  : " + world_timer, 40, 50, paint);
    	
    	if (DEBUG_MODE) 
		{
		canvas.scale(0.5f, 0.5f, 240, 400);
		}

    	paint.setColor(Color.BLACK);
    	
    	earth.onDraw(canvas);
    	player.onDraw(canvas);
    	
    	for(int i = flyingObjects.size()-1; i >= 0; i--){
    		//jesli obiekt sie zatrzymal (wali konia poza ekranem) to go usun
    		if(flyingObjects.get(i).getSpeed() == 0.0 && !checkVissible(flyingObjects.get(i))){
    			flyingObjects.get(i).setLife(0);
    		}
    		if(!checkIfInArea(flyingObjects.get(i))){
    			flyingObjects.get(i).setLife(0);
    		}
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
    				flyingObjects.get(j).resolveCollision(flyingObjects.get(i));
    			}
    		}
    		/****************************************************
    		 * sprawdzenie kolizji gracza z obiektami latajacymi*
    		 ****************************************************/
    		//sprawdzanie kolizji z pieniedzmi
    		if(flyingObjects.get(i) instanceof Money && flyingObjects.get(i).isOn_ground()){
    			//jesli obiekt to moneta to gracz przyciaga ja do siebie jesli jest na ziemi
    			if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), true)){
    				if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
    					player.resolveCollision(flyingObjects.get(i));
    				}
    				((Money) flyingObjects.get(i)).resolvePlayerGravity(player.getX(), player.getY(), player.getAngle());
    				if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
    					player.resolveCollision(flyingObjects.get(i));
    				}
        		}
    		}
    		else{
    			//z kazdym innym obiektem kolizja przebiega normalnie
    			if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
        			player.resolveCollision(flyingObjects.get(i));
        		}
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
    	
    	/************************************************************************************
    	 * 							GENERATOR TU JEST ODPALANY								*
    	 ************************************************************************************/
    	if(world_timer <= 0){
    		resetTimer();
    		//dodaj graczowi punkty za generacje (przezycie kolejnych x sekund)
    		//player.setPoints(player.getPoints() + 1);
    		
    		List<FlyingObject> temp = generator.generate(player.getPoints(), player.isArmagedon(), player.isMoney_rain());
    		for(int i = 0; i < temp.size(); i++){
    			flyingObjects.add(temp.get(i));
    		}
    		//jesli gracz odpalil generator z armagedonem lub money_rainem wylacz je
	    	if(player.isArmagedon()){
	    		player.setArmagedon_timer(player.getArmagedon_timer() - 1);
	    		if(player.getArmagedon_timer() <= 0){
	    			player.setArmagedon_timer(0);
	    			player.setArmagedon(false);
	    		}
	    	}
	    	if(player.isMoney_rain()){
	    		player.setMoney_rain_timer(player.getMoney_rain_timer() -1);
	    		if(player.getMoney_rain_timer() <= 0){
	    			player.setMoney_rain_timer(0);
	    			player.setMoney_rain(false);
	    		}
	    	}

    	}
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
    		
    		//nadanie zaktualizowanych statystyk ziemi obiektom latajacym
    		for(int i = flyingObjects.size()-1; i >= 0; i--){
    			flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    			if(!flyingObjects.get(i).isOn_ground()){
        			flyingObjects.get(i).resolveGravity(earth.getGravity(), earth.getMass(), earth.getRadius());
        		}
    		}
    		//nadanie zaktualizowanych statystyk ziemi playerowi
    		player.set_earth(earth.getX(), earth.getY(), earth.getRadius());
    		if(!player.isOn_ground()){
    			player.resolveGravity(earth.getGravity(), earth.getMass(), earth.getRadius());
    		}
    	}
    }
    
    public void resetTimer(){
    	world_timer = default_world_timer;
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
        			playerjumping = true;
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
        	event.getX(1);
        	float yy = event.getY(1);
        	if(yy > 400){
        		if(player.isOn_ground()){
        			player.jump();
        			playerjumping = true;
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
    		 else {
    			 playerjumping = false;
    		 }
    		 //
    	 }
    	 
        else if(event.getAction()==MotionEvent.ACTION_POINTER_2_UP) {
         	Log.d(VIEW_LOG_TAG, "SECOND pointer UP");
         	float yy = event.getY(1);
        	if (yy>400) {
        		playerjumping = false;
        	}
         	
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
    
    
    public boolean checkVissible(FlyingObject object){
    	if(object.getX() + object.getRadius() > 0 && object.getX() - object.getRadius() < 480 && object.getY() + object.getRadius() > 0 && object.getY() - object.getRadius() < 800){
    		return true;
    	}
    	return false;
    }
    
    public boolean checkIfInArea(FlyingObject object){
    	if(object.getX() > area_x && object.getX() < area_w && object.getY() > area_y && object.getY() < area_h){
    		return true;
    	}
    	return false;
    }
    
    /************************************************
     * 	NIE UZYWAM TEGO ALE Z SENTYMENTU ZOSTAWIE	*
     ************************************************/
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
    