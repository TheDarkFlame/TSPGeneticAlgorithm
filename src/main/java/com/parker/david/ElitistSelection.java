package com.parker.david;

import java.util.ArrayList;
import java.util.Collections;

/**
 * this is an elitist selection strategy, it joins the parents and offspring into a single pool
 * and selects the best solutions from that pool, not caring whether it is a parent or offspring
 */
public class ElitistSelection implements PopulationReplacement {
	/**
	 * using parents and offspring select a new set of solutions that are the best of according to best fitness
	 *
	 * @param offspring the offspring population generated from the parents
	 * @param parents   the parent population that started this generation
	 * @return the best of both parents and offspring
	 */
	@Override
	public SolutionPopulation replace(SolutionPopulation parents, SolutionPopulation offspring) {
		//put all solutions into a single list
		ArrayList<CandidateSolution> allSolutions = new ArrayList<>();
		allSolutions.addAll(parents.getSolutions());
		allSolutions.addAll(offspring.getSolutions());

		//sort solutions (based on fitness, as implemented in CandidateSolution)
		Collections.sort(allSolutions);

		//using the size of the original population, select a new population (based on them being ordered by fitness)
		ArrayList<CandidateSolution> bestSolutions = new ArrayList<>(allSolutions.subList(0, parents.getPopulationSize()));

		//return a new population object
		return new SolutionPopulation(bestSolutions);
	}
}
