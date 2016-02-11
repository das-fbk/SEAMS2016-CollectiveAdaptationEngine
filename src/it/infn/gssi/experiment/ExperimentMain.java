package it.infn.gssi.experiment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Random;

import javax.naming.ConfigurationException;

import eu.fbk.das.adaptation.DemoManagementSystem;
import eu.fbk.das.adaptation.EnsembleManager;
import eu.fbk.das.adaptation.ensemble.Ensemble;
import eu.fbk.das.adaptation.presentation.CAWindow;

/**
 * this is the main file of the experiment for our SEAMS 2016 paper
 * 
 * @author Ivano Malavolta
 * 
 */
public class ExperimentMain {
	private final static String PROP_PATH = "adaptation.properties";

	public static void main(String[] args) throws ConfigurationException, FileNotFoundException {

		System.gc();
		// reading property file
		String propPath = PROP_PATH;
		if (args.length > 0) {
			propPath = args[0];
		}
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(propPath));
		} catch (IOException e) {
			System.err.println("Cannot read configuration file: " + e.getMessage());
		}
		// setting up repository path
		// if (props.getProperty("repo_path") == null) {
		// throw new ConfigurationException("repo_path property is not found in
		// the config file: execution aborted");
		// }
		;
		// demo management system construction

		DemoManagementSystem dms = DemoManagementSystem.initializeSystem("scenario/");

		List<Treatment> treatments = ExperimentMain.createTreatments();

		// Ensemble Creation - Instance of Ensemble 1
		Ensemble e1 = dms.getEnsemble("E1");
		EnsembleManager e1Manager = new EnsembleManager(e1);

		// Ensemble Creation - Instance of Ensemble 2
		Ensemble e2 = dms.getEnsemble("E2");
		EnsembleManager e2Manager = new EnsembleManager(e2);

		// Ensemble Creation - Instance of Ensemble 3
		Ensemble e3 = dms.getEnsemble("E3");
		EnsembleManager e3Manager = new EnsembleManager(e3);

		// Ensemble Creation - Instance of Ensemble 3
		// Ensemble e4 = dms.getEnsemble("E4");
		// EnsembleManager e4Manager = new EnsembleManager(e4);

		List<EnsembleManager> ensembles = new ArrayList<EnsembleManager>();
		ensembles.add(e1Manager);
		ensembles.add(e2Manager);
		ensembles.add(e3Manager);
		// ensembles.add(e4Manager);

		Utilities.buildSolversMap(ensembles);
		// System.out.println(Utilities.getSolversMap());

		System.gc();
		try {
			System.out.println("Experiment starting in 5 seconds...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ExperimentMain.runTreatments(treatments, ensembles);

		// remove the first element of the treatments list
		treatments.remove(0);
		Utilities.genericWriteFile(treatments, "treatments.csv");
		System.exit(1);

	}

	/*
	 * static void launchWindow(EnsembleManager ensembleManager) { CAWindow
	 * window = new CAWindow();
	 * window.loadActiveIssueResolutionsTable(ensembleManager.
	 * getActiveIssueResolutions(), ensembleManager); window.loadTreeFrame();
	 * ensembleManager.checkRoles(window);
	 * 
	 * //System.out.println("END SIMULATION"); }
	 */
	static List<Treatment> createTreatments() {

		List<Treatment> result = new ArrayList<Treatment>();
		int treatmentsForSubject = 1000;

		int[] v1Values = { 1, 250, 500, 750, 1000 };
		int[] othersValues = { 0, 20, 40, 60, 80, 100 };
		boolean fullyRandom = true;

		int currentTreatmentId = 1;

		if (fullyRandom) {
			for (int i = 0; i < v1Values.length; i++) {
				for (int t = 1; t <= treatmentsForSubject; t++) {
					result.add(createRandomTreatment(currentTreatmentId++, v1Values[i]));
				}
			}
		} else {
			// othersValues.length
			for (int i = 0; i < v1Values.length; i++) {
				for (int issueIndex = 0; issueIndex <= 4; issueIndex++) {
					for (int j = 0; j < othersValues.length; j++) {
						for (int t = 1; t <= treatmentsForSubject; t++) {
							result.add(createTreatment(currentTreatmentId++, v1Values[i], issueIndex, othersValues[j]));
						}
					}
				}
			}
		}
		Collections.shuffle(result, new Random(System.nanoTime()));
		// add a copy of the first element, it will be always at the beginning
		// and at the end of the list of treatments
		Treatment treatmentToAdd = ((Treatment) result.get(0)).clone();
		result.add(treatmentToAdd);
		return result;
	}

	static Treatment createTreatment(int id, int v1Value, int issueIndex, int othersValue) {
		Treatment result = new Treatment(id, v1Value, issueIndex, othersValue);
		result.populate();
		return result;
	}

	static Treatment createRandomTreatment(int id, int v1Value) {
		Treatment result = new Treatment(id, v1Value);
		result.populate();
		return result;
	}

	static void runTreatments(List<Treatment> treatments, List<EnsembleManager> ensembles) {
		ListIterator<Treatment> iterator = treatments.listIterator();
		List<ExperimentResult> results = new ArrayList<ExperimentResult>();
		CAWindow window = null;
		// CAWindow window = new CAWindow();
		int id = 1;
		while (iterator.hasNext()) {
			results.add(ExperimentMain.runTreatment(iterator.next(), ensembles, window, id));
			id = id + 1;
		}
		// remove the first result
		results.remove(0);
		Utilities.genericWriteFile(results, "results.csv");
	}

	static ExperimentResult runTreatment(Treatment treatment, List<EnsembleManager> ensembles, CAWindow window,
			int id) {
		System.out.println("" + id + " - " + treatment.toString());
		return ExperimentRunner.getInstance().run(treatment, ensembles, window);

	}
}
