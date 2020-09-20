package com.parker.david;

import java.util.ArrayList;

/**
 * the lowest level object, representing a single city. Has an ID, and can find distances to the other cities
 */
public class City {

	/**
	 * this is a vector indicating distance to other cities.
	 * eg for [3,0,4] this city is 3 from city 0; 0 from city 1; and 4 from city 2
	 */
	private final ArrayList<Integer> distancesToOtherCities;

	/**
	 * this city's ID, this also this city's index in other city's distance vectors
	 */
	private final int cityId;

	/**
	 * an accessor for the city ID
	 *
	 * @return int this city's ID
	 */
	public int getCityId() {
		return cityId;
	}

	/**
	 * constructor, requires this city's ID as well as a distance vector to all other city IDs
	 */
	City(int cityId, ArrayList<Integer> distanceVector) {
		this.cityId = cityId;
		this.distancesToOtherCities = distanceVector;
	}

	/**
	 * this gets the distance from this city to another city, enabling abstraction in higher level objects
	 *
	 * @param otherCity the city who's distance we seek from this city
	 * @return int corresponding to distance to the city in question
	 */
	public int getDistanceToCity(City otherCity) {
		return distancesToOtherCities.get(otherCity.getCityId());
	}

	/**
	 * represent this city as a string
	 *
	 * @return the cityid as a string
	 */
	@Override
	public String toString() {
		return Integer.toString(cityId);
	}
}
