package com.parker.david;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * an implementation of a mutator, first it selects an element of the population randomly.
 * Then it selects two cities within that solution randomly and swaps them.
 */
public class SwapTwoCities implements PopulationMutator {

	/**
	 * a random number generator
	 */
	private final Random randomNumberGenerator = ThreadLocalRandom.current();

	/**
	 * an internal record of the last population mutation performed as stored by a mutant record
	 */
	private ArrayList<MutantRecord> mutations = new ArrayList<>();

	/**
	 * this method takes a population and performs a mutation on a single solution in that population, picked at random
	 *
	 * @param unmutatedPopulation the population pre-mutation
	 * @return the solution population with a mutated solution in it
	 */
	@Override
	public SolutionPopulation mutatePopulation(SolutionPopulation unmutatedPopulation) {
		//make a copy of the population so that we can change the population without affecting prior populations,
		//but still keep links to the underlying solutions that aren't being changed for computational and memory efficiency
		SolutionPopulation mutatedPopulation = unmutatedPopulation.copy();

		//randomly pick a solution to mutate
		int solutionToMutate = randomNumberGenerator.nextInt(mutatedPopulation.getPopulationSize()) + 1;

		//mutate the solution
		CandidateSolution mutated = mutateSolution(mutatedPopulation.getSolutions().get(solutionToMutate));

		//swap out the original solution with the mutated one and return
		mutatedPopulation.getSolutions().set(solutionToMutate, mutated);
		return mutatedPopulation;
	}

	/**
	 * a method that returns an array list of mutations that occurred in the last population mutation.
	 * For this implementation it will always contain only 1 element
	 *
	 * @return an array list of mutant records containing a single mutant record
	 */
	@Override
	public ArrayList<MutantRecord> getMutations() {
		return mutations;
	}

	/**
	 * method that takes a single solution and returns a mutated version of it
	 * the original solution object is left intact
	 *
	 * @param unmutatedSolution the original solution before mutation
	 * @return the mutated candidate solution derived by swapping two cities in the original solution
	 */
	private CandidateSolution mutateSolution(CandidateSolution unmutatedSolution) {
		//reset mutations to empty
		mutations = new ArrayList<>();

		//create a copy of the solution that does not affect the original solution
		CandidateSolution mutatedSolution = unmutatedSolution.copy();

		//randomly select two cities
		int city1 = randomNumberGenerator.nextInt(mutatedSolution.cityCount()) + 1;
		int city2;
		do {
			city2 = randomNumberGenerator.nextInt(mutatedSolution.cityCount()) + 1;
		} while (city1 == city2);

		//swap those cities
		Collections.swap(mutatedSolution.getCities(), city1, city2);

		//record the mutation and return the result
		mutations.add(new MutantRecord(unmutatedSolution, mutatedSolution));
		return mutatedSolution;
	}
}
