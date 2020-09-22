package com.parker.david;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * a tournament selection method of selecting a parent for breeding.
 * selects a number of parents from the parent pool, and puts them into a tournament.
 * selection is based on a probability that is proportional to the fitness of the solution
 */
public class TournamentSelection implements ParentSelector {

	/**
	 * the size of a tournament if the population pool is unrestricted
	 */
	private final int tournamentSize;

	/**
	 * a random number generator
	 */
	private final Random randomNumberGenerator = ThreadLocalRandom.current();

	/**
	 * constructor
	 */
	TournamentSelection(int tournamentSize) {
		this.tournamentSize = tournamentSize;
	}

	/**
	 * given a parent pool, select min(parent.size(),tournamentSize) parents for a tournament
	 * and return the best solution
	 *
	 * @param population the input parent population from which we want to select a solution
	 * @return a single solution that won the tournament
	 */
	@Override
	public CandidateSolution getParent(SolutionPopulation population) {
		//create a working population from which we may remove solutions
		SolutionPopulation parentPool = population.copy();

		//
		ArrayList<CandidateSolution> tournament = new ArrayList<>();

		while (tournament.size() < tournamentSize && !parentPool.getSolutions().isEmpty()) {
			int removeIndex = weightedRandomIndex(parentPool.getSolutions(), parentPool.meanFitness());
			tournament.add(parentPool.getSolutions().remove(removeIndex));
		}
		return Collections.min(tournament);
	}

	/**
	 * a weighted random selection, first gets all candidates to have a chance in proportion to their index
	 * then selects one randomly using a roulette selection-esque method to decide on an element
	 * 1 / (population size) * (fitness / mean fitness)
	 * thus if all are equal total of all probabilities is 1 / popsize * ((fitness/meanfitness) * popsize) = 1
	 *
	 * @param solutions   the set of solutions
	 * @param meanFitness the mean fitness of all the solutions
	 * @return the index of the solution which we wish to remove
	 */
	private int weightedRandomIndex(ArrayList<CandidateSolution> solutions, double meanFitness) {

		//an arraylist with the upper threshold of each segment
		ArrayList<Double> probabilityThresholds = new ArrayList<>();
		double thresholdAccumulator = 0;
		for (CandidateSolution solution : solutions) {
			//calculate probability an add to the threshold accumulator
			double probability = solution.getFitness() * 1.0 / solutions.size() / meanFitness;
			thresholdAccumulator += probability;

			//accumulator is now the upper limit of this element
			probabilityThresholds.add(thresholdAccumulator);
		}

		//generate a value between 0 and 1
		double selectedValue = randomNumberGenerator.nextDouble();

		//if value at index < random value, the value is from that index
		for (int index = 0; index < probabilityThresholds.size(); index++) {
			if (probabilityThresholds.get(index) < selectedValue)
				return index;
		}

		//we should never actually hit this condition, but java requires it to compile, so our never-occuring default value is the last index
		return probabilityThresholds.size() - 1;
	}
}
