package com.parker.david;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * the candidate solution, contains an array of cities as its core data structure.
 * This is a vectorized permutation encoding as each city as a unique ID
 * fitness of a solution is seen as the sum of the edges of a route
 * as this object only contains references to the underlying cities, it should have a low memory footprint
 */
public class CandidateSolution implements Comparable<CandidateSolution> {

	/**
	 * the core data structure, stores the references to cities in an order. the order of these cities is what defines a solution
	 */
	private final ArrayList<City> cities;

	/**
	 * an internal fitness value calculated at creation of the solution.
	 * fitness is calculated at initialisation because it is accessed multiple times, and eager computation should improve performance
	 * fitness is the sum of distances of edges
	 */
	private final int fitness;

	/**
	 * an accessor for the internal city data structure
	 *
	 * @return an array list of unique cities
	 */
	public ArrayList<City> getCities() {
		return cities;
	}

	/**
	 * accessor for the fitness property
	 *
	 * @return an int corresponding to this object's fitness
	 */
	public int getFitness() {
		return fitness;
	}

	/**
	 * the constructor. takes in cities, and calculates fitness upon creation
	 *
	 * @param cities an array list of cities in an order which corresponds to the other the salesman in the TSP would follow
	 */
	CandidateSolution(ArrayList<City> cities) {
		this.cities = cities;
		this.fitness = calculateSolutionFitness(); //eagerly calculate fitness upon creation of this solution
	}

	/**
	 * an accessor to the number of cities in the TSP route
	 *
	 * @return the int number of cities in the solution
	 */
	public int cityCount() {
		return cities.size();
	}

	/**
	 * a semi-deep copy method. the new candidate solution has a brand new array list that is not linked at all to the initial one
	 * cities in the array list are shared. There will only ever be n cities ever created in the JVM where n is the number of cities in the TSP route
	 *
	 * @return a brand new solution that has the same order of cities as the current one
	 */
	CandidateSolution copy() {
		ArrayList<City> solutionCities = new ArrayList<>();
		for (City city : cities) {
			solutionCities.add(city);
		}
		return new CandidateSolution(solutionCities);
	}

	/**
	 * the fitness calculation. iterates through the cities stored and checks the distance to the next city.
	 * loops back to the start for the last city's distance, summing the distances as it goes.
	 *
	 * @return an int fitness(total distance) of this solution
	 */
	private int calculateSolutionFitness() {
		int cityCount = cities.size();
		int totalDistance = 0;

		//loop through all cities and add up the distances between each one and the next in the cycle
		for (int i = 0; i < cityCount; i++) {
			City departureCity = cities.get(i);
			City destinationCity = cities.get((i + 1) % cityCount);//reference the next city (or first city for the last city)

			totalDistance += departureCity.getDistanceToCity(destinationCity);
		}
		return totalDistance;
	}

	/**
	 * an implementation of the Comparable interface. This allows java's collections library to make comparisons between
	 * Candidate solution objects directly.
	 * for a.compareTo(b): (a > b) --> 1;  (a == b) --> 0; (a < b) --> -1
	 *
	 * @param comparisonSolution the solution we are comparing this solution's fitness to
	 * @return -1,0,1 for if this solution is less than, equal, or greater than the comparison solution
	 */
	@Override
	public int compareTo(CandidateSolution comparisonSolution) {
		return Integer.compare(this.getFitness(), comparisonSolution.getFitness());
	}

	/**
	 * represent this candidate solution string as a permutation of city ids
	 *
	 * @return a string representing this solution with ordered cities formatted as [0-3-1-2]
	 */
	@Override
	public String toString() {
		return "[" + cities.stream().map(City::toString).collect(Collectors.joining("-")) + "]";
	}
}