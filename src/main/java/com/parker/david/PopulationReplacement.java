package com.parker.david;

/**
 * an interface that allows for multiple population replacement strategies
 */
public interface PopulationReplacement {

	/**
	 * takes in the parents and offspring(mutated) and finds some combination of them as the new solution population
	 *
	 * @param parents   the parent population
	 * @param offspring the mutated offspring population
	 * @return a blend of parents and offspring, from which an incumbent may be found, and may be used as the next generation's parents
	 */
	SolutionPopulation replace(SolutionPopulation parents, SolutionPopulation offspring);
}
