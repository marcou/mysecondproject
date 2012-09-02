package com.gra.gra;

import java.util.ArrayList;
import java.util.List;

import com.gra.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
	
	private float w_factor;
	private float h_factor;
	
	private Generator generator;		//generator obiektow latajacych
	
	private int default_world_timer = 80;
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
	
	private List<TempSprite> temps = new ArrayList<TempSprite>();//lista obiektow ktore znikaja po krotkim czasie (dym)
	
	private Paint paint;
	
	private boolean playermoving = false;
	private boolean playerjumping = false;
	private boolean clockwisedirection;
	
	private boolean DEBUG_MODE = false;
	
	/*
	 * ZESTAW BITMAP DO RYSOWANIA WRAZ Z DANYMI (LICZBA KOLUMN I RZEDOW)
	 */
	private Bitmap asteroid_bmp;
	private int a_columns = 4;
	private int a_rows = 4;
	
	private Bitmap money_bmp;
	private int m_columns = 4;
	private int m_rows = 4;
	
	private Bitmap upgrade_bmp;
	private int u_columns = 1;
	private int u_rows = 1;
	
	private Bitmap earth_bmp;
	private int e_columns = 1;
	private int e_rows = 1;
	
	private Bitmap thorn_bmp;
	private int t_columns = 1;
	private int t_rows = 1;
	
	private Bitmap smoke_bmp;
	private int s_columns = 4;
	private int s_rows = 4;
	
	private Bitmap background_bmp;
	private int background_x = 0;
	
	
    public GameView(Context context, double w_factor, double h_factor) {
        super(context);
        
        this.h_factor = (float)h_factor;
	   	this.w_factor = (float)w_factor;
        
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
    	/*
    	 * inicjowanie bitmap
    	 */
    	asteroid_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.asteroids);
    	money_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.gemstone);
    	upgrade_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.upg);
    	earth_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.earthcrap);
    	thorn_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.kolce);
    	background_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.gownotlo);
    	smoke_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.dym);
    	
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
    	Upgrade  u3 = new Upgrade(this,flyingObjects, 20, 		200, 	1, 		0, 		upgradeType.low_gravity);
    	
    	GroundEnemy e1  = new GroundEnemy(this, flyingObjects, 0, 0, 2, 90, 1, 10);
    	
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
    	
    	flyingObjects.add(e1);
 
    	temps.add(new TempSprite(temps, this, 0, 0, 0, 1, 16));
    	
    	for(int i = 0; i < flyingObjects.size(); i++){
    		flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    		
    		if(flyingObjects.get(i) instanceof Asteroid){
				flyingObjects.get(i).setBmpData(a_columns, a_rows);
			}
			else if(flyingObjects.get(i) instanceof Money){
				flyingObjects.get(i).setBmpData(m_columns, m_rows);
			}
			else if(flyingObjects.get(i) instanceof Upgrade){
				flyingObjects.get(i).setBmpData(u_columns, u_rows);
			}
			else if(flyingObjects.get(i) instanceof GroundEnemy){
				flyingObjects.get(i).setBmpData(t_columns, t_rows);
			}
    	}
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	//SKALOWANIE
    	canvas.scale(this.w_factor, this.h_factor);
    	//odliczanie tajmera
    	world_timer--;
    	canvas.save();
    	//skala do testowania generatora (generuje obiekty poza ekranem)
    	if (DEBUG_MODE) 
    		{
    		canvas.scale(0.5f, 0.5f, 240, 400);
    	}
    	
    	//rysowanie tla
    	paint.setColor(Color.WHITE);
    	//wersja bez grafiki
    	canvas.drawRect(-1000, -1000, 4800, 8000, this.paint);
    	//wersja z grafika
    	//drawBackground(canvas);
    	
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
    	canvas.drawText("Immortality : " + player.isImmortal(), 240, 90, paint);
    	
    	//tajmery
    	paint.setColor(Color.YELLOW);
    	canvas.drawText("Player_timer : " + player.getTimer(), 40, 10, paint);
    	canvas.drawText("Earth_timer  : " + earth.getTimer(), 40, 30, paint);
    	canvas.drawText("World_timer  : " + world_timer, 40, 50, paint);
    	canvas.drawText("Immortality_timer  : " + player.getImmortality_timer(), 40, 70, paint);
    	
    	if (DEBUG_MODE) 
		{
		canvas.scale(0.5f, 0.5f, 240, 400);
		}

    	paint.setColor(Color.BLACK);
    	
    	/*
    	 * RYSOWANIE ZIEMI I PLAYER
    	 */
    	//wersja bez spriteow
    	//earth.onDraw(canvas);
    	//player.onDraw(canvas);
    	//wersja ze spritami
    	drawSprite(canvas, (int)earth.getX(), (int)earth.getY(), e_columns, e_rows, earth_bmp.getWidth()/e_columns, earth_bmp.getHeight()/e_rows, earth.getCurrentFrame(), earth_bmp, 0, true);
    	earth.update();
    	player.onDraw(canvas);
    	
    	//rysowanie dymu
    	for(int i = temps.size()-1; i >=0; i--){
    		Log.d("TEMP SPRITE", "frame : " + temps.get(i).getCurrentFrame());
    		drawSprite(canvas, (int)temps.get(i).getX(),  (int)temps.get(i).getY(), s_columns, s_rows, smoke_bmp.getWidth()/s_columns, smoke_bmp.getHeight()/s_rows, temps.get(i).getCurrentFrame(), smoke_bmp, 0, false);
    		drawSprite(canvas, 240,  400, s_columns, s_rows, smoke_bmp.getWidth()/s_columns, smoke_bmp.getHeight()/s_rows, temps.get(i).getCurrentFrame(), smoke_bmp, 0, false);
    		temps.get(i).update();
    	}
    	
    	for(int i = flyingObjects.size()-1; i >= 0; i--){
    		//jesli obiekt sie zatrzymal (wali konia poza ekranem) to go usun
    		if(flyingObjects.get(i).getSpeed() == 0.0 && !checkVissible(flyingObjects.get(i))){
    			flyingObjects.get(i).setLife(0);
    		}
    		//jesli obiekt wylecial poza obszar gry
    		if(!checkIfInArea(flyingObjects.get(i))){
    			flyingObjects.get(i).setLife(0);
    		}
    		//rysowanie obiektow (tylko tych widocznych - ekran)
    		if(checkVissible(flyingObjects.get(i))){
    			//rysowanie kulek
    			//flyingObjects.get(i).onDraw(canvas);
    			
    			/*==================
    			 * rysowanie spritow
    			 *==================*/
    			 
    			//rysowanie asteroid
    			if(flyingObjects.get(i) instanceof Asteroid){
    				drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), 	a_columns, a_rows,  asteroid_bmp.getWidth()/a_columns, asteroid_bmp.getHeight()/a_rows,  flyingObjects.get(i).getCurrentFrame(), asteroid_bmp, (float)flyingObjects.get(i).getAngle(), false);
    			}
    			//rysowanie hajsu
    			else if(flyingObjects.get(i) instanceof Money){
    				drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(),  m_columns, m_rows,  money_bmp.getWidth()/m_columns, money_bmp.getHeight()/m_rows,  flyingObjects.get(i).getCurrentFrame(), money_bmp, (float)flyingObjects.get(i).getAngle(), false);
    			}
    			//rysowanie upgradeow
    			else if(flyingObjects.get(i) instanceof Upgrade){
    				drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(),  u_columns, u_rows,  upgrade_bmp.getWidth()/u_columns, upgrade_bmp.getHeight()/u_rows,  flyingObjects.get(i).getCurrentFrame(), upgrade_bmp, (float)flyingObjects.get(i).getAngle(), false);
    			}
    			else if(flyingObjects.get(i) instanceof GroundEnemy){
    				drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(),  t_columns, t_rows,  thorn_bmp.getWidth()/t_columns, thorn_bmp.getHeight()/t_rows,  flyingObjects.get(i).getCurrentFrame(), thorn_bmp, (float)flyingObjects.get(i).getAngle(), true);
    			}
    			//update (klatki ++)
    			flyingObjects.get(i).update();
    		}
    		//przesuwanie obiektow
    		if(!flyingObjects.get(i).isOn_ground()){
    			float temp_x = flyingObjects.get(i).getX();
    			float temp_y = flyingObjects.get(i).getY();
    			
    			flyingObjects.get(i).resolveGravity(earth.getGravity(), earth.getMass(), earth.getRadius());
    			//narysuj warkocz za asteroida
    			if(flyingObjects.get(i) instanceof Asteroid){
    				//temps.add(new TempSprite(temps, this, flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getAngle(), asteroid_bmp.getWidth()/a_columns, smoke_bmp.getWidth()/s_columns));
    				temps.add(new TempSprite(temps, this, temp_x, temp_y));
    			}
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
    				//jesi obiekt styka sie z naszym obiektem to go zasysamy
    				if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
    					player.resolveCollision(flyingObjects.get(i));
    				}
    				((Money) flyingObjects.get(i)).resolvePlayerGravity(player.getX(), player.getY(), player.getAngle());
    				//sprawdzamy czy po przemieszczdeniu monety obiekt przypadkiem si� nie styka z naszym obiektem, jesli tak to go zasysamy
    				if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
    					player.resolveCollision(flyingObjects.get(i));
    				}
        		}
    		}
    		//zasysanie monety w locie
    		else if(flyingObjects.get(i) instanceof Money && !flyingObjects.get(i).isOn_ground()){
    			if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
					player.resolveCollision(flyingObjects.get(i));
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
    		
    		flyingObjects.addAll(generator.generate(player.getPoints(), player.isArmagedon(), player.isMoney_rain()));
    		//List<FlyingObject> temp = generator.generate(player.getPoints(), player.isArmagedon(), player.isMoney_rain());
    		
    		//zaktualizowanie danych
    		for(int i = 0; i < flyingObjects.size(); i++){
    			flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    			
    			if(flyingObjects.get(i) instanceof Asteroid){
    				flyingObjects.get(i).setBmpData(a_columns, a_rows);
    			}
    			else if(flyingObjects.get(i) instanceof Money){
    				flyingObjects.get(i).setBmpData(m_columns, m_rows);
    			}
    			else if(flyingObjects.get(i) instanceof Upgrade){
    				flyingObjects.get(i).setBmpData(u_columns, u_rows);
    			}
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
        //SKALOWANIE
        x = x / this.w_factor;
        y = y / this.h_factor;
        
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
    
    //width -> szerokosc bitmapy podzielona przez rows
    public void drawSprite(Canvas canvas, int x, int y, int columns, int rows, int width, int height, int currentFrame, Bitmap bmp, float angle, boolean thorn){
    	if(thorn){
    		canvas.save();
    		canvas.rotate(angle, x, y);
    	}
    	int srcX = 0;
    	int srcY = 0;
    	int row;
    	srcX = (currentFrame % (columns)) * width;
        row = currentFrame / (rows + 1);
        srcY = row * height;
        
    	Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x - width/2, y - width/2, x + width/2, y + width/2);
		canvas.drawBitmap(bmp, src, dst, paint);
		if(thorn){
			canvas.restore();
		}
    }
    
    public void drawBackground(Canvas canvas){
    	Rect src = new Rect(background_x, 0, background_x + 480, 800);
		Rect dst = new Rect(0, 0, 480, 800);
		canvas.drawBitmap(background_bmp, src, dst, paint);
		
		background_x++;
		if(background_x + 480 > background_bmp.getWidth()){
			background_x = 0;
		}
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
    