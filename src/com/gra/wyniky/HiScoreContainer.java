package com.gra.wyniky;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Maciej
 * Przechowuje skory
 */
public class HiScoreContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String,Integer> scores; // <PlayerName, PlayerScore>

}
