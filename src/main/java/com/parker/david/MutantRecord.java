package com.parker.david;

/**
 * a chunk of a population that represents a mutation from unmutated to mutated
 * this class is largely for bookkeeping purposes, and is not required for functioning, but instead for tracking which unmutated create which mutated
 */
public class MutantRecord {
	/**
	 * getter for the underlying unmutated (original) solution
	 *
	 * @return an unmutated solution
	 */
	public CandidateSolution getUnmutated() {
		return unmutated;
	}

	/**
	 * internal structure to record the unmutated (original) solution
	 */
	private CandidateSolution unmutated;

	/**
	 * internal structure to record the mutated solution
	 */
	private CandidateSolution mutated;

	/**
	 * constructor
	 *
	 * @param unmutated the unmutated(original) solution
	 * @param mutated   the solution that come from mutating the unmutated solution
	 */
	MutantRecord(CandidateSolution unmutated, CandidateSolution mutated) {
		this.unmutated = unmutated;
		this.mutated = mutated;
	}

	/**
	 * checks if matchSolution matches the unmutated record, if so returns the mutation, else returns empty string
	 *
	 * @param matchSolution the unmutated solution we want to match
	 * @return a string indicating the mutation (if any) of the match solution
	 */
	public String unmutatedIfMutatedMatches(CandidateSolution matchSolution) {
		if (matchSolution.equals(mutated))
			return unmutated + "->" + mutated;
		else
			return "";
	}
}
