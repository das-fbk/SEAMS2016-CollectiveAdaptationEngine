package eu.fbk.das.adaptation.model;

import java.util.ArrayList;
import java.util.UUID;

import eu.fbk.das.adaptation.RoleManager;
import eu.fbk.das.adaptation.ensemble.Issue;

/**
 * Issue communication
 * 
 * @author heorhi
 * 
 */
public class IssueCommunication {
    private UUID id;
    /**
     * role initiating the communication
     */
    private RoleManager originManager;

    /**
     * set of communication targets
     */

    /**
     * ISsue communicated
     */

    private Issue issueToSolve;

    public Issue getIssueToSolve() {
	return issueToSolve;
    }

    public void setIssueToSolve(Issue issueToSolve) {
	this.issueToSolve = issueToSolve;
    }

    private ArrayList<Target> targets;

    public ArrayList<Target> getTargets() {
	return targets;
    }

    public void setTargets(ArrayList<Target> targets) {
	this.targets = targets;
    }

    private CommunicationStatus status;

    public IssueCommunication(UUID id, RoleManager originManager, Issue issueToSolve, ArrayList<Target> targets,
	    CommunicationStatus status) {
	super();
	this.id = id;
	this.originManager = originManager;
	this.issueToSolve = issueToSolve;
	this.targets = targets;
	this.status = status;
    }

    public static enum CommunicationStatus {
	INIT
    }

}
