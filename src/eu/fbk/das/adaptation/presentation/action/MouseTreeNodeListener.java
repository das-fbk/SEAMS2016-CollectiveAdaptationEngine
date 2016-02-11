package eu.fbk.das.adaptation.presentation.action;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;

// Handler mouse click on activity
public class MouseTreeNodeListener extends MouseAdapter {

    private static final Logger logger = LogManager.getLogger(MouseTreeNodeListener.class);

    private mxGraphComponent graphComponent;

    public MouseTreeNodeListener(mxGraphComponent graphComponent) {
	this.graphComponent = graphComponent;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	Object cell = graphComponent.getCellAt(e.getX(), e.getY());

	if (cell != null) {
	    if (cell instanceof mxCell) {
		if (((mxCell) cell).isVertex()) {
		    String label = graphComponent.getGraph().getLabel(cell);
		    logger.debug("Clicked cell=" + label);
		    //System.out.println(label);

		    // String user = window.getController().getCurrentUser();
		    /*
		     * switch (label) { case "UMS_UtilityRanking": if
		     * (window.getController() != null) {
		     * window.getUtilityView().setData(
		     * window.getController().getUserData(user));
		     * window.showUtilityFrame(true); } break; case
		     * "UMS_SecurityAndPrivacyFiltering": if
		     * (window.getController() != null) {
		     * window.getPSView().setData(
		     * window.getController().getUserData(user));
		     * 
		     * window.showPSFrame(true); } break; default:
		     * window.getController().post( new
		     * SelectedAbstractActivityEvent(label)); //
		     * JOptionPane.showMessageDialog(graphComponent, "cell=" //
		     * + graphComponent.getGraph().getLabel(cell)); break; }
		     */
		}
	    }
	}
    }
}
