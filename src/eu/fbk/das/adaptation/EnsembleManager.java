package eu.fbk.das.adaptation;

import java.util.ArrayList;
import java.util.HashMap;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxTraversal;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraph.mxICellVisitor;

import eu.fbk.das.adaptation.ensemble.Analyzer;
import eu.fbk.das.adaptation.ensemble.Ensemble;
import eu.fbk.das.adaptation.ensemble.Role;
import eu.fbk.das.adaptation.model.IssueResolution;
import eu.fbk.das.adaptation.presentation.CATree;
import eu.fbk.das.adaptation.presentation.CAWindow;

public class EnsembleManager {
    private static int distance = 0;
    private Ensemble ensemble;

    private ArrayList<IssueResolution> activeIssueResolutions;

    private ArrayList<RoleManager> rolesManagers;

    private int abortedResolutions;

    public int getAbortedResolutions() {
	return abortedResolutions;
    }

    public void setAbortedResolutions(int abortedResolutions) {
	this.abortedResolutions = abortedResolutions;
    }

    public ArrayList<RoleManager> getRolesManagers() {
	return rolesManagers;
    }

    public void setRolesManagers(ArrayList<RoleManager> rolesManagers) {
	this.rolesManagers = rolesManagers;
    }

    public ArrayList<IssueResolution> getActiveIssueResolutions() {
	return activeIssueResolutions;
    }

    public Ensemble getEnsemble() {
	return ensemble;
    }

    public void setEnsemble(Ensemble ensemble) {
	this.ensemble = ensemble;
    }

    public void setActiveIssueResolutions(ArrayList<IssueResolution> activeIssueResolutions) {
	this.activeIssueResolutions = activeIssueResolutions;

	// associate each issue at the related RoleManager
	for (int i = 0; i < this.activeIssueResolutions.size(); i++) {
	    IssueResolution currentResolution = this.activeIssueResolutions.get(i);
	    RoleManager rm = currentResolution.getRoleCurrent();
	    rm.addIssueResolution(currentResolution);
	}
    }

    public EnsembleManager(Ensemble ensemble) {
	super();
	this.ensemble = ensemble;

	// create all the route managers
	for (int i = 0; i < ensemble.getRole().size(); i++) {
	    Role role = ensemble.getRole().get(i);
	    RoleManager manager = new RoleManager(role);
	    this.addRoleManager(manager);
	}

    }

    private HashMap<String, CATree> trees = new HashMap<String, CATree>();

    // private HashMultimap<Pair<String, RoleManager>, CATree> trees = new
    // HashMap<RoleManager, CATree>();

    public HashMap<String, CATree> getTrees() {
	return trees;
    }

    public void setTrees(HashMap<String, CATree> trees) {
	if (trees == null) {
	    // System.out.println("trees must be not null");
	    return;
	}

	this.trees = trees;
    }

    public RoleManager getRolebyType(String type) {
	RoleManager role = null;
	for (int i = 0; i < this.getEnsemble().getRole().size(); i++) {
	    RoleManager current = this.getRolesManagers().get(i);
	    if (current.getRole().getType().equalsIgnoreCase(type)) {
		role = current;
		break;
	    }
	}
	return role;

    }

    public void checkRoles(String capID, CAWindow window) {

	// id of the collective adaptation problem
	// here we assume that only one CAP is active in the ensemble
	// UUID capID = UUID.randomUUID();
	// System.out.println("ACTIVE ISSUE RESOLUTIONS IN THE ENSEMBLE " +
	// this.getEnsemble().getName() + ": "+
	// this.getActiveIssueResolutions().size());
	if (this.trees == null) {
	    this.trees = new HashMap<String, CATree>();
	    // new HashMap<RoleManager, CATree>();
	}

	while (this.getActiveIssueResolutions().size() > 0) {

	    for (int i = 0; i < this.getRolesManagers().size(); i++) {
		RoleManager currentRoleManager = this.getRolesManagers().get(i);
		CATree currentTree;

		if (this.getTrees().get(currentRoleManager) == null) {
		    // //System.out.println("creo l'albero per il ruolo: " +
		    // currentRoleManager.getRole().getType());
		    currentTree = new CATree();
		} else {
		    currentTree = this.getTrees().get(currentRoleManager);

		}

		// //System.out.println("TREES CREATED: " +
		// this.getTrees().size());

		if (currentRoleManager.getIssueResolutions().size() != 0) {
		    ArrayList<IssueResolution> resolutions = currentRoleManager.getIssueResolutions();

		    // an issue exists and must be managed by the respective
		    // Analyzer
		    if (resolutions.size() > 0) {
			// for now each role can have only one active issue
			// resolution in a CAP
			IssueResolution res = resolutions.get(0);

			Analyzer an = currentRoleManager.getAnalyzer();
			if (res.getStatus() != "End") {

			    an.resolveIssue(capID, res, this, window, currentTree);

			    // //System.out.println(pair.toString());
			    // //System.out.println(res.getRoleCurrent().getRole().getType());

			} else {
			    // issue resolved and removed
			    // add tree to the list
			    // System.out.println("Issue Risolta");

			    resolutions.remove(res);
			    this.getActiveIssueResolutions().remove(res);
			}

		    }

		} else {

		    // //System.out.println("No Active Issue Resolution for
		    // role:
		    // "
		    // + currentRole.getType());
		}

	    }
	}

    }

    public void addRoleManager(RoleManager manager) {

	if (this.rolesManagers == null) {
	    this.rolesManagers = new ArrayList<RoleManager>();
	    this.rolesManagers.add(manager);
	} else {
	    this.rolesManagers.add(manager);
	}

    }

    public void addIssueResolution(IssueResolution ir) {
	if (this.activeIssueResolutions == null) {
	    this.activeIssueResolutions = new ArrayList<IssueResolution>();
	    this.activeIssueResolutions.add(ir);
	} else {
	    this.activeIssueResolutions.add(ir);
	}
    }

    /* max depth */
    public int grapDepth(CATree cat) {
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
	return distance;

    }

}
