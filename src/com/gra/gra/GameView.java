package com.gra.gra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aasmieci.Ball;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gra.R;
import com.gra.R.raw;
import com.gra.zapisy.AchievementsHolder;
import com.gra.zapisy.UserSettings;
import com.gra.zapisy.achievementType;
/**
 * 
 * @author Szpada
 * 
 * gra w kulki, jeszcze nie wiadomo co to bedzie robilo :/
 *
 */

public class GameView extends SurfaceView{
	/***********************************/
	 private boolean DEBUG_MODE = false;
	/***********************************/
	//punkty startowe gracza
	private int startPoints;
	//gracz skonczyl gre
	private boolean playerFinishedGame = false;
	
	private float w_factor;
	private float h_factor;
	
	private Generator generator;		//generator obiektow latajacych
	
	private int default_world_timer = 80;
	private int world_timer = default_world_timer;	//timer swiata, po tym czasie (logicznym) odpalany jest generator
	
	private int default_upgrade_timer = 200;
	private int upgrade_timer = default_upgrade_timer;	//timer po kotrym generowane sa upgrady
	
	//Pole gry (wieksze od ekranu) na ktorym generuje sie obiekty tak zeby gracz ich nie widzial (nie moga sie przeca nagle pojawiac)
	private int area_x = - 200;
	private int area_y = - 200;
	private int area_w = 680;
	private int area_h = 1000;
	
	private GameLoopThread thread;
	
	private List<Ball> balls = new ArrayList<Ball>();	//lista kulek
	private Earth earth;	//ziemia
	private Player player;	//gracz
	private Player temp_player = null;
	private ArrayList<FlyingObject> flyingObjects = new ArrayList<FlyingObject>();//lista obiektow latajacych
	
	private List<TempSprite> temps = new ArrayList<TempSprite>();//lista obiektow ktore znikaja po krotkim czasie (dym)
	
	private Paint paint;
	
	private boolean playermoving = false;
	@SuppressWarnings("unused")
	private boolean playerjumping = false;
	private boolean clockwisedirection;
	
	//kolec wystajacy z ziemi
	private boolean thorn = false;
	//achievementy gracza
	private AchievementsHolder achievements;
	
	//ustawienia gracza
	private UserSettings settings;
	
	//latajacy statek kosmiczny
	private SpaceShip ship = null;
	private boolean spaceShipReady = true;
	
	//HashMapa zaweirajaca bitmapy dla danych ID
	private HashMap<Integer, Bitmap> bitmaps;
	//Tablica zawierajaca liczbe kolumn i wierszy (dla animacji) ID rowne temu z hashmapy
	private int[][] bitmapProperties;
	//mediaplayer do odgrywania glownej melodyjki
	private MediaPlayer mediaPlayer;
	//FXPlayer do odtwarzania efektow dzwiekowych (wybuchy i inne)
	private FXPlayer fx;
	
	/*
	 * ZESTAW BITMAP DO RYSOWANIA WRAZ Z DANYMI (LICZBA KOLUMN I RZEDOW)
	 */
	private Bitmap spaceShip_bmp;
	private Bitmap player_bmp;
	private Bitmap heart_bmp;
	private Bitmap lifebar_bmp;
	private Bitmap asteroid_bmp;
	private Bitmap money_bmp;
	private Bitmap upgrade_bmp;
	private Bitmap earth_bmp;
	private Bitmap thorn_bmp;
	private Bitmap smoke_bmp;
	private Bitmap explosion_bmp;
	private Bitmap background_bmp;
	private int background_x = 0;
	private Bitmap mosteroid_bmp;	//money asteroid
	
	//Przy upgrejdach rysowany jest tylko najnowszy upgrade i jest on rysowany prosto z gameView (nie tworzy sie obiektu)
	private int info_frames;
	private int info_current_frame = 0;
	private int default_info_life = 30;
	private int info_life = default_info_life;
	private boolean info = false;
	
	private Bitmap info_bmp;
	private Bitmap info_speed;
	private Bitmap info_x2;
	private Bitmap info_x3;
	private Bitmap info_x4;
	private Bitmap info_tiny;
	private Bitmap info_huge;
	private Bitmap info_armagedon;
	private Bitmap info_money_rain;
	private Bitmap info_high_gravity;
	private Bitmap info_low_gravity;
	
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
    
    public GameView(Context context, double w_factor, double h_factor, Player player) {
        super(context);
        
        this.h_factor = (float)h_factor;
	   	this.w_factor = (float)w_factor;
	   	
	   	temp_player = player;
	   	
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
            	   createGenerator();
            	   createSprites();
            	   thread.setRunning(true);
            	   thread.start();
	            }
	            //@Override
	            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
	            	
	            }
        }); 
    }
    
    public void updatePlayer(Player player){
    	this.player = player;
    }
    
	public void createGenerator(){
    	this.generator = new Generator();
    	generator.setBounds(area_x, area_y, area_w, area_h);
    }
	
    public void createSprites(){
    	/*
    	 * inicjowanie bitmap
    	 */
    	asteroid_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.asteroids);
    	money_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.gems);
    	upgrade_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.upg);
    	earth_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.earthcrap);
    	thorn_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.kolce);
    	background_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.gownotlo);
    	smoke_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.dym);
    	info_speed = BitmapFactory.decodeResource(this.getResources(), R.drawable.upspeed);
    	info_x2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.upx2);
    	info_x3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.upx2);
    	info_x4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.upx2);
    	info_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.upspeed);
    	info_tiny = BitmapFactory.decodeResource(this.getResources(), R.drawable.uptiny);
    	info_huge = BitmapFactory.decodeResource(this.getResources(), R.drawable.uptiny);
    	info_armagedon = BitmapFactory.decodeResource(this.getResources(), R.drawable.uparmagedon);
    	info_high_gravity = BitmapFactory.decodeResource(this.getResources(), R.drawable.uphighgravity);
    	info_low_gravity = BitmapFactory.decodeResource(this.getResources(), R.drawable.uphighgravity);
    	info_money_rain = BitmapFactory.decodeResource(this.getResources(), R.drawable.uparmagedon);
    	explosion_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion);
    	heart_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.heart);
    	lifebar_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.lifebar);
    	player_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.jez);
    	mosteroid_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.money_asteroids);
    	spaceShip_bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.spaceship);
    	
    	bitmaps = new HashMap<Integer, Bitmap>();
    	
    	bitmaps.put(0, asteroid_bmp);
    	bitmaps.put(1, money_bmp);
    	bitmaps.put(2,upgrade_bmp);
    	bitmaps.put(3,earth_bmp);
    	bitmaps.put(4,thorn_bmp);
    	bitmaps.put(5,background_bmp);
    	bitmaps.put(6,smoke_bmp);
    	bitmaps.put(7,info_speed);
    	bitmaps.put(8,info_x2);
    	bitmaps.put(9,info_x3);
    	bitmaps.put(10,info_x4);
    	bitmaps.put(11,info_bmp);
    	bitmaps.put(12,info_tiny);
    	bitmaps.put(13,info_huge);
    	bitmaps.put(14,info_armagedon);
    	bitmaps.put(15,info_high_gravity);
    	bitmaps.put(16,info_low_gravity);
    	bitmaps.put(17,info_money_rain);
    	bitmaps.put(18,explosion_bmp);
    	bitmaps.put(19,heart_bmp);
    	bitmaps.put(20,lifebar_bmp);
    	bitmaps.put(21,player_bmp);
    	bitmaps.put(22,mosteroid_bmp);
    	bitmaps.put(23,spaceShip_bmp);

    	bitmapProperties = new int[][]{	{3,4},	//asteroid
    									{1,6},	//money
    									{1,1},	//upgrade
    									{1,1},	//earth
    									{1,1},	//thorn
    									{1,1},	//background
    									{4,4},	//smoke
    									{1,1},	//INFO.speed
    									{4,4},	//INFO.x2
    									{4,4},	//INFO.x3
    									{4,4},	//INFO.x4
    									{4,4},	//INFO
    									{1,1},	//INFO.tiny
    									{4,4},	//INFO.huge
    									{4,4},	//INFO.armagedon
    									{4,4},	//INFO.highGravity
    									{4,4},	//INFO.lowGravity
    									{4,4},	//INFO.moneyRain
    									{4,4},	//explosion
    									{1,1},	//heart
    									{1,1},	//lifebar
    									{1,1},	//player
    									{3,4},	//moneyAsteroid
    									{1,1},	//spaceship
    									};
    	
    	Log.d("START PROGRAMU", "=============================================");
    	Log.d("==============", "=============================================");
    	Log.d("START PROGRAMU", "=============================================");
    	paint = new Paint();
    	paint.setColor(Color.BLACK);	
    						//x		y		mass	radius	gravity
    	earth = new Earth	(240, 	400, 	2000, 	75, 	2.8);
    									//x		y		mass	radius	angle
    	player = new Player	(240, 	290, 	1, 		10, 	270);
    	 
    	createGenerator();
    	
    	updateGameSettings();
    	
    	player.set_earth(earth.getX(), earth.getY(), earth.getRadius());
    	player.setY((float)(earth.getY() - earth.getRadius() - player.getRadius()));
    	
    	for(int i = 0; i < flyingObjects.size(); i++){
    		flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    		//klatki animacji dla obiektow latajacych
    		flyingObjects.get(i).setFrames(bitmapProperties[flyingObjects.get(i).getID()][0] * bitmapProperties[flyingObjects.get(i).getID()][1] - 1);
    	}
    	//ustaw liczbe klatek animacji gracza
    	player.setFrames(bitmapProperties[player.getID()][0] * bitmapProperties[player.getID()][1] - 1);
    	
    	if(temp_player != null){
    		this.player = temp_player;
    	}
    	
    	//klatki informacji
    	info_frames = bitmapProperties[11][0] * bitmapProperties[11][1] - 1; 
    	
    	achievements = new AchievementsHolder();
    	
    	prepareSounds();
    }

    public void prepareSounds() {
    	mediaPlayer = MediaPlayer.create(getContext(), R.raw.maintheme);
    	mediaPlayer.setVolume(0.1f, 0.1f);
    	mediaPlayer.setLooping(true);
    	mediaPlayer.start();
    	
    	fx = new FXPlayer(10, getContext());
    	fx.addSound(0, R.raw.expl);
    	
	}
    @Override
    public void onDraw(Canvas canvas) {
    	//jesli player zdobyl ponad 1000 punktow to dodawany zostaje kolec (przeciwnik)
    	if(!thorn && player.getPoints() > 1000){
    		flyingObjects.add(new GroundEnemy(flyingObjects, -64, -64, 1, player.getAngle() + 180, 1, 10));
    		thorn = true;
    	}
    	//SKALOWANIE
    	canvas.scale(this.w_factor, this.h_factor);
    	
    	//odliczanie tajmera
    	world_timer--;
    	upgrade_timer--;
    	
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
    	drawBackground(canvas);
    	
    	//zolty prostokat reprezentuje obszar ekranu
    	paint.setColor(Color.YELLOW);
    	
    	paint.setStyle(Paint.Style.STROKE);
    	
    	canvas.drawRect(-1, -1, 480, 800, paint);
    	
    	//czerwony prostokat reprezentuje obszar gry
    	paint.setColor(Color.RED);
    	canvas.drawRect(area_x, area_y, area_w, area_h, paint);
    	
    	paint.setStyle(Paint.Style.FILL_AND_STROKE);
    	
    	canvas.restore();
    	
    	if(DEBUG_MODE){
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
    	}
    	if (DEBUG_MODE) 
		{
		canvas.scale(0.5f, 0.5f, 240, 400);
		}

    	paint.setColor(Color.BLACK);

    	//rysowanie statku kosmicznego
    	if(ship != null){
    		//drawSprite(canvas, (int)ship.getX(), (int)ship.getY(), 1, 1, ship.getWidth(), ship.getHeight(), 0, spaceShip_bmp, ship.getAngle(), ship.getScale());
    		drawSSprite(canvas, (int)ship.getX(), (int)ship.getY(), ship.getID(), ship.getCurrentFrame(), ship.getAngle(), ship.getScale(), 255);
    		ship.move();
    		if(ship.getX() > this.area_w || ship.getY() > this.area_h) ship = null;
    	}
    	//drawSprite(canvas, (int)earth.getX(), (int)earth.getY(), e_columns, e_rows, earth_bmp.getWidth()/e_columns, earth_bmp.getHeight()/e_rows, earth.getCurrentFrame(), earth_bmp, 0, earth.getRadius()/100.0f);
    	drawSSprite(canvas, (int)earth.getX(), (int)earth.getY(), earth.getID(), earth.getCurrentFrame(), 0, earth.getRadius()/100.0f, 255);
    	earth.update();
    	//drawSprite(canvas, (int)player.getX(), (int)player.getY(), p_columns, p_rows, player_bmp.getWidth()/p_columns, player_bmp.getHeight()/p_rows, player.getCurrentFrame(), player_bmp,0, (float)player.getRadius()/10.0f);
    	drawSSprite(canvas, (int)player.getX(), (int)player.getY(), player.getID(), player.getCurrentFrame(), 90, (float)player.getRadius()/10.0f, player.getAlpha());
    	player.update();
    	//player.onDraw(canvas);
    	
    	//rysowanie dymu
    	for(int i = temps.size()-1; i >=0; i--){
    		if(temps.get(i).getType() == tempType.smoke){
    			//drawSprite(canvas, (int)temps.get(i).getX(),  (int)temps.get(i).getY(), s_columns, s_rows, smoke_bmp.getWidth()/s_columns, smoke_bmp.getHeight()/s_rows, temps.get(i).getCurrentFrame(), smoke_bmp, 0, 1.0f);
    			drawSSprite(canvas, (int)temps.get(i).getX(),  (int)temps.get(i).getY(), temps.get(i).getID(), temps.get(i).getCurrentFrame(), 0, 1.0f, 255);
    		}
    	}

    	for(int i = flyingObjects.size()-1; i >= 0; i--){
    		//jesli obiekt sie zatrzymal (i jest poza ekranem) to go usun
    		if(flyingObjects.get(i).getSpeed() == 0.0 && !checkVissible(flyingObjects.get(i))){
    			flyingObjects.get(i).setLife(0);
    		}
    		//jesli obiekt wylecial poza obszar gry
    		if(!checkIfInArea(flyingObjects.get(i))){
    			flyingObjects.get(i).setLife(0);
    		}
    		//rysowanie obiektow (tylko tych widocznych - ekran)
    		if(DEBUG_MODE || checkVissible(flyingObjects.get(i))){
    			//rysowanie kulek
    			//flyingObjects.get(i).onDraw(canvas);
    			
    			/*==================
    			 * rysowanie spritow
    			 *==================*/
    			 
    			//rysowanie asteroid
    			if(flyingObjects.get(i) instanceof Asteroid){
    				if(flyingObjects.get(i) instanceof MoneyAsteroid){
        				//drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), 	moa_columns, moa_rows,  mosteroid_bmp.getWidth()/moa_columns, mosteroid_bmp.getHeight()/moa_rows,  flyingObjects.get(i).getCurrentFrame(), mosteroid_bmp, (float)flyingObjects.get(i).getAngle(), 0.5f + (float)((Asteroid)flyingObjects.get(i)).getSize()/6.0f);
        				drawSSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), ((MoneyAsteroid)flyingObjects.get(i)).getID(), flyingObjects.get(i).getCurrentFrame(), (float)flyingObjects.get(i).getAngle(), 0.5f + (float)((Asteroid)flyingObjects.get(i)).getSize()/6.0f, 255);
        			}
    				else{
    					//drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), 	a_columns, a_rows,  asteroid_bmp.getWidth()/a_columns, asteroid_bmp.getHeight()/a_rows,  flyingObjects.get(i).getCurrentFrame(), asteroid_bmp, (float)flyingObjects.get(i).getAngle(),  0.5f + (float)((Asteroid)flyingObjects.get(i)).getSize()/6.0f);
    					drawSSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), ((Asteroid)flyingObjects.get(i)).getID(), flyingObjects.get(i).getCurrentFrame(), (float)flyingObjects.get(i).getAngle(), 0.5f + (float)((Asteroid)flyingObjects.get(i)).getSize()/6.0f, 255);
    				}
    			}
    			//rysowanie hajsu
    			else if(flyingObjects.get(i) instanceof Money){
    				//drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(),  m_columns, m_rows,  money_bmp.getWidth()/m_columns, money_bmp.getHeight()/m_rows,  flyingObjects.get(i).getCurrentFrame(), money_bmp, (float)flyingObjects.get(i).getAngle(), false);
    				drawGems(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), ((Money)flyingObjects.get(i)).getPoints());
    			}
    			//rysowanie upgradeow
    			else if(flyingObjects.get(i) instanceof Upgrade){
    				//drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(),  u_columns, u_rows,  upgrade_bmp.getWidth()/u_columns, upgrade_bmp.getHeight()/u_rows,  flyingObjects.get(i).getCurrentFrame(), upgrade_bmp, (float)flyingObjects.get(i).getAngle(), 1.0f);
    				drawSSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), flyingObjects.get(i).getID(), flyingObjects.get(i).getCurrentFrame(), (float)flyingObjects.get(i).getAngle(), 1.0f, 255);
    			}
    			else if(flyingObjects.get(i) instanceof GroundEnemy){
    				//drawSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(),  t_columns, t_rows,  thorn_bmp.getWidth()/t_columns, thorn_bmp.getHeight()/t_rows,  flyingObjects.get(i).getCurrentFrame(), thorn_bmp, (float)flyingObjects.get(i).getAngle(), 1.0f);
    				drawSSprite(canvas, (int)flyingObjects.get(i).getX(), (int)flyingObjects.get(i).getY(), flyingObjects.get(i).getID(), flyingObjects.get(i).getCurrentFrame(), (float)flyingObjects.get(i).getAngle(), 0.5f, 255);
    			}
    			//update (klatki ++)
    			flyingObjects.get(i).update();
    		}
    		//przesuwanie obiektow
    		if(!flyingObjects.get(i).isOn_ground()){
    			float temp_x = flyingObjects.get(i).getX();
    			float temp_y = flyingObjects.get(i).getY();
    			
    			flyingObjects.get(i).resolveGravity(earth.getGravity(), earth.getMass(), earth.getRadius());
    			//narysuj warkocz za asteeroida jesli jest ona widoczna
    			if(flyingObjects.get(i) instanceof Asteroid && checkVissible(flyingObjects.get(i))){
    				//temps.add(new TempSprite(temps, this, flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getAngle(), asteroid_bmp.getWidth()/a_columns, smoke_bmp.getWidth()/s_columns));
    				temps.add(new TempSprite(temps, temp_x, temp_y, tempType.smoke));
    			}
    		}
    		//jesli obiekt dotyka ziemi
    		if(flyingObjects.get(i).isOn_ground() && flyingObjects.get(i) instanceof Asteroid){
    			//Jesli asteroida uderzyla pierwszy raz w ziemie dodaj wybuch
    			if(!((Asteroid) flyingObjects.get(i)).isExploded()){
    				temps.add(new TempSprite(temps, flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), tempType.explosion));
    				((Asteroid) flyingObjects.get(i)).setExploded(true);
    				fx.playSound(0, 0.2f);
    			}
    			if(flyingObjects.get(i).isOn_ground() && flyingObjects.get(i) instanceof MoneyAsteroid){
        			int size = flyingObjects.size();
        			flyingObjects.addAll(((MoneyAsteroid) flyingObjects.get(i)).getMoney(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getAngle()));
        			Log.d("MoneyAsteroid", "DODANO : " + (flyingObjects.size() - size) + " obiektow");
        		}
    		}
    		
    		//sprawdzenie kolizji miedzy obiektami latajacymi
    		for(int j = i - 1; j >= 0; j--){
    			if(flyingObjects.get(i).checkCollision(flyingObjects.get(j).getX(), flyingObjects.get(j).getY(), flyingObjects.get(j).getRadius())){
    				//jesli koliduja ze soba 2 asteroidy to dodaj wybuch
    				if(flyingObjects.get(i) instanceof Asteroid && flyingObjects.get(j) instanceof Asteroid){
    					//jesli obiekty sa widoczne
    					if(checkIfInArea(flyingObjects.get(i)) || checkIfInArea(flyingObjects.get(j))){
    						//dodaj wybuch
	    					temps.add(new TempSprite(temps,flyingObjects.get(j).getX() - (flyingObjects.get(j).getX() - flyingObjects.get(i).getX())/2, flyingObjects.get(j).getY() - (flyingObjects.get(j).getY() - flyingObjects.get(i).getY())/2, tempType.explosion));
	    					//oblicz odleglosc od ziemi i wedlug niej odwtorz dzwiek z glosnoscia
	    					//odwrotnie propocjonalna do niej
	    					float distance = (400.0f - (float)(Math.pow(Math.pow((earth.getX() - flyingObjects.get(i).getX()), 2) + Math.pow((earth.getY() - flyingObjects.get(i).getY()), 2),0.5)))/400.0f;
	    					Log.d("GameView", "DISTANCE : " + distance);
	    					fx.playSound(0, distance);
    					}
    				}
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
    				//sprawdzamy czy po przemieszczdeniu monety obiekt przypadkiem siê nie styka z naszym obiektem, jesli tak to go zasysamy
    				if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
    					player.resolveCollision(flyingObjects.get(i));
    				}
        		}
    		}
    		//zasysanie monety w locie
    		else if(flyingObjects.get(i) instanceof Money && !flyingObjects.get(i).isOn_ground()){
    			if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
    				if(settings.getScore() < player.getPoints()){
    					settings.setScore(player.getPoints());
    				}
					player.resolveCollision(flyingObjects.get(i));
				}
    		}
    		else{
    			//z kazdym innym obiektem kolizja przebiega normalnie
    			if(player.checkCollision(flyingObjects.get(i).getX(), flyingObjects.get(i).getY(), flyingObjects.get(i).getRadius(), false)){
    				//jesli obiekt to upgrade to wyswietlane jest info o tym upgradzie
    				if(flyingObjects.get(i) instanceof Upgrade){
    					showInfo(((Upgrade) flyingObjects.get(i)).getType());
    					drawUpgrade(canvas, info_speed);
    					//dodaj achievement (podniesiony upgrade)
    					showAchievementInfo(achievements.addUpgrade(((Upgrade) flyingObjects.get(i)).getType()));
    				}
    				//jesli obiekt to asteroida lub asterodia z kasa to sprawdz czy gracz dostal w locie (duck hunter achievement)
    				else if(flyingObjects.get(i) instanceof Asteroid || flyingObjects.get(i) instanceof MoneyAsteroid){
    					if(!player.isOn_ground()) showAchievementInfo(achievements.addDuck());
    				}
        			player.resolveCollision(flyingObjects.get(i));
        		}
    		}
    	}
    	//rysowanie wybuchow
    	for(int i = temps.size()-1; i >=0; i--){
    		if(temps.get(i).getType() == tempType.explosion){
    			//drawSprite(canvas, (int)temps.get(i).getX(),  (int)temps.get(i).getY(), ex_columns, ex_rows, explosion_bmp.getWidth()/ex_columns, explosion_bmp.getHeight()/ex_rows, temps.get(i).getCurrentFrame(), explosion_bmp, 0, 1.0f);
    			drawSSprite(canvas, (int)temps.get(i).getX(),  (int)temps.get(i).getY(), temps.get(i).getID(), temps.get(i).getCurrentFrame(), 0, 1.0f, 255);
    		}
    		temps.get(i).update();
    	}
    	//rysowanie info (jesli takie isntieje)
    	if(info){
    		drawUpgrade(canvas, info_bmp);
    	}
    	
    	drawLife(canvas);
    	
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
    	if(upgrade_timer <= 0){
    		resetUpgradeTimer();
    		flyingObjects.addAll(generator.generateUpgrades(player.getPoints(), player.isArmagedon(), player.isMoney_rain()));
    	}
    	if(world_timer <= 0){
    		resetTimer();
    		//dodaj graczowi punkty za generacje (przezycie kolejnych x sekund)
    		//punkte zalezne sa od wielkosci ziemi (im mniejsza tym wiecej)
    		player.setPoints(player.getPoints() + (6 - settings.getEarthStats()[settings.getEarth()][2]) * settings.getEarthStats()[settings.getEarth()][1]/2);
    		
    		flyingObjects.addAll(generator.generate(player.getPoints(), player.isArmagedon(), player.isMoney_rain()));
    		
    		//jesli gracz ma pelnego lajfa i ponad 10k punktow to ma szanse, ¿e pojawi sie statek kosmiczny
    		if(spaceShipReady && player.getPoints() >= 10 && player.getLife() == player.getMaxLife()){
    			ship = new SpaceShip(-100, -100, 128, 128);
    			ship.setBounds(area_x, area_y, area_w, area_h);
    			ship.calculateProperties();
    			spaceShipReady = false;
    		}
    		
    		//zaktualizowanie danych
    		for(int i = 0; i < flyingObjects.size(); i++){
    			flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
    			//klatki animacji dla obiektow latajacych
        		flyingObjects.get(i).setFrames(bitmapProperties[flyingObjects.get(i).getID()][0] * bitmapProperties[flyingObjects.get(i).getID()][1] - 1);
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
    			//klatki animacji dla obiektow latajacych
        		flyingObjects.get(i).setFrames(bitmapProperties[flyingObjects.get(i).getID()][0] * bitmapProperties[flyingObjects.get(i).getID()][1] - 1);
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
    	drawPoints(canvas);
    }
   
    public void drawLife(Canvas canvas){
    	//rysowanie paska zycia
    	/**PORTRET***********************/
    	//drawSSprite(canvas, 246, 720, 20, 0, 0, 1.0f, 255);
    	//for(int i = 0; i < player.getLife(); i++){
    		//drawSSprite(canvas, 214 + (i * 34), 720, 19, 0, 0, 1.0f, 255);
    	//}
    	/**LANDSCAPE**********************/
    	//drawSSprite(canvas, 460, 50, 20, 0, 90, 1.0f, 255);
    	//for(int i = 0; i < player.getLife(); i++){
		drawSSprite(canvas, 460, 720, 19, 0, 90, 1.0f, 255);
		canvas.save();
		canvas.rotate(90, 440, 750);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setTextSize(24.0f);
		canvas.drawText(" x " + player.getLife(), 445, 740, paint);
		canvas.restore();
    	//}
    	/***********************/
    }
    
	public void resetTimer(){
    	world_timer = default_world_timer;
    }
	
	public void resetUpgradeTimer(){
		upgrade_timer = default_upgrade_timer;
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

    public void achievementLog(){
    	Log.d("GAMEVIEW", "=========================ACHIEVEMENTS=======================");
    	Log.d("", "oneGameUpgrades : " + achievements.getOneGameUpgrades());
    	Log.d("", "upgrades10 : " + achievements.getUpgrades10());
    	Log.d("", "upgrades20 : " + achievements.getUpgrades20());
    	Log.d("", "upgrades30 : " + achievements.getUpgrades30());
    	Log.d("", "deaths : " + achievements.getDeaths());
    	Log.d("", "hearts : " + achievements.getHearts());
    	Log.d("", "aliens : " + achievements.getAliens());
    	Log.d("", "duck : " + achievements.getDuck());
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	int touchInd = event.getActionIndex();
    	float x = event.getX(touchInd)/ this.w_factor;
        float y = event.getY(touchInd) / this.h_factor;
    	
        boolean upT = false;
        boolean downT = false;
        boolean moveT = false;
    	//Log.d(VIEW_LOG_TAG, "Pointer Num:" + Integer.toString(event.getPointerId(0)));
    	if(event.getActionMasked()==MotionEvent.ACTION_UP || event.getActionMasked()==MotionEvent.ACTION_POINTER_UP ) {
        	upT=true;
        	downT=false;
        	moveT = false;
    	}
    	else if (event.getActionMasked()==MotionEvent.ACTION_DOWN || event.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN) {
    		downT=true;
    		upT=false;
    		moveT = false;
    	}
    	else if (event.getActionMasked()==MotionEvent.ACTION_MOVE) {
    		moveT=true;
    	}
    	else {
    		upT=false;
    		downT=false;
    		moveT=false;
    	}
    	

//        Log.d("TOUCH", Integer.toString(touchInd));
        
        //jesli player zyje
        if(player.getLife() > 0){
        	//jesli statek jest na ekranie to w pierwszej kolejnosci sprawdz kolizje z nim
        	if(ship != null){
        		//sprawdz kolizje ze statkiem tylko jesli nie jest on przysloniety przez ziemie (sprawdz kolizje z ziemia najpierw)
        		if(!earth.checkCollision(x,y)){
	        		if(ship.checkCollision(x, y)){
	        			showAchievementInfo(achievements.addAlien());
	        			temps.add(new TempSprite(temps,ship.getX() - (ship.getX() - ship.getX())/2, ship.getY() - (ship.getY() - ship.getY())/2, tempType.explosion));
	        			ship = null;
	        		}
        		}
        	}
        	
        	if (upT)  {
        		if (y<400) {
        			if ((x < 240 && !clockwisedirection) || (x >= 240 && clockwisedirection)) {
        				playermoving=false;
        			}
        				
        		}
        		else if (y>=400) {
        			playerjumping=false;	
        		}
        	}
        		
        	       	
        	else if (downT) {
        		if (y>=400 && player.isOn_ground()) {
	        		player.jump();
	    			playerjumping = true;
	    			achievementLog();
        		} // bag/hack z podwojnym skokiem powodujacym ruh
        		else if (x < 240) {
        			clockwisedirection = false;
        			playermoving = true;
        		}
        		else if (x >= 240) {
        			clockwisedirection = true;
        			playermoving = true;
        		}
        	}
        	
        	else if (moveT) {
        		if (y>=400 && player.isOn_ground() && playermoving) {
	        		player.jump();
	    			playerjumping = true;
	    			playermoving=false;
        		}
        		else if (y<400) {
        			if 		(x < 240 && (clockwisedirection || !playermoving)) {
        				clockwisedirection = false;
        				playermoving = true;
        			}
        			else if (x >= 240 && (!clockwisedirection || !playermoving)) {
        				clockwisedirection = true;
        				playermoving = true;
        			}
        		}
        	}
      
	        
	        else {
//	        	Log.d(VIEW_LOG_TAG,"Sth else Action " + Integer.toString(event.getActionMasked()));
	        	
	        	
	        	 
	        }
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
    public void drawSprite(Canvas canvas, int x, int y, int columns, int rows, int width, int height, int currentFrame, Bitmap bmp, float angle, float scale){
    	//rotacja
		canvas.save();
		canvas.rotate(angle, x, y);
		
		//skala
		canvas.scale(scale, scale, x, y);
		
    	int srcX = 0;
    	int srcY = 0;
    	int row;
    	srcX = (currentFrame % (columns)) * width;
    	if(rows % 2 == 0){
    		row = currentFrame / (rows);
    	}
    	else{
    		row = currentFrame / (rows + 1);
    	}
        srcY = row * height;
        
    	Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x - width/2, y - height/2, x + width/2, y + height/2);
		canvas.drawBitmap(bmp, src, dst, paint);
		
		canvas.restore();
    }
    
    public void drawSSprite(Canvas canvas, int x, int y, int ID, int currentFrame, float angle, float scale, int alpha){
    	//bledne ID
    	if(ID < 0) return;
    	
    	int columns = bitmapProperties[ID][1];
    	int rows = bitmapProperties[ID][0];
    	int width  = bitmaps.get(ID).getWidth()/bitmapProperties[ID][1];
    	int height = bitmaps.get(ID).getHeight()/bitmapProperties[ID][0];
    	//rotacja
		canvas.save();
		canvas.rotate(angle, x, y);
		
		//skala
		canvas.scale(scale, scale, x, y);
		
    	int srcX = 0;
    	int srcY = 0;
    	int row;
    	srcX = (currentFrame % (columns)) * width;
    	if(rows % 2 == 0){
    		row = currentFrame / (rows);
    	}
    	else{
    		row = currentFrame / (rows + 1);
    	}
        srcY = row * height;
        
    	Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect(x - width/2, y - height/2, x + width/2, y + height/2);
		
		paint.setAlpha(alpha);
		
		canvas.drawBitmap(bitmaps.get(ID), src, dst, paint);
		
		paint.reset();
		
		canvas.restore();
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
    
    public void drawUpgrade(Canvas canvas, Bitmap bmp){
    	/**PORTRET***********************/
    	//int x = 240;
    	//int y = 40;
    	/**LANDSCAPE**********************/
    	int x = 440;
    	int y = 400;
    	canvas.save();
    	canvas.rotate(90, x, y);
    	/***********************/
    	int srcX = 0;
    	int srcY = 0;
    	int row;
    	if(info_life > 0){
    		info_life--;
    	}
    	else{
    		srcX = (info_current_frame % (bitmapProperties[11][1])) * (info_bmp.getWidth()/bitmapProperties[11][1]);
    		if(bitmapProperties[11][0] % 2 == 0){
    			row = info_current_frame / (bitmapProperties[11][0]);
        	}
        	else{
        		row = info_current_frame / (bitmapProperties[11][0] + 1);
        	}
    		row = info_current_frame / (bitmapProperties[11][0]);
    		srcY = row * (info_bmp.getHeight()/bitmapProperties[11][0]);
    	}
    	Rect src = new Rect(srcX, srcY, srcX + info_bmp.getWidth()/bitmapProperties[11][1], srcY + info_bmp.getHeight()/bitmapProperties[11][0]);
		Rect dst = new Rect(x - info_bmp.getWidth()/(2 * bitmapProperties[11][1]), y - info_bmp.getHeight()/(2 * bitmapProperties[11][0]), x + info_bmp.getWidth()/(2 * bitmapProperties[11][1]), y + info_speed.getHeight()/(2 * bitmapProperties[11][0]));
		canvas.drawBitmap(bmp, src, dst, paint);
		if(info_life <= 0){
    		info_current_frame++;
    	}
		if(info_current_frame > info_frames){
			info_current_frame = 0;
			info_life = default_info_life;
			info = false;
		}
		canvas.restore();
    }
    
    public void showAchievementInfo(achievementType type){
    	Log.d("GameView", "Pokazuje achievement");
    	if(type == null){
    		Log.d("GameView", "Jednak nie");
    		return;
    	}
    	//wyzerowanie frame-a i life-a
    	info_current_frame = 0;
    	info_life = default_info_life;
    	switch(type){
    	case adept:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case apprentice:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case alien:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case casanova:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case collector:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case dead:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case duck:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case isdp:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case lover:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case master:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case novice:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case secret:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	}
    }
    
    public void showInfo(upgradeType type){
    	//wyzerowanie frame-a i life-a
    	info_current_frame = 0;
    	info_life = default_info_life;
    	switch(type){
    	case armagedon:
    		this.info = true;
    		this.info_bmp = info_armagedon;
    		break;
    	case high_gravity:
    		this.info = true;
    		this.info_bmp = info_low_gravity;
    		break;
    	case huge_player:
    		this.info = true;
    		this.info_bmp = info_huge;
    		break;
    	case immortality:
    		this.info = true;
    		this.info_bmp = info_speed;
    		break;
    	case low_gravity:
    		this.info = true;
    		this.info_bmp = info_high_gravity;
    		break;
    	case money_rain:
    		this.info = true;
    		this.info_bmp = info_money_rain;
    		break;
    	case speed:
    		this.info = true;
    		this.info_bmp = info_speed;
    		break;
    	case tiny_player:
    		this.info = true;
    		this.info_bmp = info_tiny;
    		break;
    	case ultra_suck:
    		this.info = true;
    		this.info_bmp = info_speed;
    		break;
    	case x2:
    		this.info = true;
    		this.info_bmp = info_x2;
    		break;
    	case x3:
    		this.info = true;
    		this.info_bmp = info_x3;
    		break;
    	case x4:
    		this.info = true;
    		this.info_bmp = info_x4;
    		break;
    	case life:
    		this.info = true;
    		this.info_bmp = info_huge;
    		break;
    	}
    }
    
    public void drawPoints(Canvas canvas){
    	paint.setTextSize(20);
    	paint.setAntiAlias(true);
    	paint.setColor(Color.GREEN);
    	/**PORTRET***********************/
    	//int yPosition = 50;
    	//int xPosition = 200;
    	/**LANDSCAPE**********************/
    	int yPosition = 50;
    	int xPosition = 460;
    	canvas.save();
    	canvas.rotate(90, xPosition, yPosition);
    	/***********************/
    	//jesli gracz zyje
    	if(player.getLife() > 0){
	    	//jesli gracz ma wiecej niz 10k skroc liczbe punktow
	    	if(player.getPoints() >= 10000){
	    		//canvas.drawText("POINTS : " + player.getPoints()/1000 + " K", earth.getX() - 64, earth.getY(), paint);
	    		canvas.drawText("POINTS : " + player.getPoints()/1000 + " K", xPosition, yPosition, paint);
	    	}
	    	//jesli gracz ma wiecej niz 10M skroc liczbe punktow
	    	else if(player.getPoints() >= 10000000){
	    		//canvas.drawText("POINTS : " + player.getPoints()/1000000 + " M", earth.getX() - 64, earth.getY(), paint);
	    		canvas.drawText("POINTS : " + player.getPoints()/1000000 + " M", xPosition, yPosition, paint);
	    	}
	    	else{
	    		//canvas.drawText("POINTS : " + player.getPoints(), earth.getX() - 64, earth.getY(), paint);
	    		canvas.drawText("POINTS : " + player.getPoints(), xPosition, yPosition, paint);
	    	}
    	}
    	else{
    		achievements.addDeath();
    		if(!playerFinishedGame){
    			settings.addGamesPlayed();
    			playerFinishedGame = true;
    		}
    		/**PORTRET***********************/
//    		canvas.drawText("UMARLES! " , earth.getX() - 32, earth.getY() - 32, paint); 
//    		canvas.drawText("ZDOBYLES : " + player.getPoints()+ " PUNKTOW", earth.getX() - 64, earth.getY(), paint);
        	/**LANDSCAPE**********************/
    		canvas.restore();
    		canvas.rotate(90, earth.getX() - 32, earth.getY() - 32);
    		canvas.drawText("UMARLES! " , earth.getX() - 32, earth.getY() - 32, paint); 
    		canvas.drawText("ZDOBYLES : " + player.getPoints()+ " PUNKTOW", earth.getX() - 64, earth.getY(), paint);
        	/***********************/
    		
    		playermoving = false;
    	}
    	canvas.restore();
    	paint.reset();
    }
    
    
    private void drawGems(Canvas canvas, int x, int y, int points) {
    	int srcX = 0;
    	int srcY = 0;
    	if(points < 5){
    		srcX = 0;
    	}
    	else if(points < 10){
    		srcX = 1;
    	}
    	else if(points < 20){
    		srcX = 2;
    	}
    	else if(points < 50){
    		srcX = 3;
    	}
    	else if(points < 100){
    		srcX = 4;
    	}
    	else{
    		srcX = 5;
    	}
    	Rect src = new Rect(srcX * bitmaps.get(1).getWidth()/bitmapProperties[1][1], srcY, srcX * bitmaps.get(1).getWidth()/bitmapProperties[1][1] + bitmaps.get(1).getWidth()/bitmapProperties[1][1], srcY + bitmaps.get(1).getHeight()/bitmapProperties[1][0]);
		Rect dst = new Rect(x - bitmaps.get(1).getWidth()/(bitmapProperties[1][1] * 2), y - bitmaps.get(1).getHeight()/(bitmapProperties[1][0] * 2), x + bitmaps.get(1).getWidth()/(bitmapProperties[1][1] * 2), y + bitmaps.get(1).getHeight()/(bitmapProperties[1][0] * 2));
		canvas.drawBitmap(bitmaps.get(1), src, dst, paint);
	}
    
    public void updateGameSettings(){
    	Log.d("GAMEVIEW", "WSZEDLEM DO UPDATESETTINGS");
    	if(settings != null){
    		//ZIEMIA
	    	earth.setRadius(20 + (settings.getEarthStats()[settings.getEarth()][2]) * 15);
	    	earth.setGravity(2.0 + (double)settings.getEarthStats()[settings.getEarth()][1] * 0.4);
	    	//podstawowe wartrosci grawitacji i promienia
	    	earth.setDefault_gravity(earth.getGravity());
	    	earth.setDefault_radius(earth.getRadius());
	    	//tekstura
	    	Log.d("GameView", "TEKSTURA ZIEMI : " + settings.getEarthStats()[settings.getEarth()][0]);
			earth_bmp = BitmapFactory.decodeResource(getResources(), settings.getEarthStats()[settings.getEarth()][0]);
			bitmaps.put(3, earth_bmp);
	    	
	    	//GRACZ
			//statystyki ziemi
			player.set_earth(earth.getX(), earth.getY(), earth.getRadius());
			player.setY((float)(earth.getY() - earth.getRadius() - player.getRadius()));
			//predkosc gracza
			player.setSpeed( 2.2 + (double)settings.getPlayerStats()[settings.getCharacter()][1] * 0.8 - (settings.getEarthStats()[settings.getEarth()][2])/2);
			player.setDefault_speed(player.getSpeed());
			//zycie gracza
			player.setLife(settings.getPlayerStats()[settings.getCharacter()][2]);
			player.setMaxLife(settings.getPlayerStats()[settings.getCharacter()][2]);
			//upgrady gracza
			player.setUpgradeLevel(settings.getPlayerStats()[settings.getCharacter()][3]);
			//punkty startowe gracza
			player.setPoints(startPoints);
			
			//GENERATOR
			generator.setPlayerUpgradeMultiplier(settings.getPlayerStats()[settings.getCharacter()][3]);
			//co ile odpaln bedzie generator upgradow
			upgrade_timer = default_upgrade_timer + (int)((((double)default_upgrade_timer)/4.0) * (5.0/(double)settings.getPlayerStats()[settings.getCharacter()][3]));
			default_upgrade_timer = upgrade_timer;
			
			//OBIEKTY LATAJACE
			for(int i = 0; i < flyingObjects.size(); i++){
				flyingObjects.get(i).set_earth(earth.getX(), earth.getY(), earth.getRadius());
				//klatki animacji dla obiektow latajacych
	    		flyingObjects.get(i).setFrames(bitmapProperties[flyingObjects.get(i).getID()][0] * bitmapProperties[flyingObjects.get(i).getID()][1] - 1);
			}
    	}
    }
    
    public void releaseSounds(){
    	mediaPlayer.release();
    	fx.release();
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
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	/**
	 * @return the flyingObjects
	 */
	public ArrayList<FlyingObject> getFlyingObjects() {
		return flyingObjects;
	}
	/**
	 * @param flyingObjects the flyingObjects to set
	 */
	public void setFlyingObjects(ArrayList<FlyingObject> flyingObjects) {
		this.flyingObjects = flyingObjects;
	}
	
	/**
	 * @param inputSettings
	 */
	public void setSettings(UserSettings inputSettings) {
		if (inputSettings!=null) {
			this.settings=inputSettings;
		}
		else {
			Log.d("GameView", "SETTINGSow nie ma");
			this.settings = new UserSettings();
		}
		this.achievements=settings.getAchievements();
	}

	/**
	 * @return the achievements
	 */
	public AchievementsHolder getAchievements() {
		return achievements;
	}

	/**
	 * @return the settings
	 */
	public UserSettings getSettings() {
		return settings;
	}

	/**
	 * @return the thorn
	 */
	public boolean isThorn() {
		return thorn;
	}

	/**
	 * @param thorn the thorn to set
	 */
	public void setThorn(boolean thorn) {
		this.thorn = thorn;
	}
	/**
	 * 
	 * @param punkty startowe
	 */
	public void setPoints(int points) {
		this.startPoints = points;
	}
}
    