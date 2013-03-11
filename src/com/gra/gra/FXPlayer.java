package com.gra.gra;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class FXPlayer {
	 public enum SoundState{PLAYING,PAUSED};
	 //SoundPool do odtwarzania dzwiekow
	 private SoundPool mSoundPool;
	 //Hashmapa z efektami dzwiekowymi
	 private HashMap<Integer, Integer> mSoundPoolMap;
	 private AudioManager mAudioManager;
	 private Context mContext;
	 private int channels = 1;
	 //czy fx dzialaja
	 private boolean enabled = true;
	 
	 public FXPlayer(Context theContext){
		 initSounds(theContext);
	 }
	 
	 public FXPlayer(int channels, Context theContext){
		 this.channels = channels;
		 initSounds(theContext);
	 }
	 
	 private void initSounds(Context theContext) {
		 mContext = theContext;
		 // The first number here is the number of audio channels
		 mSoundPool = new SoundPool(channels, AudioManager.STREAM_MUSIC, 0);
		 mSoundPoolMap = new HashMap<Integer, Integer>();
		 mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	 }
	 
	 public void addSound(int index, int SoundID){
		 mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	 }
	 
	 public void playSound(int index){
		 if(!enabled) return;
		 float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		 streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		 mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	 }
	 
	 public void playSound(int index, float volume){
		 if(!enabled) return;
		 if(volume <= 0.0f) return;
		 float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		 volume = (streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) * volume;
		 mSoundPool.play(mSoundPoolMap.get(index), volume, volume, 0, 0, 1f);
	 }
	 
	 // Returns the stream id of the sound.
	 public int playLoopedSound(int index){
		 float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		 streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		 return mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 0, 0, 1f);
	 }
	 
	 // Pause a stream specified by a streamID returned by play.
	 public void pause(int streamID){
		 mSoundPool.pause(streamID);
	 }
	 
	 // Resume a stream specified by a streamID returned by play.
	 public void resume(int streamID){
		 mSoundPool.resume(streamID);
	 }
	 
	 public void release(){
		 if (mSoundPool != null){
			 mSoundPool.release();
			 mSoundPool = null;
		 }
	 }

	public void disable() {
		this.enabled = false;
	}
	
	public void enable(){
		this.enabled = true;
	}
}