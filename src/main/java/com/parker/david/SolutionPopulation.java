package com.parker.david;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * the solution population. a holding class for a single population of solutions.
 * This contains some useful functionality, such as finding the best solution, and calculating the mean fitness
 * as it contains only references to the underlying candidate solutions (which also in turn contain references)
 * it should have a low memory footprint
 */
public class SolutionPopulation {

	/**
	 * the accessor to the underlying solution data structure
	 *
	 * @return an array list of candidate solutions
	 */
	public ArrayList<CandidateSolution> getSolutions() {
		return solutions;
	}

	/**
	 * internal data structure for storing references to all the solutions
	 */
	private final ArrayList<CandidateSolution> solutions;

	/**
	 * constructor
	 *
	 * @param solutionSet an array list of solutions to create this population
	 */
	SolutionPopulation(ArrayList<CandidateSolution> solutionSet) {
		this.solutions = solutionSet;
	}

	/**
	 * the number of solutions in this population
	 *
	 * @return an int for the number of solutions in this population
	 */
	public int getPopulationSize() {
		return solutions.size();
	}

	/**
	 * an accessor for the best solution contained within this population
	 * according to the comparison method defined in the solution class
	 *
	 * @return the best candidate solution in this population
	 */
	public CandidateSolution getBestSolution() {
		return Collections.max(solutions);
	}

	/**
	 * get the average (mean) fitness of this population
	 *
	 * @return a double for mean population fitness
	 */
	double meanFitness() {
		return solutions.stream().mapToInt(CandidateSolution::getFitness).summaryStatistics().getAverage();
	}

	/**
	 * a semi-deep copy method, the array list in the new object is not linked to that of the old one. The solutions themselves are shared.
	 * changes to the internal array list of the new object do not affect the array list of the old one.
	 * If the solutions themselves are changed (this should not happen), then both the new and old solution populations are affected
	 *
	 * @return a new population with a new array list containing solutions from the old population
	 */
	SolutionPopulation copy() {
		ArrayList<CandidateSolution> populationSolutions = new ArrayList<>();
		for (CandidateSolution solution : solutions) {
			populationSolutions.add(solution);
		}
		return new SolutionPopulation(populationSolutions);
	}
}
