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
		} while (rand.equals(blacklistedNumber));
		return rand;
	}

	/**
	 * takes a population and breaks it down into sets of parents. each set of parents generates a set of offspring
	 * the offspring sets are merged to form an offspring population.
	 *
	 * @param parents                 the population of parents
	 * @param offspringPopulationSize the size of the desired offspring population
	 * @return the population of offspring
	 */
	@Override
	public SolutionPopulation breed(SolutionPopulation parents, int offspringPopulationSize) {
		// reset families to empty
		families = new ArrayList<>();

		// set selection method
		ParentSelector parentSelector = new TournamentSelection(3);

		//create offspring solution set
		ArrayList<CandidateSolution> offspringSolutions = new ArrayList<>();

		//create a working(temporary) population of parents
		SolutionPopulation parentPool = parents.copy();

		//select 2 parents and crossover, repeat until we have offspringPopulationSize parents
		do {
			//select 2 parents using parent selection strategy,
			// remove the parent from the pool of parents available for selection when it is selected
			CandidateSolution parent1 = parentSelector.getParent(parentPool);
			parentPool.getSolutions().remove(parent1);
			CandidateSolution parent2 = parentSelector.getParent(parentPool);
			parentPool.getSolutions().remove(parent2);

			//breed and add offspring
			offspringSolutions.addAll(breedPair(parent1, parent2));
		} while (offspringSolutions.size() / 2 < offspringPopulationSize / 2);
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
		ArrayList<City> parent1_cities = parent1.getCities();
		ArrayList<City> parent2_cities = parent2.getCities();

		// extract middle from the first parent
		ArrayList<City> offspring1_parent1cities = new ArrayList<>(parent1_cities.subList(crossover1, crossover2));
		ArrayList<City> offspring2_parent2cities = new ArrayList<>(parent2_cities.subList(crossover1, crossover2));

		// create our offspring city permutation
		ArrayList<City> offspring1 = new ArrayList<>();
		ArrayList<City> offspring2 = new ArrayList<>();

		int count = parent2.cityCount();
		for (int i = crossover2; i < crossover2 + count; i++) {
			//if the offspring will not get this city from the first parent, add the city from the second parent
			if (!offspring1_parent1cities.contains(parent2_cities.get(i % count)))
				offspring1.add(parent2_cities.get(i % count));
			if (!offspring2_parent2cities.contains(parent1_cities.get(i % count)))
				offspring2.add(parent1_cities.get(i % count));

			//if we are at the end of the arraylist, and about to loop back around, add in all the cities from the first parent
			if (i == count - 1) {
				offspring1.addAll(offspring1_parent1cities);
				offspring2.addAll(offspring2_parent2cities);
			}
		}


		//return the offspring
		ArrayList<CandidateSolution> offspring = new ArrayList<>();
		offspring.add(new CandidateSolution(offspring1));
		offspring.add(new CandidateSolution(offspring2));

		//add the parents for family
		ArrayList<CandidateSolution> parents = new ArrayList<>();
		parents.add(parent1);
		parents.add(parent2);

		//record the family and return the result
		families.add(new FamilyRecord(parents, offspring));
		return offspring;
	}

}
