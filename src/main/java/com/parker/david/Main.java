package com.parker.david;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	/**
	 * entry point, initialises the cities, and the kicks off the GA
	 */
	public static void main(String[] args) {

		//initialise our cities with the distances to the other cities to set everything up for our algorithm
		ArrayList<City> cities = new ArrayList<>();
		int cityid = 0;
		cities.add(new City(cityid++, new ArrayList<>(Arrays.asList(0, 41, 26, 31, 27, 35))));
		cities.add(new City(cityid++, new ArrayList<>(Arrays.asList(41, 0, 29, 32, 40, 33))));
		cities.add(new City(cityid++, new ArrayList<>(Arrays.asList(26, 29, 0, 25, 34, 42))));
		cities.add(new City(cityid++, new ArrayList<>(Arrays.asList(31, 32, 25, 0, 28, 34))));
		cities.add(new City(cityid++, new ArrayList<>(Arrays.asList(27, 40, 34, 28, 0, 36))));
		cities.add(new City(cityid++, new ArrayList<>(Arrays.asList(35, 33, 42, 34, 36, 0))));

		//run the genetic algorithm
		runGeneticAlgorithm(cities);

	}

	/**
	 * a method that runs the genetic algorithm on the TSP problem with a given set of cities
	 * uses random generation, two-point crossover, element swap mutation, and elitist selection.
	 * stops when incumbent does not improve for 10 iterations
	 *
	 * @param cities the set of already created cities for which we want to optimise the TSP route
	 */
	public static void runGeneticAlgorithm(ArrayList<City> cities) {

		//select our strategies for our genetic algorithm
		PopulationInitialiser initialiser = new RandomGeneration();//random generation of initial population as initialisation strategy
		PopulationCrossover breeder = new TwoPointCrossOver();//two-point crossover as crossover strategy
		PopulationMutator mutator = new SwapTwoCities();//swap of two elements as mutation strategy
		PopulationReplacement selector = new ElitistSelection();//elitism as selection strategy

		//create a generation history object to track all generations
		ArrayList<GAPopulationGeneration> generationHistory = new ArrayList<>();

		//create our first generation and randomly generate its initial solutions
		GAPopulationGeneration generation = new GAPopulationGeneration(initialiser.initialise(cities, 8));

		// create our output table
		AsciiTable outputTable = new AsciiTable();
		outputTable.addRule();

		// create our incumbent and stopping criterion tracker
		CandidateSolution incumbent = generation.getParentPopulation().getBestSolution();
		int generationSinceImprovedIncumbent = 0;

		//loop until 10 iterations without improved incumbent
		while (generationSinceImprovedIncumbent <= 10) {

			//take parents and crossover to create offspring. record the results for displaying later
			generation.setOffspringPopulation(breeder.breed(generation.getParentPopulation()));
			generation.setFamilyRecords(breeder.getFamilies());

			//take offspring and perform mutation. record the results for displaying later
			generation.setMutatedPopulation(mutator.mutatePopulation(generation.getOffspringPopulation()));
			generation.setMutantRecords(mutator.getMutations());

			//take mutated offspring and perform selection.
			generation.setNextPopulation(selector.replace(generation.getParentPopulation(), generation.getMutatedPopulation()));

			//if this solution is greater than the incumbent reset stopping criterion and we have new incumbent, else increment stopping criterion
			if (generation.getBestSolutionThisGeneration().compareTo(incumbent) < 0) {
				generationSinceImprovedIncumbent = 0;
				incumbent = generation.getBestSolutionThisGeneration();
			} else
				generationSinceImprovedIncumbent++;

			//record the current generation, and create the new generation from the current one
			generation.addToTable(outputTable, incumbent);
			generationHistory.add(generation);
			generation = generation.initialiseNextGeneration();

		}

		//format the table and print it out
		outputTable.getRenderer().setCWC(new CWC_LongestLine());
		outputTable.getContext().setGrid(A8_Grids.lineDoubleBlocks());
		outputTable.setTextAlignment(TextAlignment.CENTER);
		System.out.println(outputTable.render());
	}
}
