package com.gra.zapisy;

import java.io.Serializable;
import java.util.ArrayList;

import com.gra.gra.FlyingObject;
import com.gra.gra.Player;

/**
 * @author Maciej
 * Zawiera dane potrzebne do odtworzenia stanu gry
 */
public class SaveContainer implements Serializable {

	
	ArrayList<FlyingObject> objects = new ArrayList<FlyingObject>();
	
	Player player;
	
	private static final long serialVersionUID = 1L;
	
	
	//musi zawierac pozycje playera, aktualne pointy i pozycje razem z wektoroma predkosci wszystkich kuleczek



public SaveContainer(Player player, ArrayList<FlyingObject> objs) {
	objects=objs;
	this.player=player;
}


/**
 * @return the objects
 */
public ArrayList<FlyingObject> getObjects() {
	return objects;
}


/**
 * @param objects the objects to set
 */
public void setObjects(ArrayList<FlyingObject> objects) {
	this.objects = objects;
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






}