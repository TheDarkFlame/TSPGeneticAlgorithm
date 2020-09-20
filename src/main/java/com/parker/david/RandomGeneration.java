package com.parker.david;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * an implementation of a population initialiser, generation is completely random.
 * technically allows for duplicates in a population.
 */
public class RandomGeneration implements PopulationInitialiser {

	/**
	 * a random number generator
	 */
	private final Random randomNumberGenerator = ThreadLocalRandom.current();


	/**
	 * generates an initial set of solutions, takes a set of cities and generates a single random solution,
	 * adding it to a list until there are sufficient solutions in the population
	 *
	 * @param cities         the set of already create cities which are to be ordered in creating a solution
	 * @param populationSize the number of solutions to create to form the population. Be aware that this should be even for some generation strategies
	 * @return a population of solutions
	 */
	@Override
	public SolutionPopulation initialise(ArrayList<City> cities, int populationSize) {
		//create our array list of solutions
		ArrayList<CandidateSolution> solutions = new ArrayList<>();

		//generate populationSize number of solutions and add them to the array list
		for (int i = 0; i < populationSize; i++) {
			solutions.add(generateSolution(cities));
		}

		//return a population containing the array list of solutions
		return new SolutionPopulation(solutions);
	}

	/**
	 * the method used to generate a single solution randomly. This is done by creating a pool of cities,
	 * and then drawing a random city from that pool every time and adding it to the solution as its next city to visit
	 *
	 * @param cities a set of pre-defined cities that should be ordered randomly to generate a solution
	 * @return a single candidate solution
	 */
	private CandidateSolution generateSolution(ArrayList<City> cities) {

		//create a working set of cities
		ArrayList<City> cityPool = (ArrayList<City>) cities.clone();

		//create an empty list of cities
		ArrayList<City> randomlyOrderedCities = new ArrayList<>();

		//while our working set of cities is not empty, randomly grab a city from it and put it into the randomly ordered list
		while (!cityPool.isEmpty()) {
			randomlyOrderedCities.add(cityPool.remove(randomNumberGenerator.nextInt(cityPool.size())));
		}

		//create a solution from the randomly ordered list and return it
		return new CandidateSolution(randomlyOrderedCities);
	}
}
