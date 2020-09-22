package com.parker.david;

import de.vandermeer.asciitable.AsciiTable;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * a class to hold information about this generation, and allows initialisation of the next generation
 */
public class GAPopulationGeneration {
	/**
	 * accessor for the parent population of this generation
	 *
	 * @return a solution population
	 */
	public SolutionPopulation getParentPopulation() {
		return parentPopulation;
	}

	/**
	 * accessor for the offspring (after crossover only) for this generation
	 *
	 * @return a solution population
	 */
	public SolutionPopulation getOffspringPopulation() {
		return offspringPopulation;
	}

	/**
	 * accessor for the mutated offspring for this generation
	 *
	 * @return a solution population
	 */
	public SolutionPopulation getMutatedPopulation() {
		return mutatedPopulation;
	}

	/**
	 * accessor for the next population after selection from this generation (also is the parent population of the next generation)
	 *
	 * @return a solution population
	 */
	public SolutionPopulation getNextPopulation() {
		return nextPopulation;
	}

	/**
	 * the underlying data objects for the parent population
	 */
	private SolutionPopulation parentPopulation;

	/**
	 * the underlying data objects for the offspring(crossover only) population
	 */
	private SolutionPopulation offspringPopulation;

	/**
	 * the underlying data objects for the mutated population
	 */
	private SolutionPopulation mutatedPopulation;

	/**
	 * the underlying data objects for the output of selection phase population
	 */
	private SolutionPopulation nextPopulation;

	/**
	 * an array list of records indicating how crossover was performed
	 */
	private ArrayList<FamilyRecord> familyRecords;

	/**
	 * an array list of records indicating how mutation was performed
	 */
	private ArrayList<MutantRecord> mutantRecords;

	/**
	 * an integer corresponding to this generation, starting at generation 0
	 */
	private int generationNumber;

	/**
	 * the mean fitness of this generation
	 */
	private double meanFitness;

	/**
	 * the best solution of this generation
	 * depending on genetic algorithm functions, this may or may not be the incumbent
	 */
	private CandidateSolution bestSolutionThisGeneration;

	/**
	 * accessor for the best solution of the output from this generation
	 *
	 * @return the single best candidate solution this generation
	 */
	public CandidateSolution getBestSolutionThisGeneration() {
		return bestSolutionThisGeneration;
	}

	/**
	 * a setter for the family record array list
	 *
	 * @param familyRecords a family record array list
	 */
	public void setFamilyRecords(ArrayList<FamilyRecord> familyRecords) {
		this.familyRecords = familyRecords;
	}

	/**
	 * a setter for offspring (crossover only) population
	 *
	 * @param offspringPopulation a solution population
	 */
	public void setOffspringPopulation(SolutionPopulation offspringPopulation) {
		this.offspringPopulation = offspringPopulation;
	}

	/**
	 * a setter for the mutated population
	 *
	 * @param mutatedPopulation a solution population
	 */
	public void setMutatedPopulation(SolutionPopulation mutatedPopulation) {
		this.mutatedPopulation = mutatedPopulation;
	}

	/**
	 * a setter for the mutant records for this generation
	 *
	 * @param mutantRecords a mutant record array list
	 */
	public void setMutantRecords(ArrayList<MutantRecord> mutantRecords) {
		this.mutantRecords = mutantRecords;
	}

	/**
	 * the constructor. This initialises a chain of generations, this generation being the first one.
	 * Only call this once when initialising the entire system.
	 *
	 * @param initialPopulation the initial population for the very first generation
	 */
	GAPopulationGeneration(SolutionPopulation initialPopulation) {
		this.parentPopulation = initialPopulation;
		this.generationNumber = 0;
	}

	/**
	 * a setter for the selected population after mutation.
	 * This method also assigns the mean fitness and best solution of this generation when called
	 *
	 * @param nextPopulation the post selection population
	 */
	public void setNextPopulation(SolutionPopulation nextPopulation) {
		this.nextPopulation = nextPopulation;
		this.meanFitness = nextPopulation.meanFitness();
		this.bestSolutionThisGeneration = nextPopulation.getBestSolution();
	}

	/**
	 * this method creates a new generation from the current one.
	 * increments the generation number, and sets the parent population to the previous generation's offspring population
	 */
	GAPopulationGeneration initialiseNextGeneration() {
		GAPopulationGeneration nextGen = new GAPopulationGeneration(this.nextPopulation);
		nextGen.generationNumber = this.generationNumber + 1;
		return nextGen;
	}

	public void addToTable(AsciiTable table, CandidateSolution incumbent) {

		//print the header
		table.addRow("parent", "fitness", "offspring parents", "mutation", "final offspring","fitness", "final selected", "fitness");

		//for each element of the popluation
		for (int i = 0; i < parentPopulation.getPopulationSize(); i++) {
			//grab the final offspring (post crossover and mutation)
			CandidateSolution offspring = mutatedPopulation.getSolutions().get(i);

			//check if the offspring was mutated
			String mutationRecord = mutantRecords.stream()
					.map(mutantRecord -> mutantRecord.unmutatedIfMutatedMatches(offspring))
					.collect(Collectors.joining(""));

			//if mutation exists, we look for the original unmutated offspring as the lookup
			CandidateSolution parentRecordLookup = (!mutationRecord.isEmpty()) ? mutantRecords.get(0).getUnmutated() : offspring;

			//output get the parents
			String parentRecord = familyRecords.stream()
					.map(familyRecord -> familyRecord.parentsIfOffspringMatches(parentRecordLookup))
					.collect(Collectors.joining(""));

			//add a row to the table
			table.addRow(
					parentPopulation.getSolutions().get(i),
					parentPopulation.getSolutions().get(i).getFitness(),
					parentRecord,
					mutationRecord,
					offspring,
					offspring.getFitness(),
					nextPopulation.getSolutions().get(i),
					nextPopulation.getSolutions().get(i).getFitness()
			);
		}
		table.addLightRule();
		table.addRow("generation: " + generationNumber,
				"", "incumbent: " + incumbent,
				"incumbent fitness: " + incumbent.getFitness()
				, "mean fitness: " + meanFitness, "", "",""

		);
		table.addRule();
	}
}
