package com.gra.zapisy;

import java.io.Serializable;
import java.util.ArrayList;

import com.gra.gra.FlyingObject;

/**
 * @author Maciej
 * Zawiera dane potrzebne do odtworzenia stanu gry
 */
public class SaveContainer implements Serializable {

	
	ArrayList<FlyingObject> objects = new ArrayList<FlyingObject>();
	
	private static final long serialVersionUID = 1L;
	
	
	//musi zawierac pozycje playera, aktualne pointy i pozycje razem z wektoroma predkosci wszystkich kuleczek

}
