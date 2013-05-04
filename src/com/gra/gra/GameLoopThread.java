package com.gra.gra;

import android.graphics.Canvas;
import android.util.Log;
/**
 * @author Maciej
 * watek generujacy petle kreatora
 */

public class GameLoopThread extends Thread {
	static final long FPS = 25;
	private GameView view;
	private boolean running = false;

	public GameLoopThread(GameView view) {
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

		//fps checker
		long contms=0;
		long lasttimecheck = System.currentTimeMillis();
		int fps=0;
		while (running) {
			long time =  System.currentTimeMillis();
			if(contms>1000) {
				Log.v("FPS",String.valueOf(fps));
				contms=time-lasttimecheck;
				fps=1;
			}
			else {
				fps++;
				contms+=time-lasttimecheck;
			}
			lasttimecheck = time;
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
