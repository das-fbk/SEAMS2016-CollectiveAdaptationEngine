package eu.fbk.das.adaptation;

import java.util.ArrayList;

import eu.fbk.das.adaptation.ensemble.Analyzer;
import eu.fbk.das.adaptation.ensemble.Role;
import eu.fbk.das.adaptation.model.IssueResolution;

public class RoleManager {

    Role role;

    int minDepth;

    public int getMinDepth() {
	return minDepth;
    }

    public void setMinDepth(int minDepth) {
	this.minDepth = minDepth;
    }

    public int getMaxDepth() {
	return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
	this.maxDepth = maxDepth;
    }

    public int getMinExtent() {
	return minExtent;
    }

    public void setMinExtent(int minExtent) {
	this.minExtent = minExtent;
    }

    public int getMaxExtent() {
	return maxExtent;
    }

    public void setMaxExtent(int maxExtent) {
	this.maxExtent = maxExtent;
    }

    int maxDepth;
    int minExtent;
    int maxExtent;
    int crossEnsembleIssues;

    // int agentInvolvedinResolution;

    public int getCrossEnsembleIssues() {
	return crossEnsembleIssues;
    }

    public void setCrossEnsembleIssues(int crossEnsembleIssues) {
	this.crossEnsembleIssues = crossEnsembleIssues;
    }

    public Role getRole() {
	return role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    ArrayList<IssueResolution> issueResolutions;

    public ArrayList<IssueResolution> getIssueResolutions() {
	if (issueResolutions == null) {
	    issueResolutions = new ArrayList<IssueResolution>();
	    return issueResolutions;
	} else {
	    return issueResolutions;
	}
    }

    public void setResolutions(ArrayList<IssueResolution> resolutions) {
	this.issueResolutions = resolutions;
    }

    private Analyzer analyzer;

    public Analyzer getAnalyzer() {
	if (analyzer == null) {
	    analyzer = new Analyzer();
	    return analyzer;
	} else {
	    return analyzer;
	}
    }

    public void setAnalyzer(Analyzer analyzer) {
	this.analyzer = analyzer;
    }

    public RoleManager(Role role) {
	super();
	this.role = role;
	this.crossEnsembleIssues = 0;
    }

    public void addIssueResolution(IssueResolution resolution) {
	if (this.issueResolutions == null) {
	    this.issueResolutions = new ArrayList<IssueResolution>();
	    this.issueResolutions.add(resolution);
	} else {
	    this.issueResolutions.add(resolution);
	}

    }

    public void addcrossEnsembleIssues() {
	// TODO Auto-generated method stub
	// System.out.println("esterne ensemble: " + this.crossEnsembleIssues);
	this.crossEnsembleIssues++;
	// System.out.println("esterne ensemble: " + this.crossEnsembleIssues);

    }

}
