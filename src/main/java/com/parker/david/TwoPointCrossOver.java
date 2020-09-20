package com.parker.david;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * the implementation of a population crossover algorithm, in this case the algorithm is 2point crossover.
 */
public class TwoPointCrossOver implements PopulationCrossover {

	/**
	 * a random number generator
	 */
	private final Random randomNumberGenerator = ThreadLocalRandom.current();

	/**
	 * an internal record of the last population breeding performed as stored by a family record
	 */
	private ArrayList<FamilyRecord> families = new ArrayList<>();

	/**
	 * a random number generator wrapper that will generate (inclusive) from lowest possible value to highest possible value.
	 * also supports a blacklisted value, this value will not be generated.
	 *
	 * @param lowestPossibleValue  the lowest value that may be generated
	 * @param highestPossibleValue the highest value that may be generated
	 * @param blacklistedNumber    a number in the range that is not generated, set to null if full range is needed
	 * @return a random int
	 */
	private int randomNumberGenerator(Integer lowestPossibleValue, Integer highestPossibleValue, Integer blacklistedNumber) {
		Integer rand;
		do {
			rand = randomNumberGenerator.nextInt(highestPossibleValue + 1 - lowestPossibleValue) + lowestPossibleValue;
		} while (!rand.equals(blacklistedNumber));
		return rand;
	}

	/**
	 * takes a population and breaks it down into sets of parents. each set of parents generates a set of offspring
	 * the offspring sets are merged to form an offspring population.
	 *
	 * @param parents the population of parents
	 * @return the population of offspring
	 */
	@Override
	public SolutionPopulation breed(SolutionPopulation parents) {
		// reset families to empty
		families = new ArrayList<>();

		//create offspring solution set
		ArrayList<CandidateSolution> offspringSolutions = new ArrayList<>();

		//create a pool of parent solutions to pull from
		ArrayList<CandidateSolution> parentSolutions = (ArrayList<CandidateSolution>) parents.getSolutions().clone();

		//pull 2 solutions from parents, breed them, and put them into the offspring solutions
		while (!parentSolutions.isEmpty()) {
			CandidateSolution parent1 = parentSolutions.remove(randomNumberGenerator.nextInt(parentSolutions.size()));
			CandidateSolution parent2 = parentSolutions.remove(randomNumberGenerator.nextInt(parentSolutions.size()));
			offspringSolutions.addAll(breedPair(parent1, parent2));//breed and add offspring
		}
		return new SolutionPopulation(offspringSolutions);
	}

	/**
	 * accessor for the families records, will contain information regarding which parents generated which offspring
	 */
	@Override
	public ArrayList<FamilyRecord> getFamilies() {
		return families;
	}

	/**
	 * this is the function that performs the crossover of the two parents and generates two offspring.
	 * it is done by randomly selecting two crossover points such that there are at least 1 element at the beginning
	 * and 1 element at the end of the original set of cities.
	 * from there, the middles of the two parents are swapped to create two offspring.
	 *
	 * @param parent1 the first parent to breed
	 * @param parent2 the second parent to breed
	 * @return the offspring in an array list
	 */
	public ArrayList<CandidateSolution> breedPair(CandidateSolution parent1, CandidateSolution parent2) {
		//since crossover points are actually between elements, we generate as:
		//crossover point = between the element at index <n-1> and <n> where n is randomly generated.
		//this way element n is the first element to be swapped. for the next crossover section
		int lowerBound = 1; //n may not be the first element, 0
		int upperBound = parent1.cityCount() - 1;//n may be the last element, element cityCount-1

		//generate the crossover points
		int crossover1 = randomNumberGenerator(lowerBound, upperBound, null);
		int crossover2 = randomNumberGenerator(lowerBound, upperBound, crossover1);
		//ensure that crossover1 has the smaller value
		if (crossover1 > crossover2) {
			int temp = crossover2;
			crossover2 = crossover1;
			crossover1 = temp;
		}

		//offspring1=parent1 with the middle from parent2, vica versa
		//do this in a for loop
		CandidateSolution offspring1 = parent1.copy();
		CandidateSolution offspring2 = parent2.copy();
		for (int i = crossover1; i < crossover2; ++i) {
			offspring1.getCities().set(i, parent2.getCities().get(i));
			offspring2.getCities().set(i, parent1.getCities().get(i));
		}

		//return the offspring
		ArrayList<CandidateSolution> offspring = new ArrayList<>();
		offspring.add(offspring1);
		offspring.add(offspring2);

		//add the parents for family
		ArrayList<CandidateSolution> parents = new ArrayList<>();
		parents.add(parent1);
		parents.add(parent2);

		//record the family and return the result
		families.add(new FamilyRecord(parents, offspring));
		return offspring;
	}

}