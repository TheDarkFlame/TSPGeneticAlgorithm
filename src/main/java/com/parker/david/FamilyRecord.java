package com.parker.david;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * a chunk of a population that represents a set of parents to a set of offspring in a population before and after crossover
 * this class is largely for bookkeeping purposes, and is not required for functioning, but instead for tracking which parents created which offspring
 */
public class FamilyRecord {

	/**
	 * checks if matchSolution is an offspring of the parents in this record, if so returns the parents as a string, else returns nothing
	 *
	 * @param matchSolution the offspring solution we want to match
	 * @return a string indicating the parents of the match if any
	 */
	public String parentsIfOffspringMatches(CandidateSolution matchSolution) {
		if (offspring.contains(matchSolution))
			return "[" + parents.stream().map(CandidateSolution::toString).collect(Collectors.joining(",")) + "]";
		else
			return "";
	}

	/**
	 * internal structure to record parents that formed this family
	 */
	private ArrayList<CandidateSolution> parents;

	/**
	 * internal structure to record offspring that are formed from the parents
	 */
	private ArrayList<CandidateSolution> offspring;

	/**
	 * constructor
	 *
	 * @param parents   the parents as an array list
	 * @param offspring the offspring from the parents as an array list
	 */
	FamilyRecord(ArrayList<CandidateSolution> parents, ArrayList<CandidateSolution> offspring) {
		this.offspring = offspring;
		this.parents = parents;
	}

}
