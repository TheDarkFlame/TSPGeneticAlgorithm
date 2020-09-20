package com.parker.david;

import java.util.ArrayList;

/**
 * an interface that allows for multiple implementations of a mutator
 */
public interface PopulationMutator {

	/**
	 * a method that mutates the population
	 *
	 * @param population the population which we wish to mutate
	 * @return a population that has a mutation applied to it
	 */
	SolutionPopulation mutatePopulation(SolutionPopulation population);

	/**
	 * a bookkeeping method, after the mutations are applied, calling this method yields an array of mutation record objects
	 * each record shows how a solution was mutated
	 *
	 * @return an array list of mutation records
	 */
	ArrayList<MutantRecord> getMutations();
}
