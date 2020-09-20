package com.parker.david;

import java.util.ArrayList;

/**
 * an interface allowing multiple methods of initialisation
 */
public interface PopulationInitialiser {
	/**
	 * this generates an initial solution population from which to start optimisation
	 *
	 * @param cities         the set of cities which are part of each solution
	 * @param populationSize the number of solutions to be included in a population
	 */
	SolutionPopulation initialise(ArrayList<City> cities, int populationSize);
}
