package com.parker.david;

/**
 * an interface allowing multiple methods of parent selection in crossover
 */
public interface ParentSelector {
	/**
	 * method to get a parent from a population.
	 * implementations must not modify the underlying data of the input population
	 *
	 * @param population the parent population we want to select a parent from
	 * @return a parent candidate solution from the parent population
	 */
	CandidateSolution getParent(SolutionPopulation population);
}
