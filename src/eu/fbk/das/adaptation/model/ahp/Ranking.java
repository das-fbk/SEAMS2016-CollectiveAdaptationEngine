package eu.fbk.das.adaptation.model.ahp;

import java.util.ArrayList;

public class Ranking {
    public Ranking() {
	// super();
    }

    Alternatives alternatives;
    Comparison criteria;
    double[] rankings;

    // for 2-tier criteria hierarchy
    public Ranking(int instanceNumber, ArrayList<ArrayList<Double>> instanceImportance,
	    ArrayList<ArrayList<ArrayList<Double>>> criteriaImportance, ArrayList<ArrayList<Double>> alternativeValues,
	    int[] minmaxCriteriaOptions) {
	// instanceNumber - number of instances whose utilities have to be
	// considered
	// instanceImportance - matrix of pairwise comparisons of utility
	// importance set by the current resolver (size =
	// instanceNumber*instanceNumber)
	// criteriaImportance - array list (size = instanceNumber) that contains
	// matrices of pairwise comparisons of preference importance (C matrices
	// defined in the role instances)
	// alternativesValues - values of alternatives with respect to each
	// preference; size m*n where m is a total number of preferences of all
	// instances, n is a number of alternatives
	// minmaxCriteriaOptions - m array of flags indicating whether a certain
	// criterion must be minimized (0) or maximized (1)
	criteria = new CriteriaHierarchy(instanceNumber, instanceImportance, criteriaImportance);
	alternatives = new Alternatives(alternativeValues, minmaxCriteriaOptions);
	if (criteria.weightVector.length != alternatives.scores[0].length)
	    System.out.println(
		    "mismatching sizes: " + criteria.weightVector.length + " vs " + alternatives.scores.length);
	else
	    calculateRanking();

    }

    public Ranking(int instanceNumber, String instanceImportanceFile, String[] criteriaImportanceFiles,
	    String[] filesAlternatives, int[] minmaxCriteriaOptions) {
	criteria = new CriteriaHierarchy(instanceNumber,
		FileOperations.fileToArrayListOfArrayLists(instanceImportanceFile),
		FileOperations.fileArrayTo3DArrayList(criteriaImportanceFiles));
	alternatives = new Alternatives(FileOperations.fileArrayTo2DArrayList(filesAlternatives),
		minmaxCriteriaOptions);
	if (criteria.weightVector.length != alternatives.scores[0].length)
	    System.out.println(
		    "mismatching sizes: " + criteria.weightVector.length + " vs " + alternatives.scores.length);
	else
	    calculateRanking();

    }

    private void calculateRanking() {
	// calculates final rankings of alternatives
	int aRows = alternatives.scores.length;
	int aColumns = alternatives.scores[0].length;
	int bRows = criteria.weightVector.length;
	int bColumns = 1;

	if (aColumns != bRows) {
	    throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
	}

	rankings = new double[aRows];
	System.out.print("final scores:    ,");
	for (int i = 0; i < aRows; i++) { // aRow
	    rankings[i] = 0.0;
	    for (int j = 0; j < bRows; j++) { // bColumn
		rankings[i] += alternatives.scores[i][j] * criteria.weightVector[j];
	    }
	    System.out.print(rankings[i] + ",");
	}

    }

    public double[] getRanking() {
	// returns rankings
	return rankings;
    }

    public int returnBestAlternative() {
	int index = 0;
	double best = rankings[0];
	for (int i = 1; i < rankings.length; i++) {
	    if (rankings[i] > best) {
		index = i;
		best = rankings[i];
	    }
	}
	return index;
    }

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	// String [] files = new
	// String[]{"data/SASO/alternativesValuesUtility.csv"};//,
	// "data/SASO/alternativesValuesPreferences.csv",
	// "data/SASO/alternativesValuesData.csv"};
	// int [] options = new int [] {1};//, 0, 0};
	// CriteriaHierarchy hierarchy = new CriteriaHierarchy(2,"data/SASO/",
	// criteriaImportanceFiles);

	/*
	 * String [] criteriaImportanceFiles = new
	 * String[]{"data/SASO/criteriaComparison_A.csv",
	 * "data/SASO/criteriaComparison_B.csv",
	 * "data/SASO/criteriaComparison_C.csv",
	 * "data/SASO/criteriaComparison_D.csv"}; String [] filesAlternatives =
	 * new String[]{"data/SASO/FB2ChangeRoute/alternativesPA_time.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPA_cost.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPA_walking.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPB_time.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPB_cost.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPC_time.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPD_cost.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPC_walking.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPD_cost.csv",
	 * "data/SASO/FB2ChangeRoute/alternativesPD_walking.csv", }; int []
	 * options = new int [] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; Ranking ranking
	 * = new Ranking
	 * (4,"data/SASO/FB2ChangeRoute/utilityComparison_FB2.csv",
	 * criteriaImportanceFiles, filesAlternatives, options);
	 */
	/*
	 * String [] criteriaImportanceFiles = new
	 * String[]{"data/SASO/criteriaComparison_A.csv",
	 * "data/SASO/criteriaComparison_B.csv",
	 * "data/SASO/criteriaComparison_E.csv"}; String [] filesAlternatives =
	 * new String[]{"data/SASO/FB3ChangeRoute/alternativesPA_time.csv",
	 * "data/SASO/FB3ChangeRoute/alternativesPA_cost.csv",
	 * "data/SASO/FB3ChangeRoute/alternativesPA_walking.csv",
	 * "data/SASO/FB3ChangeRoute/alternativesPB_time.csv",
	 * "data/SASO/FB3ChangeRoute/alternativesPB_cost.csv",
	 * "data/SASO/FB3ChangeRoute/alternativesPE_time.csv",
	 * "data/SASO/FB3ChangeRoute/alternativesPE_cost.csv",
	 * "data/SASO/FB3ChangeRoute/alternativesPE_walking.csv", }; int []
	 * options = new int [] {0, 0, 0, 0, 0, 0, 0, 0}; Ranking ranking = new
	 * Ranking (3,"data/SASO/FB3ChangeRoute/utilityComparison_FB3.csv",
	 * criteriaImportanceFiles, filesAlternatives, options);
	 */

	String[] criteriaImportanceFiles = new String[] { "data/SASO/criteriaComparison_A.csv",
		"data/SASO/criteriaComparison_B.csv", "data/SASO/criteriaComparison_FBC.csv" };
	String[] filesAlternatives = new String[] { "data/SASO/FBCFindNew/alternativesPA_time.csv",
		"data/SASO/FBCFindNew/alternativesPA_cost.csv", "data/SASO/FBCFindNew/alternativesPA_walking.csv",
		"data/SASO/FBCFindNew/alternativesPB_time.csv", "data/SASO/FBCFindNew/alternativesPB_cost.csv",
		"data/SASO/FBCFindNew/alternativesFBC_cost.csv", };
	int[] options = new int[] { 0, 0, 0, 0, 0, 0 };
	Ranking ranking = new Ranking(3,
		FileOperations.fileToArrayListOfArrayLists("data/SASO/FBCFindNew/utilityComparison_FBC.csv"),
		FileOperations.fileArrayTo3DArrayList(criteriaImportanceFiles),
		FileOperations.fileArrayTo2DArrayList(filesAlternatives), options);
	double[] finalRanks = ranking.getRanking();

	// Alternatives alternatives = new Alternatives
	// (FileOperations.fileArrayToArrayList(filesAlternatives), options);
	// ranking.getRanking();

    }

}
