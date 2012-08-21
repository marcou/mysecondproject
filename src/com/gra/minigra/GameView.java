package com.gra.minigra;

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
	
	private Paint paint;
	
	private static float world_gravity = 9.8f;	//grawitacja danego swiata
	
	private long coolDown = 100;	//co ile mozna kliknac w ekran
	private long lastClick;	//czas ostatniego klikniecia
	
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
    	paint = new Paint();
    	paint.setColor(Color.BLACK);	//x		y		mass	radius	gravity
    	earth = new Earth	(this, 		240, 	400, 	2000, 	100, 	9.8);
    									//x		y		mass	radius	angle
    	player = new Player	(this, 		240, 	340, 	1, 		10, 	270);
    	
    	player.set_earth(earth.getX(), earth.getY(), earth.getRadius());   
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	canvas.drawRect(0, 0, 480, 800, this.paint);
    	
    	paint.setColor(Color.GREEN);
    	canvas.drawText("X : " + player.getX(), 240, 10, paint);
    	canvas.drawText("Y : " + player.getY(), 240, 20, paint);
    	canvas.drawText("Angle : " + player.getAngle(), 240, 30, paint);
    	canvas.drawText("On_Ground : " + player.isOn_ground(), 240, 40, paint);
    	
//    	canvas.drawText("UP : " + up, 140, 10, paint);
//    	canvas.drawText("DOWN : " + down, 140, 20, paint);

    	paint.setColor(Color.BLACK);
    	
    	earth.onDraw(canvas);
    	player.onDraw(canvas);
    	
    	resolveGravity();
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
    	
    	float x = event.getX();
        float y = event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
        	if(y > 400){
        		if(player.isOn_ground()){
        			player.jump();
        		}
        	}
        	else{
	        	if(x < 240){
		        	player.move(false);
	        	}
	        	if(x > 240){
		        	player.move(true);
	        	}
        	}
    	 }
    	 if(event.getAction()==MotionEvent.ACTION_UP){
    		 //
    	 }
    		return super.onTouchEvent(event);		
    	}
    	
    	/*
    	if(System.currentTimeMillis() - lastClick > coolDown) {
    		lastClick = System.currentTimeMillis();
	    	float x = event.getX();
	        float y = event.getY();
	        Random rand = new Random();
	        
	        //Log.d("onTouchEvent", "EVENT : " + event.getAction());
	        if(event.getAction() == MotionEvent.ACTION_DOWN){
	             //record the start time
	        	if(x < 240){
		        	player.move(false);
		        }
	        	if(x > 240){
		        	player.move(true);
	        	}
	             startTime = event.getEventTime();

	             Log.d("LC", "IN DOWN");
	          }else if(event.getAction() == MotionEvent.ACTION_UP){
	             //record the end time
	             endTime = event.getEventTime();
	             Log.d("LC", "IN UP");
	          }else if(event.getAction() == MotionEvent.ACTION_MOVE){
	              Log.d("LC", "IN move");
	              if(x < 240){
			        	player.move(false);
	              }
		        	if(x > 240){
			        	player.move(true);
		        	}
	              endTime=0;
	          }
	          //verify
	          if(endTime - startTime >= 10){
	              Log.d("LC", "time touched greater than 10ms");
		              if(x < 240){
				        	player.move(false);
		              }
			        	if(x > 240){
				        	player.move(true);
			        	}
	        	startTime=0; 
	        	endTime=0;
	        	return false; //notify that you handled this event (do not propagate)
	          }
    		}
        return true;//propogate to enable drag

    }
    */
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
    