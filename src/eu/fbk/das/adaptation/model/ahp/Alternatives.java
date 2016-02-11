package eu.fbk.das.adaptation.model.ahp;

import java.util.ArrayList;

import eu.fbk.das.adaptation.model.IssueCommunication;


public class Alternatives {
	//Comparison [] optionComparisons; // Comparisons of alternative solutions for each criteria
	double [][] scores; // m x n matrix of scores of alternative solutions for each criteria, where n is number of criteria, m is number of alternatives
	
	
	public Alternatives() {
		
		// TODO Auto-generated constructor stub
	}
	
	public Alternatives(ArrayList<ArrayList<Double>> alternatives, int [] minmaxFlags) {
		Comparison [] optionComparisons; 
		// TODO Auto-generated constructor stub
		optionComparisons = new Comparison [alternatives.size()];
		for (int j = 0; j < alternatives.size(); j++) {
			optionComparisons[j] = new Comparison (alternatives.get(j), minmaxFlags[j]);
			double [] weights = optionComparisons[j].getWeightVector();
			if (j == 0) {
				scores = new double [weights.length][alternatives.size()];
			}
			for (int i = 0; i < weights.length; i ++) {
				scores[i][j] = weights[i];
			}
		}
		
	}
	public Alternatives(String file, int [] minmaxFlags){
		
	}
	
	public static void main(String[] args) {
		
	}

	public ArrayList<IssueCommunication> getOutgoing() {
		// TODO Auto-generated method stub
		return null;
	}
}
