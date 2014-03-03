package com.mrakconsultinggruwp.rackcity.client;

import java.util.ArrayList;

public class CrimeGeoList {

	/**
	 *  Should this be a singleton class? 
	 */
	
	ArrayList<CrimeGeoPoint> crimeList = new ArrayList<CrimeGeoPoint>();
	
	public CrimeGeoList(){
		
	}
	
	public ArrayList<CrimeGeoPoint> getCrimeList(){
		return this.crimeList;
	}
}
