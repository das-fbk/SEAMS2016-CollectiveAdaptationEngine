package eu.fbk.das.adaptation.presentation;

import com.mxgraph.view.mxGraph;

public class CATree extends mxGraph {

    private Object firstNode;

    public Object getFirstNode() {
	return firstNode;
    }

    public void setFirstNode(Object firstNode) {
	this.firstNode = firstNode;
    }

    public Object insertNode(Object parent, String roleName, String style) {

	if (firstNode == null) {
	    firstNode = insertVertex(parent, null, roleName, 0, 0, 100, 30, style);
	    return firstNode;
	} else {
	    return insertVertex(parent, null, roleName, 0, 0, 100, 30, style);

	}
	// return insertVertex(parent, null, roleName, 0, 0, 100, 30, style);
    }

}
