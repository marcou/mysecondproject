package com.gra.minigra;

import android.graphics.Canvas;
	/**
	 * @author Maciej
	 * watek generujacy petle kreatora
	 */

public class MiniGame1LoopThread extends Thread {
       static final long FPS = 15;
       private MiniGame1View view;
       private boolean running = false;
       
       public MiniGame1LoopThread(MiniGame1View view) {
           this.view = view;
       }

       public void setRunning(boolean run) {
           running = run;
       }
       @Override
       public void run() {
             long ticksPS = 1000 / FPS;
             long startTime;
             long sleepTime;
             while (running) {
                    Canvas c = null;
                    startTime = System.currentTimeMillis();
                    try {
                           c = view.getHolder().lockCanvas();
                           synchronized (view.getHolder()) {
                                  view.onDraw(c);
                           }
                    }
                    catch (Exception e) {
                    	e.printStackTrace();
                    }
                    finally {
                           if (c != null) {
                                  view.getHolder().unlockCanvasAndPost(c);
                           }
                    }
                    sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
                    try {
                           if (sleepTime > 0)
                                  sleep(sleepTime);
                           else
                                  sleep(10);
                    } catch (Exception e) {
                    	e.printStackTrace();
                    }
             }
       }
}
