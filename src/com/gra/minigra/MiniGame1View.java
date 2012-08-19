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
/**
 * 
 * @author Szpada
 * 
 * gra w kulki, jeszcze nie wiadomo co to bedzie robilo :/
 *
 */

public class MiniGame1View extends SurfaceView{
	private MiniGame1LoopThread thread;
	private List<Ball> balls = new ArrayList<Ball>();	//lista kulek
	private Paint paint;
	
	private static float world_gravity = 9.8f;	//grawitacja danego swiata
	
	private long coolDown = 300;	//co ile mozna kliknac w ekran
	private long lastClick;	//czas ostatniego klikniecia
	
    public MiniGame1View(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //this.setOnTouchListener(this);
    	thread = new MiniGame1LoopThread(this);
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
    	paint.setColor(Color.BLACK);
    	
    	//zderzenia 2D
    	Ball ball = new Ball(this, this.balls, 140, 100, 10, 70, 10, 5, 0.6f, 10);
    	Ball ball2 = new Ball(this, this.balls, 340, 700, 10, 250, 30, 5, 0.2f, 10);
    	ball.setGarvity(world_gravity);
    	ball2.setGarvity(world_gravity);
    	//this.balls.add(ball);
    	//this.balls.add(ball2);
    	//zderzenia 1D
    	Ball ball3 = new Ball(this, this.balls, 40, 400, 10, 0, 10, 5, 0.6f, 10);
    	Ball ball4 = new Ball(this, this.balls, 440, 400, -5, 0, 10, 30, 0.2f, 10);
    	ball.setGarvity(world_gravity);
    	ball2.setGarvity(world_gravity);
    	ball3.setColor(Color.BLUE);
    	this.balls.add(ball3);
    	this.balls.add(ball4);
    }
    
    @Override
    public void onDraw(Canvas canvas) {
    	canvas.drawRect(0, 0, 480, 800, this.paint);
    	for(int i = balls.size()-1; i >=0; i--){
    		balls.get(i).onDraw(canvas);
    	}
    	if(balls.size() > 1){
    		paint.setColor(Color.BLUE);
    		canvas.drawText("direction [1]    " + balls.get(0).getDirection(), 120, 20,paint );
    		canvas.drawText("velocity  [1]    " + balls.get(0).getVelocity(), 120, 40,paint );
    		canvas.drawText("mass  	   [1]    " + balls.get(0).getMass(), 120, 60,paint );
    		
    		paint.setColor(Color.RED);
    		canvas.drawText("direction [2]    " + balls.get(1).getDirection(), 320, 20,paint );
    		canvas.drawText("velocity  [2]    " + balls.get(1).getVelocity(), 320, 40,paint );
    		canvas.drawText("mass  	   [2]    " + balls.get(1).getMass(), 320, 60,paint );
    	}
    	paint.setColor(Color.BLACK);
    	if(balls.size() > 1){
    		checkCollision();
    	}
    }
    public void checkCollision(){
    	for(int i = 0; i < balls.size(); i++){
    		for(int j = i + 1; j < balls.size(); j++){
    			if(balls.get(i).checkBallCollision(balls.get(j).getX(), balls.get(j).getY(), balls.get(j).getRadius())){
    				resolveCollision(balls.get(i), balls.get(j));
//    				Ball temp_ball1 = balls.get(i);
//    				Ball temp_ball2 = balls.get(j);
    				//balls.get(i).copyBall(resolveCollision(balls.get(i), balls.get(j)));
    				//balls.get(j).copyBall(resolveCollision(balls.get(j), balls.get(i)));
//    				balls.get(i).copyBall(resolveCollision(temp_ball1, temp_ball2));
//    				balls.get(j).copyBall(resolveCollision(temp_ball2, temp_ball1));
    			}
    		}
    	}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(System.currentTimeMillis() - lastClick > coolDown) {
    		lastClick = System.currentTimeMillis();
	    	float x = event.getX();
	        float y = event.getY();
	        Random rand = new Random();
	        Ball ball = new Ball(this, this.balls, x, y, rand.nextInt(20), rand.nextInt(360), 15, 5, 0.5f, 10);
	    	ball.setGarvity(world_gravity);
	    	this.balls.add(ball);
    	}
    	return true;
    }
    public void resolveCollision(Ball ball1, Ball ball2){
    	Log.d("View", "resolving");
		//A = atan2(y1 - y2, x1 - x2)
//		v1x = u1 X cos(D1 - A)
//		v1y = u1 X sin(D1 - A)
//		v2x = u2 X cos(D2 - A)
//		v2y = u2 X sin(D2 - A)
//		f1x = v1x(m1 - m2) + 2m2v2x
//				/m1 + m2
//
//		f2x = v2x(m1 - m2) + 2m2v1x
//				/m1 + m2 
//		v1 = (f1x2 X f1x2 + v1y X v1y2)
//		v2 = (f2x2 X f2x2 + v2y X v2y2)
//
//		D1 = atan2(v1y, f1x) + A
//		D2 = atan2(v2y, f2x) + A
				
    	/*
    	 * kolizje 1D
    	 */
    	/*
    	 * v1 = u1(m1 - m2) + 2m2u2       
					/m1 + m2
    	 */
    	/*
    	float u1 = ball1.getVelocity();
    	float u2 = ball2.getVelocity();
    	float m1 = ball1.getMass();
    	float m2 = ball2.getMass();
    	
    	Log.d("KURWA", "u1 : " + u1);
    	Log.d("KURWA", "u2 : " + u2);
    	Log.d("KURWA", "m1 : " + m1);
    	Log.d("KURWA", "m2 : " + m2);
    	float co_jest_kurwa = (u1*(m1 - m2) + 2 * m2 * u2)/(m1 + m2);
    	float co_jest_kurwa2 = (u2*(m2 - m1) + 2 * m1 * u1)/(m1 + m2);
    	Log.d("KURWA", "velocity1 after collision : " + co_jest_kurwa);
    	Log.d("KURWA", "velocity2 after collision : " + co_jest_kurwa2);
    	ball1.setVelocity(co_jest_kurwa);
    	ball2.setVelocity(co_jest_kurwa2);
    	if(co_jest_kurwa < 0){
    		ball1.setDirection(180-ball1.getDirection());
    	}
    	if(co_jest_kurwa2 < 0){
    		ball2.setDirection(180-ball2.getDirection());
    	}
    	ball1.setX_speed((float)Math.cos(Math.toRadians(ball1.getDirection())) * ball1.getVelocity());
    	ball1.setY_speed((float)Math.sin(Math.toRadians(ball1.getDirection())) * ball1.getVelocity());
    	ball2.setX_speed((float)Math.cos(Math.toRadians(ball2.getDirection())) * ball2.getVelocity());
    	ball2.setY_speed((float)Math.sin(Math.toRadians(ball2.getDirection())) * ball2.getVelocity());
    	Log.d("Ball collision", "ball 1 velocity : " + ball1.getVelocity());
    	Log.d("Ball collision", "ball 2 velocity : " + ball2.getVelocity());
//    	ball1.setVelocity((ball1.getVelocity()*(ball1.getMass() - ball2.getMass()) + 2 * ball2.getMass() * ball2.getVelocity())/ball1.getMass() + ball2.getMass());
//    	ball1.setVelocity((ball1.getVelocity()*(ball1.getMass() - ball2.getMass()) + 2 * ball2.getMass() * ball2.getVelocity())/ball1.getMass() + ball2.getMass());
    	*/
    	/*
    	 * kolizje 2D
    	 */
    	/*
		float A = (float)Math.atan2(ball1.getY_position() - ball2.getY_position(), ball1.getX_position() - ball2.getX_position());
		
		ball1.setX_speed((float)(ball1.getVelocity() * Math.cos(Math.toRadians(ball1.getDirection() - A))));	//v1x
		ball1.setY_speed((float)(ball1.getVelocity() * Math.sin(Math.toRadians(ball1.getDirection() - A))));	//v1y
		ball2.setX_speed((float)(ball2.getVelocity() * Math.cos(Math.toRadians(ball2.getDirection() - A))));	//v2x
		ball2.setY_speed((float)(ball2.getVelocity() * Math.sin(Math.toRadians(ball2.getDirection() - A))));	//v2y
		
		float f1 = (ball1.getX_speed() * (ball1.getMass() - ball2.getMass()) + 2 * ball2.getMass() * ball2.getX_speed())/ball1.getMass() + ball2.getMass();
		float f2 = (ball2.getX_speed() * (ball1.getMass() - ball2.getMass()) + 2 * ball2.getMass() * ball1.getX_speed())/ball1.getMass() + ball2.getMass();
		
		ball1.setVelocity((float)(Math.pow(Math.pow(f1,4) + Math.pow(ball1.getX_speed(),4),0.5)));
		ball2.setVelocity((float)(Math.pow(Math.pow(f2,4) + Math.pow(ball1.getY_speed(),4),0.5)));
		
		ball1.setDirection((float)(Math.atan2(ball1.getY_speed(), f1) + A));
		ball2.setDirection((float)(Math.atan2(ball2.getY_speed(), f2) + A));
		
		ball1.setX_speed((float)Math.cos(Math.toRadians(ball1.getDirection())) * ball1.getVelocity());
    	ball1.setY_speed((float)Math.sin(Math.toRadians(ball1.getDirection())) * ball1.getVelocity());
    	ball2.setX_speed((float)Math.cos(Math.toRadians(ball2.getDirection())) * ball2.getVelocity());
    	ball2.setY_speed((float)Math.sin(Math.toRadians(ball2.getDirection())) * ball2.getVelocity());
    	*/
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
    	
//    	if(v1 < 0 && ball1.getVelocity() > 0 || v1 > 0 && ball1.getVelocity() < 0){
//    		ball1.setDirection(180-ball1.getDirection());
//    	}
//    	if(v2 < 0 && ball1.getVelocity() > 0 || v2 > 0 && ball2.getVelocity() < 0){
//    		ball2.setDirection(180-ball2.getDirection());
//    	}
    	//ball1.setVelocity(v1);
    	//ball2.setVelocity(v2);
    	
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
    	//tangens x/y 
    	
//    	ball1.setX_speed((float)Math.cos(Math.toRadians(ball1.getDirection())) * ball1.getVelocity());
//    	ball1.setY_speed((float)Math.sin(Math.toRadians(ball1.getDirection())) * ball1.getVelocity());
//    	ball2.setX_speed((float)Math.cos(Math.toRadians(ball2.getDirection())) * ball2.getVelocity());
//    	ball2.setY_speed((float)Math.sin(Math.toRadians(ball2.getDirection())) * ball2.getVelocity());
	}
}
    