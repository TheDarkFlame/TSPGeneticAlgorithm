package com.parker.david;

import java.util.ArrayList;

/**
 * an interface allowing for multiple implementations of a population crossover function
 */
public interface PopulationCrossover {
	/**
	 * the breed function, it takes in a population of parents
	 * and produces a new population of offspring by some crossover method
	 *
	 * @param parents                 the parent population
	 * @param offspringPopulationSize the size of the desired offspring population
	 * @return an offspring population generated from the parent population
	 */
	SolutionPopulation breed(SolutionPopulation parents, int offspringPopulationSize);

	/**
	 * a bookkeeping method, after the offspring are generated, calling this method yields an array of family record objects
	 * each family record object shows a set of parents and their offspring who are related
	 *
	 * @return an arraylist of family records
	 */
	ArrayList<FamilyRecord> getFamilies();
}
