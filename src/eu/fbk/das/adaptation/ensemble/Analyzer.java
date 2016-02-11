package eu.fbk.das.adaptation.ensemble;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.swing.JPanel;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxTraversal;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraph.mxICellVisitor;

import eu.fbk.das.adaptation.EnsembleManager;
import eu.fbk.das.adaptation.RoleManager;
import eu.fbk.das.adaptation.model.IssueCommunication;
import eu.fbk.das.adaptation.model.IssueCommunication.CommunicationStatus;
import eu.fbk.das.adaptation.model.IssueResolution;
import eu.fbk.das.adaptation.model.Target;
import eu.fbk.das.adaptation.model.Target.TargetStatus;
import eu.fbk.das.adaptation.presentation.CANode;
import eu.fbk.das.adaptation.presentation.CATree;
import eu.fbk.das.adaptation.presentation.CAWindow;
import eu.fbk.das.adaptation.utils.Pair;

/**
 * This is the class that realizes the Analyzer (of the MAPE) behavior of a Role
 * 
 * @author Antonio
 * 
 */

public class Analyzer {

    private List<Solver> localSolvers;

    private static final String STYLE_ROLE = "verticalAlign=middle;dashed=false;dashPattern=5;rounded=true;align=center";
    private static final String STYLE_COM = "verticalAlign=middle;fillColor=FFF000";
    private static final String STYLE_OR = "verticalAlign=middle;shape=rhombus;fillColor=red;strokeColor=black";
    private static final String STYLE_INIT = "verticalAlign=middle;dashed=false;dashPattern=5;rounded=true;fillColor=white";
    private static final String STYLE_AND = "verticalAlign=middle;shape=rhombus;fillColor=black;strokeColor=black";
    private static final String STYLE_ISSUE_EDGE = "fontColor=#FF0000;endArrow=classic;html=1;fontFamily=Helvetica;align=left";
    private static final String STYLE_DOTTED_EDGE = "dashed=1;fontColor=#FF0000;";
    private static int distance = 0;
    private static int depth = 0;

    private static int miniumExtent = 1;
    private static int maximumExtent = 1;
    private static boolean printed = false;

    public List<Solver> getLocalSolver() {
	return localSolvers;

    }

    public void setLocalSolver(List<Solver> localSolvers) {
	this.localSolvers = localSolvers;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void resolveIssue(String capID, IssueResolution res, EnsembleManager en, CAWindow window, CATree cat) {

	// Resolution Tree of the role is resolving the issue

	// SWITCH on the Issue Resolution State for the specific role

	switch (res.getStatus()) {
	case "ISSUE_RECEIVED": {

	    RoleManager rm = res.getRoleCurrent();
	    Issue is = res.getIssueInstance();
	    String roleName = rm.getRole().getType();

	    // [attiva]window.updateResolutions(capID, res, en);
	    // check if there are solvers

	    // rm.addraisedIssue();
	    // System.out.println(rm.getRaisedIssues());
	    manageIssueReceived(capID, res, en, cat);

	    Pair pair = new Pair(capID, rm);
	    // //System.out.println(pair.toString());
	    // //System.out.println(res.getRoleCurrent().getRole().getType());
	    String count = "0";
	    // [attiva]String count = Integer.toString(window.counter);
	    en.getTrees().put(count, cat);
	    // int depth = this.grapDepth(cat);
	    this.depth = 0;
	    int minDepth = this.MinimumDepth(cat);
	    rm.setMinDepth(minDepth);
	    // System.out.println("MinDepth: " + rm.getMinDepth() + "RECEIVED");

	    this.distance = 0;
	    int maxDepth = this.maxDepth(cat);
	    rm.setMaxDepth(maxDepth);
	    // System.out.println("MaxDepth: " + rm.getMinDepth() + "RECEIVED");

	    int minExtent = this.MinExtent(cat);
	    rm.setMinExtent(minExtent);

	    int maxExtent = this.MaxExtent(cat);
	    // System.out.println("max Extent: " + maxExtent + " RECEIVED");
	    rm.setMaxExtent(maxExtent);

	}
	    break;

	case "SOLUTION_FORWARDED": {
	    // System.out.println(res.getRoleCurrent().getRole().getType() + "
	    // *****COMMIT_RECEIVED*****");
	    res.setStatus("COMMIT_RECEIVED");
	    // System.out.println(res.getRoleCurrent().getRole().getType() + "
	    // *****EXECUTED*****");
	    res.setStatus("EXECUTED");
	    // System.out.println(res.getRoleCurrent().getRole().getType() + "
	    // *****END*****");
	    res.setStatus("End");

	}
	    break;

	case "ISSUE_TRIGGERED": {

	    RoleManager rm = res.getRoleCurrent();
	    Issue is = res.getIssueInstance();
	    String roleName = rm.getRole().getType();
	    // [attiva] window.updateResolutions(capID, res, en);
	    // generate the local tree to the role that triggers the issue
	    // System.out.println(rm.getRaisedIssues());

	    generateSubTree(capID, res, en, cat, rm, is, roleName, window);

	    String count = "0";
	    // [attiva] String count = Integer.toString(window.counter);
	    en.getTrees().put(count, cat);
	    // int depth = this.grapDepth(cat);
	    this.depth = 0;
	    int minDepth = this.MinimumDepth(cat);
	    rm.setMinDepth(minDepth);
	    // System.out.println("MinDepth: " + rm.getMinDepth() +
	    // "TRIGGERED");

	    this.distance = 0;
	    int maxDepth = this.maxDepth(cat);
	    rm.setMaxDepth(maxDepth);
	    // System.out.println("MaxDepth: " + rm.getMinDepth() +
	    // "TRIGGERED");

	    int minExtent = this.MinExtent(cat);
	    rm.setMinExtent(minExtent);

	    int maxExtent = this.MaxExtent(cat);
	    rm.setMaxExtent(maxExtent);

	}
	    break;
	case "SOLUTIONS_RETURNED": {
	    for (int i = 0; i < res.getCommunications().size(); i++) {
		IssueCommunication com = res.getCommunications().get(i);
		boolean allReturned = false;
		for (int j = 0; j < com.getTargets().size(); j++) {
		    Target t = com.getTargets().get(j);
		    if (t.getStatus() == TargetStatus.CALCULATED) {
			allReturned = true;
		    }

		}
		if (allReturned) {
		    // here we choose the best target
		    // AHP should be inserted here

		    // System.out.println(res.getRoleCurrent().getRole().getType()
		    // + " *****SOLUTION_CHOOSEN*****");
		    res.setStatus("SOLUTION_CHOOSEN");

		    // differenziare se la solution personale ha anche una parte
		    // locale o no

		    // System.out.println(res.getRoleCurrent().getRole().getType()
		    // + " *****SOLUTION_COMMITTED*****");
		    res.setStatus("SOLUTION_COMMITTED");
		    //
		    // System.out.println(res.getRoleCurrent().getRole().getType()
		    // + " *****END*****");
		    res.setStatus("End");

		}

	    }

	}
	    break;

	case "End": {

	    // System.out.println("ISSUE RISOLTA");

	}
	    break;
	default:
	    // System.out.println("*****DEFAULT*****");
	    break;
	}

	// update the tree assigned at the specific role
	// Pair pair = new Pair(capID, res.getRoleCurrent());
	// //System.out.println(pair.toString());
	// //System.out.println(res.getRoleCurrent().getRole().getType());
	// en.getTrees().put(pair, cat);
	// en.getTrees().put(res.getRoleCurrent(), cat);

	// AHP used to find the best solution among Final Solutions
	// Ranking ranking = new Ranking();
	// double[] finalRanks = ranking.getRanking();

	// return solution;
	// return new Solution();
    }

    private void manageIssueReceived(String capID, IssueResolution res, EnsembleManager en, CATree cat) {

	Object currentNode = new CANode();
	Object root = cat.insertNode(cat.getDefaultParent(), "INIT", STYLE_INIT);
	currentNode = root;

	Role r = res.getRoleCurrent().getRole();
	RoleManager rm = res.getRoleCurrent();
	String roleType = r.getType();

	String issueName = res.getIssueInstance().getIssueType();
	// System.out.println(roleType + " *****ISSUE_RECEIVED*****");
	// System.out.println("Issue to solve: " + issueName + " - for role: " +
	// roleType);

	// update of experiments variable dv4 - number of issue received
	// rm.addreceivedIssue();

	// search the solver to use and the issues it triggers
	for (int i = 0; i < r.getSolver().size(); i++) {
	    Solver currentSolver = r.getSolver().get(i);

	    if (currentSolver.getIssue().getIssueType().equals(issueName)) {

		String label = "ISSUE_RECEIVED: " + issueName + "\n" + "Solver: " + currentSolver.getName();

		Object v = cat.insertNode(currentNode, roleType, STYLE_ROLE);
		cat.insertEdge(cat.getDefaultParent(), "", label, currentNode, v, STYLE_ISSUE_EDGE);
		currentNode = v;

		// we take the solution proposed by the solver (for the
		// moment the first solution
		Solution sol = currentSolver.getSolution().get(0);
		if (sol.getIssue().size() == 0) {
		    // no Issues to trigger - only local solution
		    // In thi case it will forward the solution to the
		    // father

		    String roleName = r.getType();
		    String label1 = "INTERNAL_SOLUTION";
		    Object v1 = cat.insertNode(currentNode, roleName, STYLE_ROLE);
		    cat.insertEdge(cat.getDefaultParent(), "", label1, currentNode, v1, STYLE_ISSUE_EDGE);
		    currentNode = v1;

		    // System.out.println(res.getRoleCurrent().getRole().getType()
		    // + " *****SOLUTION_FORWARDED*****");
		    res.setStatus("SOLUTION_FORWARDED");
		    // System.out.println(res.getRoleCurrent().getRole().getType()
		    // + " *****ISSUE_ENDED*****");
		    res.setStatus("End");

		} else {
		    // Extra Issue to be triggered as part of a solution

		    ArrayList<Issue> issues = new ArrayList<Issue>();
		    for (int j = 0; j < sol.getIssue().size(); j++) {
			issues.add(sol.getIssue().get(j));
		    }

		    ArrayList<IssueCommunication> communications = new ArrayList<IssueCommunication>();
		    communications = this.targetIssues(issues, en, rm);
		    if (communications.size() == 0) {
			// System.out.println("Issue non risolvibile
			// nell'ensembles corrente: " +
			// en.getEnsemble().getName());

			// [TO BE MODIFIED]
			String label1 = "ISSUE TO EXTERNAL ENSEMBLE ";
			Object v1 = cat.insertNode(currentNode, roleType, STYLE_ROLE);
			cat.insertEdge(cat.getDefaultParent(), "", label1, currentNode, v1, STYLE_ISSUE_EDGE);
			currentNode = v1;

			// update of experiments variable dv5 - number of
			// cross-ensemble issue triggered
			// rm.addcrossEnsembleIssues();

			rm.addcrossEnsembleIssues();

			// ISSUE TARGETED
			// System.out.println(res.getRoleCurrent().getRole().getType()
			// + " *****ISSUE_TARGETED*****");
			res.setStatus("ISSUE_TARGETED");

			// END
			// System.out.println(res.getRoleCurrent().getRole().getType()
			// + " *****ISSUE_END*****");
			res.setStatus("End");

			break;
		    } else {
			// System.out.println("Communications created for the
			// Issue: " + communications.size());
			res.setCommunications(communications);

			// ADD AND NODE IF MORE THEN ONE COMMUNICATION
			// EXISTS
			if (communications.size() > 1) {
			    // FIRST CREATE THE AND NODE
			    Object v1 = cat.insertNode(currentNode, "AND", STYLE_AND);
			    cat.insertEdge(cat.getDefaultParent(), "", "", currentNode, v1, STYLE_DOTTED_EDGE);
			    currentNode = v1;

			    // ADD COMMUNICATION NODES
			    for (int j = 0; j < communications.size(); j++) {
				Object v5 = cat.insertNode(currentNode, "COM", STYLE_COM);
				cat.insertEdge(cat.getDefaultParent(), "", issueName, currentNode, v5);
				currentNode = v5;

				// System.out.println("targets: " +
				// communications.get(j).getTargets().size());
				if (communications.get(j).getTargets().size() > 1) {
				    // FIRST CREATE THE OR NODE
				    Object v2 = cat.insertNode(currentNode, "OR", STYLE_OR);
				    cat.insertEdge(cat.getDefaultParent(), "", "", v5, v2, STYLE_DOTTED_EDGE);
				    currentNode = v2;

				    // CREATE ONE NODE FOR EACH TARGET

				    for (int k = 0; k < communications.get(j).getTargets().size(); k++) {
					Target currentTarget = communications.get(j).getTargets().get(k);
					String name = currentTarget.getTargetRole().getRole().getType();

					String isName = currentTarget.getTargetRole().getRole().getSolver().get(0)
						.getName();

					Object v3 = cat.insertNode(currentNode, name, STYLE_ROLE);
					cat.insertEdge(cat.getDefaultParent(), "", "Solver: " + isName, currentNode, v3,
						STYLE_DOTTED_EDGE);
					currentNode = v3;

				    }
				} else {
				    // only one target
				    for (int k = 0; k < communications.get(j).getTargets().size(); k++) {
					Target currentTarget = communications.get(j).getTargets().get(k);
					String name = currentTarget.getTargetRole().getRole().getType();
					String isName = currentTarget.getTargetRole().getRole().getSolver().get(0)
						.getName();

					Object v3 = cat.insertNode(currentNode, name, STYLE_ROLE);
					cat.insertEdge(cat.getDefaultParent(), "", "Solver: " + isName, v5, v3,
						STYLE_DOTTED_EDGE);
					currentNode = v3;

				    }
				}

			    }
			} else {

			    // only one communication
			    IssueCommunication com = communications.get(0);
			    Object v5 = cat.insertNode(currentNode, "COM", STYLE_COM);
			    cat.insertEdge(cat.getDefaultParent(), "", com.getIssueToSolve().getIssueType(),
				    currentNode, v5, STYLE_ISSUE_EDGE);
			    // System.out.println("targets: " +
			    // communications.get(0).getTargets().size());
			    currentNode = v5;

			    if (communications.get(0).getTargets().size() > 1) {
				// FIRST CREATE THE OR NODE
				Object v2 = cat.insertNode(currentNode, "OR", STYLE_OR);
				cat.insertEdge(cat.getDefaultParent(), "", "", currentNode, v2);
				currentNode = v2;

				// CREATE ONE NODE FOR EACH TARGET

				for (int k = 0; k < communications.get(0).getTargets().size(); k++) {
				    Target currentTarget = communications.get(0).getTargets().get(k);
				    String name = currentTarget.getTargetRole().getRole().getType();
				    String isName = currentTarget.getTargetRole().getRole().getSolver().get(0)
					    .getName();

				    Object v3 = cat.insertNode(currentNode, name, STYLE_ROLE);
				    cat.insertEdge(cat.getDefaultParent(), "", "Solver: " + isName, currentNode, v3);

				    IssueCommunication currentCom = communications.get(0);

				    // prendo sempre il primo target

				    RoleManager targetManager = currentTarget.getTargetRole();

				    IssueResolution ir = new IssueResolution(1, "ISSUE_RECEIVED", rm, targetManager,
					    currentCom.getIssueToSolve(), null);

				    targetManager.getIssueResolutions().add(ir);
				    en.addIssueResolution(ir);

				}

				// ISSUE TARGETED
				// System.out.println(res.getRoleCurrent().getRole().getType()
				// + " *****ISSUE_TARGETED*****");
				res.setStatus("ISSUE_TARGETED");

				// END
				// System.out.println(res.getRoleCurrent().getRole().getType()
				// + " *****ISSUE_END*****");
				res.setStatus("End");

			    } else {
				// only one target

				Target currentTarget = communications.get(0).getTargets().get(0);
				String name = currentTarget.getTargetRole().getRole().getType();
				String isName = currentTarget.getTargetRole().getRole().getSolver().get(0).getName();

				Object v3 = cat.insertNode(currentNode, name, STYLE_ROLE);

				cat.insertEdge(cat.getDefaultParent(), "", "Solver: " + isName, currentNode, v3,
					STYLE_DOTTED_EDGE);
				currentNode = v3;

				// add a new issue resolution for the target
				// selected
				IssueCommunication currentCom = communications.get(0);

				RoleManager targetManager = currentTarget.getTargetRole();
				int capid = Integer.parseInt(capID);
				IssueResolution ir = new IssueResolution(capid, "ISSUE_RECEIVED", rm, targetManager,
					currentCom.getIssueToSolve(), null);

				targetManager.getIssueResolutions().add(ir);
				en.addIssueResolution(ir);

				// window.updateResolutions(capID, res, en);
				// window.updateResolutions(capID, ir, en,
				// targetManager);
				// ISSUE TARGETED
				// System.out.println(res.getRoleCurrent().getRole().getType()
				// + " *****ISSUE_TARGETED*****");
				res.setStatus("ISSUE_TARGETED");

				// END
				// System.out.println(res.getRoleCurrent().getRole().getType()
				// + " *****ISSUE_END*****");
				res.setStatus("End");

			    }
			}
		    }
		}

		break;

	    }
	}

    }

    /**
     * This method generates the tree to solve a specific issue in a specific
     * Role
     */

    private void generateSubTree(String capID, IssueResolution res, EnsembleManager en, CATree cat, RoleManager rm,
	    Issue is, String roleName, CAWindow window) {

	// creation of the tree root - the role triggers an issue internally
	Object currentNode = new CANode();
	Object root = cat.insertNode(cat.getDefaultParent(), "INIT", STYLE_INIT);
	currentNode = root;
	String issueName = res.getIssueInstance().getIssueType();
	// cat.setDefaultParent(root);

	String label = "ISSUE_TRIGGERED: " + issueName;

	Object v1 = cat.insertNode(currentNode, roleName, STYLE_ROLE);
	cat.insertEdge(cat.getDefaultParent(), "", label, currentNode, v1, STYLE_ISSUE_EDGE);
	currentNode = v1;

	// System.out.println(roleName + " *****ISSUE_TRIGGERED*****");
	// System.out.println("Issue to solve: " + issueName + " - for role: " +
	// roleName);

	// update of experiments variable dv3
	// rm.addraisedIssue();

	// we found the solvers inside the agent tha can solve the issue
	List<Solver> solvers = this.callLocalSolver(rm, is);

	// Find only one Local Solver - to solve a specifice issue
	if (solvers.size() == 1) {
	    this.localSolvers = solvers;
	    createSingleCom(capID, res, en, cat, currentNode, rm, roleName, window);
	    // window.updateActiveIssueResolutionTable(capID, res, en);

	} else if (solvers.size() > 1) {
	    // more the one solver for the same Issue
	    this.localSolvers = solvers;
	    createMultipleCom(capID, res, en, cat, currentNode, rm, roleName, window);

	} else {

	    res.setStatus("SOLUTION_FORWARDED");
	    // System.out.println(res.getRoleCurrent().getRole().getType() +
	    // " *****ISSUE_ENDED*****");
	    res.setStatus("End");

	    // System.out.println(roleName + " *****
	    // LOCAL_SOLUTION_NOT_FOUND*****");
	    // System.out.println(res.getRoleCurrent().getRole().getType() + "
	    // *****ISSUE_ENDED*****");

	}

    }

    private void createMultipleCom(String capID, IssueResolution res, EnsembleManager en, CATree cat,
	    Object currentNode, RoleManager rm, String roleName, CAWindow window) {

	res.setStatus("SOLVER_CALLED");
	// System.out.println(roleName + " *****LOCAL_SOLVER_CALLED*****");
	Object v3 = cat.insertNode(currentNode, "OR", STYLE_OR);
	cat.insertEdge(cat.getDefaultParent(), "", "", currentNode, v3, STYLE_DOTTED_EDGE);
	currentNode = v3;

	for (int i = 0; i < this.localSolvers.size(); i++) {

	    Solver current = this.localSolvers.get(i);
	    Solution currentSolution = current.getSolution().get(0);

	    // if no extra issue generated, add only a node
	    if (currentSolution.getIssue().size() == 0) {

		String label = "INTERNAL_SOLUTION";
		Object v1 = cat.insertNode(currentNode, roleName, STYLE_ROLE);
		cat.insertEdge(cat.getDefaultParent(), "", label, currentNode, v1, STYLE_ISSUE_EDGE);
		currentNode = v1;

		// here we forward the solution to the caller
		// System.out.println(res.getRoleCurrent().getRole().getType() +
		// " *****SOLUTION_FORWARDED*****");
		res.setStatus("SOLUTION_FORWARDED");
		// System.out.println(res.getRoleCurrent().getRole().getType() +
		// " *****ISSUE_ENDED*****");
		res.setStatus("End");

	    } else {
		// extra issue triggered
		targetWithIssues(res, en, cat, currentNode, rm, roleName);

	    }

	}

    }

    private void createSingleCom(String capID, IssueResolution res, EnsembleManager en, CATree cat, Object currentNode,
	    RoleManager rm, String roleName, CAWindow window) {
	// solvers exist
	// A Solver Exists
	res.setStatus("SOLVER_CALLED");
	// System.out.println(roleName + " *****LOCAL_SOLVER_CALLED*****");

	// [TODO: for the moment we select the first solution]
	createSolverTree(res, en, cat, currentNode, rm, roleName);

    }

    private void createSolverTree(IssueResolution res, EnsembleManager en, CATree cat, Object currentNode,
	    RoleManager rm, String roleName) {

	if (this.localSolvers.get(0).getSolution().get(0).getIssue().size() > 0) {
	    targetWithIssues(res, en, cat, currentNode, rm, roleName);

	} else {

	    // System.out.println("No Exta Issues founded");
	    // here we forward the solution to the caller
	    // System.out.println(res.getRoleCurrent().getRole().getType() + "
	    // *****SOLUTION_FORWARDED*****");
	    res.setStatus("SOLUTION_FORWARDED");
	    // System.out.println(res.getRoleCurrent().getRole().getType() + "
	    // *****ISSUE_ENDED*****");
	    res.setStatus("End");

	}
    }

    private void targetWithIssues(IssueResolution res, EnsembleManager en, CATree cat, Object currentNode,
	    RoleManager rm, String roleName) {
	res.setStatus("SOLUTION_FOUND");
	// System.out.println(roleName + " *****LOCAL_SOLUTION_FOUND*****");

	ArrayList<IssueCommunication> communications = new ArrayList<IssueCommunication>();
	communications = this.targetIssues(this.localSolvers.get(0).getSolution().get(0).getIssue(), en, rm);
	// System.out.println("Communications created for the Issue: " +
	// communications.size());
	res.setCommunications(communications);
	// here we creare and AND node for the tree with the
	// different communications
	// FIRST CREATE THE AND NODE

	String issues = "";
	for (int i = 0; i < this.localSolvers.get(0).getSolution().get(0).getIssue().size(); i++) {
	    String currentIssue = this.localSolvers.get(0).getSolution().get(0).getIssue().get(i).getIssueType();
	    if (i == this.localSolvers.get(0).getSolution().get(0).getIssue().size() - 1) {
		issues += currentIssue;
	    } else {
		issues += currentIssue + ", ";
	    }

	}

	String solverLabel = "Solver: " + this.localSolvers.get(0).getName() + "\n" + "ISSUE_TRIGGERED: {" + issues
		+ "}";

	if (this.localSolvers.get(0).getSolution().get(0).getIssue().size() > 1) {
	    Object v2 = cat.insertNode(currentNode, "AND", STYLE_AND);
	    cat.insertEdge(cat.getDefaultParent(), "", solverLabel, currentNode, v2, STYLE_DOTTED_EDGE);
	    currentNode = v2;
	}

	// ADD COMMUNICATION NODES
	for (int j = 0; j < communications.size(); j++) {
	    Object v5 = cat.insertNode(currentNode, "COM", STYLE_COM);
	    String issueToSolve = communications.get(j).getIssueToSolve().getIssueType();
	    cat.insertEdge(cat.getDefaultParent(), "", issueToSolve, currentNode, v5);
	    // currentNode = v5;
	    // System.out.println("targets: " +
	    // communications.get(j).getTargets().size());
	    if (communications.get(j).getTargets().size() > 1) {
		// FIRST CREATE THE OR NODE
		Object v3 = cat.insertNode(currentNode, "OR", STYLE_OR);
		cat.insertEdge(cat.getDefaultParent(), "", "", v5, v3, STYLE_DOTTED_EDGE);
		currentNode = v3;

		// CREATE ONE NODE FOR EACH TARGET

		for (int k = 0; k < communications.get(j).getTargets().size(); k++) {

		    Target currentTarget = communications.get(j).getTargets().get(k);
		    String name = currentTarget.getTargetRole().getRole().getType();
		    String isName = currentTarget.getTargetRole().getRole().getSolver().get(0).getName();
		    String Label = "Solver: " + isName;
		    Object v4 = cat.insertNode(currentNode, name, STYLE_ROLE);
		    cat.insertEdge(cat.getDefaultParent(), "", Label, v5, v4, STYLE_DOTTED_EDGE);
		    currentNode = v4;
		    // add an Issue Resolution to the global list
		    IssueCommunication currentCom = communications.get(j);

		    RoleManager targetManager = currentTarget.getTargetRole();

		    IssueResolution ir = new IssueResolution(1, "ISSUE_RECEIVED", rm, targetManager,
			    currentCom.getIssueToSolve(), null);

		    targetManager.getIssueResolutions().add(ir);
		    en.addIssueResolution(ir);
		    // pippo
		    // window.updateResolutions(capID, ir, en);

		}
	    } else {
		// only one target
		for (int k = 0; k < communications.get(j).getTargets().size(); k++) {
		    Target currentTarget = communications.get(j).getTargets().get(k);
		    String name = currentTarget.getTargetRole().getRole().getType();
		    String isName = currentTarget.getTargetRole().getRole().getSolver().get(0).getName();
		    String Label = "Solver: " + isName;
		    Object v3 = cat.insertNode(currentNode, name, STYLE_ROLE);
		    cat.insertEdge(cat.getDefaultParent(), "", Label, v5, v3, STYLE_DOTTED_EDGE);
		    currentNode = v3;

		    // add an Issue Resolution to the global list
		    IssueCommunication currentCom = communications.get(j);

		    RoleManager targetManager = currentTarget.getTargetRole();

		    IssueResolution ir = new IssueResolution(1, "ISSUE_RECEIVED", rm, targetManager,
			    currentCom.getIssueToSolve(), null);

		    targetManager.getIssueResolutions().add(ir);
		    en.addIssueResolution(ir);
		    // window.updateResolutions(capID, ir, en,
		    // targetManager);

		}
	    }

	}

	// System.out.println("Number of ACTIVE Issue Resolutions: " +
	// en.getActiveIssueResolutions().size());

	// window.updateActiveIssueResolutionTable(capID, res, en);
	// rm);

	// ISSUE TARGETED
	// System.out.println(res.getRoleCurrent().getRole().getType() + "
	// *****ISSUE_TARGETED*****");
	res.setStatus("ISSUE_TARGETED");

	// ISSUE ENDED
	// System.out.println(res.getRoleCurrent().getRole().getType() + "
	// *****ISSUE_ENDED*****");
	res.setStatus("End");
    }

    /*
     * Method to send Issue to different target roles through Issue
     * Communications it returns the set of communications created For each
     * Issue it returns a communication with targets
     */
    private ArrayList<IssueCommunication> targetIssues(List<Issue> issues, EnsembleManager e,
	    RoleManager originManager) {
	ArrayList<Target> targets = new ArrayList<Target>();
	ArrayList<IssueCommunication> communications = new ArrayList<IssueCommunication>();
	boolean result = false;
	for (int i = 0; i < issues.size(); i++) {
	    // here we create an Issue Communication with roles able to solve
	    // the specific issue

	    Issue currentIssue = issues.get(i);
	    // if
	    // (!(currentIssue.getIssueCondition().equalsIgnoreCase("trigger")))
	    // {
	    // System.out.println("Issue da Cercare nei Solver: " +
	    // currentIssue.getIssueType());
	    targets = this.find_targets(currentIssue, e, originManager);

	    if (targets.size() > 0) {
		UUID id = UUID.randomUUID();

		IssueCommunication com = new IssueCommunication(id, originManager, currentIssue, targets,
			CommunicationStatus.INIT);
		communications.add(com);

	    } else {
		// here we should find in other ensembles where the current
		// Manager is part of

		// UUID id = UUID.randomUUID();
		// IssueCommunication com = new IssueCommunication(id,
		// originManager, currentIssue, targets,
		// CommunicationStatus.INIT);
		// communications.add(com);

		// System.out.println("Target Non Trovato nella corrente
		// Ensemble per la Issue: " + currentIssue.getIssueType());

	    }
	    // }

	}
	return communications;

    }

    /* find target roles able to solve a specific issue */
    private ArrayList<Target> find_targets(Issue currentIssue, EnsembleManager em, RoleManager origin) {

	ArrayList<Target> targets = new ArrayList<Target>();
	// HashMap<Role, Solver> targets = new HashMap<Role, Solver>();

	// System.out.println("Numero di Roles Managers: " +
	// em.getRolesManagers().size());

	for (int i = 0; i < em.getRolesManagers().size(); i++) {
	    RoleManager currentRoleManager = em.getRolesManagers().get(i);
	    Role currentRole = currentRoleManager.getRole();
	    Role originRole = origin.getRole();
	    // if (originRole.getId() != currentRole.getId()) {
	    for (int j = 0; j < currentRole.getSolver().size(); j++) {
		Solver solver = currentRole.getSolver().get(j);
		if (solver.getIssue().getIssueType().equals(currentIssue.getIssueType())) {
		    // The Role is able to manage the current Issue
		    // System.out.println("ROLE ABLE TO SOLVE THE ISSUE: " +
		    // currentRole.getType());
		    Target target = new Target(currentRoleManager, solver, TargetStatus.INIT);
		    targets.add(target);
		}
	    }
	    // }

	}
	if (targets.size() == 0) {

	    // we should find targets outside the current ensemble
	    // for the moment I create fake target called External Ensemble
	    // [TODO: non risolvibile all'interno della stessa ensemble]
	    // origin.addcrossEnsembleIssues();
	    // System.out.println("EXTERNAL ENSEMBLE");

	}
	return targets;

    }

    /* Method to check if an issue can be solved locally to a role */

    public List<Solver> callLocalSolver(RoleManager roleManager, Issue issue) {

	List<Solver> solvers = new ArrayList<Solver>();
	// System.out.println("ISSUE: " + issue.getIssueType());
	// System.out.println("ROLE: " + roleManager.getRole().getType());
	// boolean result = false;

	// Find Local Solver and set in the analyzer
	for (int i = 0; i < roleManager.getRole().getSolver().size(); i++) {
	    Solver currentSolver = roleManager.getRole().getSolver().get(i);

	    if (currentSolver.getIssue().getIssueType().equalsIgnoreCase(issue.getIssueType())) {
		// solver provided for the current issue
		// this.localSolver = currentSolver;
		solvers.add(currentSolver);
		// //System.out.println(this.localSolver.getSolution().get(0).getName());
		// //System.out.println(this.localSolver.getSolution().get(0).getIssue().get(0).getIssueType());
		// result = true;
		// break;
	    }

	}
	// System.out.println("NUMERO DI LOCAL SOLVER per la Issue " +
	// issue.getIssueType() + ": " + solvers.size());
	return solvers;
    }

    public class Graph extends JPanel {
	public Graph() {
	    setSize(500, 500);
	}

	@Override
	public void paintComponent(Graphics g) {
	    Graphics2D gr = (Graphics2D) g; // This is if you want to use
					    // Graphics2D
	    // Now do the drawing here
	    ArrayList<Integer> scores = new ArrayList<Integer>(10);

	    Random r = new Random();

	    for (int j : scores) {
		j = r.nextInt(20);
		// System.out.println(r);
	    }

	    int y1;
	    int y2;

	    for (int i = 0; i < scores.size() - 1; i++) {
		y1 = (scores.get(i)) * 10;
		y2 = (scores.get(i + 1)) * 10;
		gr.drawLine(i * 10, y1, (i + 1) * 10, y2);
	    }
	}
    }

    /* max depth */
    public int maxDepth(CATree cat) {
	// build graph
	distance = 0;
	mxGraph graph = new mxGraph();

	mxAnalysisGraph aGraph = new mxAnalysisGraph();
	aGraph.setGraph(cat);

	// apply dfs to find depth of a tree
	mxTraversal.dfs(aGraph, cat.getFirstNode(), new mxICellVisitor() {

	    @Override
	    public boolean visit(Object vertex, Object edge) {
		mxCell v = (mxCell) vertex;
		mxCell e = (mxCell) edge;
		String eVal = "N/A";

		if (e != null) {
		    if (e.getValue() == null) {
			eVal = "1.0";
		    } else {
			eVal = e.getValue().toString();
		    }
		}

		if (!eVal.equals("N/A")) {
		    distance = distance + 1;
		}

		// System.out.print("(v: " + v.getValue() + " e: " + eVal +
		// ")");

		return false;
	    }
	});
	// System.out.println("MaxDepth= " + distance);
	return distance;

    }

    public int MinimumDepth(CATree cat) {

	// init analysis
	mxAnalysisGraph aGraph = new mxAnalysisGraph();
	aGraph.setGraph(cat);

	// apply bfs
	mxTraversal.bfs(aGraph, cat.getFirstNode(), new mxICellVisitor() {

	    @Override
	    public boolean visit(Object vertex, Object edge) {
		mxCell v = (mxCell) vertex;
		mxCell e = (mxCell) edge;

		if (e != null) {
		    depth++;
		    // System.out.println("Visit " + v.getValue());
		    if (hasChild(v) == 0) {
			if (!printed) {
			    printed = true;
			    // System.out.println("Minimum depth = " + depth);
			}
		    }

		}

		return false;
	    }
	});
	printed = false;
	//System.out.println("MIN DEPTH: " + depth);
	return depth;
    }

    public int MinExtent(CATree cat) {

	// init analysis
	mxAnalysisGraph aGraph = new mxAnalysisGraph();
	aGraph.setGraph(cat);

	// apply bfs
	mxTraversal.bfs(aGraph, cat.getFirstNode(), new mxICellVisitor() {

	    @Override
	    public boolean visit(Object vertex, Object edge) {
		mxCell v = (mxCell) vertex;
		mxCell e = (mxCell) edge;

		if (e != null) {
		    // System.out.println("Visit " + v.getValue());
		    int c = hasChild(v);
		    if (c != 0) {
			if (c < miniumExtent) {
			    miniumExtent = c;
			}
		    }

		}

		return false;
	    }
	});
	// System.out.println("minimum Extent : " + miniumExtent);
	return miniumExtent;

    }

    public int MaxExtent(CATree cat) {

	// init analysis
	mxAnalysisGraph aGraph = new mxAnalysisGraph();
	aGraph.setGraph(cat);

	// apply bfs
	mxTraversal.bfs(aGraph, cat.getFirstNode(), new mxICellVisitor() {

	    @Override
	    public boolean visit(Object vertex, Object edge) {
		mxCell v = (mxCell) vertex;
		mxCell e = (mxCell) edge;

		if (e != null) {
		    // System.out.println("Visit " + v.getValue());
		    int c = hasChild(v);

		    if (c != 0) {

			if (c > maximumExtent) {
			    maximumExtent = c;
			}
		    }

		}

		return false;
	    }
	});
	// System.out.println("maximum Extent : " + maximumExtent);
	return maximumExtent;

    }

    private static int hasChild(mxICell cell) {

	if (cell.getChildCount() == 0) {
	    return 0;
	}
	int childs = 0;
	for (int i = 0; i < cell.getChildCount(); i++) {
	    mxICell child = cell.getChildAt(i);
	    if (child != null && child.isVertex()) {
		childs++;
	    }
	}
	return childs;
    }

}
