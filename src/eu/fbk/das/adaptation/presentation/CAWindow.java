package eu.fbk.das.adaptation.presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import eu.fbk.das.adaptation.EnsembleManager;
import eu.fbk.das.adaptation.ensemble.Ensemble;
import eu.fbk.das.adaptation.model.IssueResolution;
import eu.fbk.das.adaptation.presentation.action.IssueTableSelectionListener;
import eu.fbk.das.adaptation.presentation.action.MouseTreeNodeListener;

public class CAWindow extends JFrame {

    private static final Logger logger = LogManager.getLogger(CAWindow.class);
    private static final long serialVersionUID = -2707712944901661771L;

    public int counter = 0;

    // main frame
    public JFrame frame;

    public JPanel treePanel;

    // main window components

    private JPanel mainPanel;

    private Label label;

    // private JList<String> IssueResolutionsList;
    private JTable IssueResolutionsList;

    private JTable analyzerLog;
    private JTable monitorList;
    private JTable planningList;
    private JTable executeList;

    private mxGraphLayout layout;
    private JButton btnStep;

    private JTextArea logTextArea;

    private JScrollPane activeIssuesScrollPane;
    private JScrollPane CATreeScrollPane;
    private JScrollPane activeMonitorScrollPane;
    private JScrollPane analyzerLogScrollPane;
    private JScrollPane plannerLogScrollPane;
    private JScrollPane executeLogScrollPane;
    private mxGraphComponent graphComponent;

    public mxGraphComponent getGraphComponent() {
	return graphComponent;
    }

    public void setGraphComponent(mxGraphComponent graphComponent) {
	this.graphComponent = graphComponent;
    }

    private JScrollPane treeScrollPanel;

    public CAWindow() {

	super("Collective Adaptation Viewer");

	mainPanel = new JPanel();
	mainPanel.setVisible(true);
	mainPanel.setLayout(null);
	// mettere solo preferredSize per far comparire le barre di scorrimento
	// verticali
	mainPanel.setPreferredSize(new Dimension(1024, 1300));

	label = new Label("Select an active Issue Resolution to see its collective adaptation resolution tree");
	label.setBounds(5, 55, 713, 22);
	mainPanel.add(label);

	// Log Text ARea
	Label lblLog = new Label("Log");
	lblLog.setBounds(10, 550, 223, 22);
	mainPanel.add(lblLog);

	// log inside a scrollpane logTextArea = new JTextArea("");
	logTextArea = new JTextArea();
	logTextArea.setBounds(10, 575, 982, 71);
	logTextArea.setEditable(false);
	JScrollPane logScrollPane = new JScrollPane(logTextArea);
	logScrollPane.setBounds(10, 580, 982, 71);
	mainPanel.add(logScrollPane);

	getContentPane().add(mainPanel);

	// window to show issue tables and trees

	Vector<String> columnNames = new Vector<String>();
	columnNames.add("N");
	columnNames.add("CAP ID");
	columnNames.add("Role Type");
	columnNames.add("Role ID");
	columnNames.add("Issue Type");
	columnNames.add("Ensemble");

	IssueResolutionsList = new JTable(null, columnNames) {
	    @Override
	    public boolean isCellEditable(int r, int c) {
		return false; // Disallow the editing of any cell
	    }

	};
	IssueResolutionsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	IssueResolutionsList.setBounds(5, 125, 400, 400);
	IssueResolutionsList.setFillsViewportHeight(true);
	// IssueResolutionsList.getSelectionModel()
	// .addListSelectionListener(new
	// IssueTableSelectionListener(IssueResolutionsList, em, this));

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(5, 125, 400, 400);
	mainPanel.add(tabbedPane);

	activeIssuesScrollPane = new JScrollPane(IssueResolutionsList);
	tabbedPane.addTab("Active Issue Resolutions", null, activeIssuesScrollPane, null);
	tabbedPane.setEnabledAt(0, true);

	// Load Trees Frame
	CATree cat = new CATree();
	// frame to see the issue resolution tree mxGraphComponent
	graphComponent = new mxGraphComponent(cat);
	// layout = new mxParallelEdgeLayout(graphComponent.getGraph());

	// layout = new mxHierarchicalLayout(graphComponent.getGraph());
	// layout = new mxCompactTreeLayout(graphComponent.getGraph());

	graphComponent.getGraphControl().addMouseListener(new MouseTreeNodeListener(graphComponent));

	graphComponent.setBounds(520, 125, 700, 400);
	graphComponent.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));

	graphComponent.setEnabled(true);
	// layout.execute(graphComponent.getGraph().getDefaultParent());

	JTabbedPane tabbedPaneTree = new JTabbedPane(JTabbedPane.TOP);
	tabbedPaneTree.setBounds(520, 125, 700, 400);
	mainPanel.add(tabbedPaneTree);

	CATreeScrollPane = new JScrollPane(graphComponent);
	tabbedPaneTree.addTab("Issue Resolution Tree", null, CATreeScrollPane, null);
	tabbedPaneTree.setEnabledAt(0, true);

	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setSize(10, 10);
	// this.setBounds(0, 0, 400, 600);
	this.setVisible(true);

    }

    private Vector<String> convertAndFilterForJtable(String counter, String capID, IssueResolution ir,
	    EnsembleManager em) {
	// Vector<String> response = new Vector<String>();

	Vector<String> v = new Vector<String>();
	v.add(counter);
	v.add(capID);
	v.add(ir.getRoleCurrent().getRole().getType());
	v.add(ir.getRoleCurrent().getRole().getId());
	v.add(ir.getIssueInstance().getIssueType());
	v.add(em.getEnsemble().getName());

	return v;
    }

    private Vector<Vector<String>> convertPlannerForJtable(List<String> plans) {
	Vector<Vector<String>> response = new Vector<Vector<String>>();
	/*
	 * for (IssueResolution ir : issueResolutions) { Vector<String> v = new
	 * Vector<String>(); v.add(ir.getRoleCurrent().getType());
	 * v.add(ir.getIssueInstance().getType());
	 * 
	 * response.add(v);
	 * 
	 * }
	 */
	return response;
    }

    private Vector<Vector<String>> convertExecuteForJtable(List<String> executes) {
	Vector<Vector<String>> response = new Vector<Vector<String>>();
	/*
	 * for (IssueResolution ir : issueResolutions) { Vector<String> v = new
	 * Vector<String>(); v.add(ir.getRoleCurrent().getType());
	 * v.add(ir.getIssueInstance().getType());
	 * 
	 * response.add(v);
	 * 
	 * }
	 */
	return response;
    }

    private Vector<Vector<String>> convertAnalyzerForJtable(List<String> logs) {
	Vector<Vector<String>> response = new Vector<Vector<String>>();
	/*
	 * for (IssueResolution ir : issueResolutions) { Vector<String> v = new
	 * Vector<String>(); v.add(ir.getRoleCurrent().getType());
	 * v.add(ir.getIssueInstance().getType());
	 * 
	 * response.add(v);
	 * 
	 * }
	 */
	return response;
    }

    private Vector<Vector<String>> convertMonitorForJtable(List<String> monitors) {
	Vector<Vector<String>> response = new Vector<Vector<String>>();
	/*
	 * for (IssueResolution ir : issueResolutions) { Vector<String> v = new
	 * Vector<String>(); v.add(ir.getRoleCurrent().getType());
	 * v.add(ir.getIssueInstance().getType());
	 * 
	 * response.add(v);
	 * 
	 * }
	 */
	return response;
    }

    public void loadCATree(CATree cat) {

	if (cat == null) {
	    cat = new CATree();
	    // frame to see the issue resolution tree mxGraphComponent
	    graphComponent = new mxGraphComponent(cat);
	    // layout = new mxParallelEdgeLayout(graphComponent.getGraph());

	    layout = new mxHierarchicalLayout(graphComponent.getGraph());

	    // graphComponent.setBounds(280, 125, 400, 400);
	    graphComponent.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));

	    graphComponent.setEnabled(true);
	    layout.execute(graphComponent.getGraph().getDefaultParent());

	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    // tabbedPane.setBounds(280, 125, 400, 400);
	    mainPanel.add(tabbedPane);

	    CATreeScrollPane = new JScrollPane(graphComponent);
	    tabbedPane.addTab("Issue Resolution Tree", null, CATreeScrollPane, null);
	    tabbedPane.setEnabledAt(0, true);

	    refreshWindow();

	} else {

	    // frame to see the issue resolution tree mxGraphComponent
	    graphComponent = new mxGraphComponent(cat);

	    // layout = new mxParallelEdgeLayout(graphComponent.getGraph());

	    layout = new mxHierarchicalLayout(graphComponent.getGraph());

	    // graphComponent.setBounds(280, 125, 400, 400);
	    graphComponent.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));

	    graphComponent.setEnabled(true);
	    layout.execute(cat.getDefaultParent());

	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    // tabbedPane.setBounds(280, 125, 400, 400);
	    mainPanel.add(tabbedPane);

	    CATreeScrollPane = new JScrollPane(graphComponent);
	    tabbedPane.addTab("Issue Resolution Tree", null, CATreeScrollPane, null);
	    tabbedPane.setEnabledAt(0, true);

	    refreshWindow();
	}

    }

    /*
     * public void displayTree(CATree cat) { if (cat == null) { logger.warn(
     * "CA Tree be not null"); return; } // display process Model if (cat ==
     * null) { cat = new CATree(); this.treePanel.setLayout(new
     * GridBagLayout()); JScrollPane treeScrollPane = new
     * JScrollPane(treePanel); treeScrollPane.setBounds(247, 447, 586, 200);
     * treeScrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
     * 
     * mainPanel.add(treeScrollPane);
     * 
     * } this.clearTree(); this.updateTree(cat);
     * 
     * refreshWindow(); }
     */
    /**
     * Clear graph
     * 
     * @see {@link mxGraphModel#clear}
     */
    public void clearTree() {
	mxGraph current = graphComponent.getGraph();
	((mxGraphModel) current.getModel()).clear();

    }

    public void updateTree(CATree tree) {

	graphComponent.setGraph(tree);
	layout = new mxHierarchicalLayout(graphComponent.getGraph());
	layout.execute(graphComponent.getGraph().getDefaultParent());
	graphComponent.repaint();

    }

    public void loadTreeFrame() {

	CATree cat = new CATree();
	// frame to see the issue resolution tree mxGraphComponent
	graphComponent = new mxGraphComponent(cat);
	// layout = new mxParallelEdgeLayout(graphComponent.getGraph());

	// layout = new mxHierarchicalLayout(graphComponent.getGraph());

	graphComponent.getGraphControl().addMouseListener(new MouseTreeNodeListener(graphComponent));

	graphComponent.setBounds(420, 125, 400, 400);
	graphComponent.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));

	graphComponent.setEnabled(true);
	// layout.execute(graphComponent.getGraph().getDefaultParent());

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(420, 125, 400, 400);
	mainPanel.add(tabbedPane);

	CATreeScrollPane = new JScrollPane(graphComponent);
	tabbedPane.addTab("Issue Resolution Tree", null, CATreeScrollPane, null);
	tabbedPane.setEnabledAt(0, true);
    }

    public void loadAnalyzerFrame(List<String> logs, Ensemble e) {
	Vector<Vector<String>> data = convertAnalyzerForJtable(logs);
	// List of Active Issue Resolutions
	Vector<String> columnNames = new Vector<String>();
	columnNames.add("State");

	analyzerLog = new JTable(data, columnNames) {
	    @Override
	    public boolean isCellEditable(int r, int c) {
		return false; // Disallow the editing of any cell
	    }

	};
	analyzerLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	analyzerLog.setBounds(640, 310, 200, 130);
	analyzerLog.setFillsViewportHeight(true);

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(640, 310, 200, 130);
	mainPanel.add(tabbedPane);

	analyzerLogScrollPane = new JScrollPane(analyzerLog);
	tabbedPane.addTab("Analyzing", null, analyzerLogScrollPane, null);
	tabbedPane.setEnabledAt(0, true);

	refreshWindow();
    }

    public void loadMonitoringTable(List<String> activeMonitors, Ensemble e) {
	Vector<Vector<String>> data = convertMonitorForJtable(activeMonitors);
	// List of Active Issue Resolutions
	Vector<String> columnNames = new Vector<String>();
	columnNames.add("Monitor");
	columnNames.add("State");

	monitorList = new JTable(data, columnNames) {
	    @Override
	    public boolean isCellEditable(int r, int c) {
		return false; // Disallow the editing of any cell
	    }

	};
	monitorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	monitorList.setBounds(640, 125, 200, 130);
	monitorList.setFillsViewportHeight(true);

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(640, 125, 200, 130);
	mainPanel.add(tabbedPane);

	activeMonitorScrollPane = new JScrollPane(monitorList);
	tabbedPane.addTab("Monitoring", null, activeMonitorScrollPane, null);
	tabbedPane.setEnabledAt(0, true);

	refreshWindow();
    }

    public void updateResolutions(String capID, IssueResolution issue, EnsembleManager em) {

	this.counter = this.counter + 1;
	String counter = Integer.toString(this.counter);
	Vector<String> row = convertAndFilterForJtable(counter, capID, issue, em);
	// //System.out.println(row);
	// for (Ensemble ensemble : ensembles) {
	// Vector<String> v = new Vector<String>();
	// v.add(ensemble.getName());
	// data.add(v);
	// }

	Vector<String> columnNames = new Vector<String>();
	columnNames.add("N");
	columnNames.add("CAP ID");
	columnNames.add("Role Type");
	columnNames.add("Role ID");
	columnNames.add("Issue Type");
	columnNames.add("Ensemble");

	((DefaultTableModel) IssueResolutionsList.getModel()).addRow(row);
	IssueResolutionsList.getSelectionModel()
		.addListSelectionListener(new IssueTableSelectionListener(IssueResolutionsList, em, this));
	// IssueResolutionsList.setModel(new DefaultTableModel(data,
	// columnNames));

    }

    /*
     * public void loadActiveIssueResolutionsTable(List<IssueResolution>
     * activeIssueResolutions, EnsembleManager em) { Vector<Vector<String>> data
     * = convertAndFilterForJtable(activeIssueResolutions, em); // List of
     * Active Issue Resolutions Vector<String> columnNames = new
     * Vector<String>(); columnNames.add("Role Type"); columnNames.add("Role ID"
     * ); columnNames.add("Issue Type"); columnNames.add("Ensemble");
     * 
     * IssueResolutionsList = new JTable(data, columnNames) {
     * 
     * @Override public boolean isCellEditable(int r, int c) { return false; //
     * Disallow the editing of any cell }
     * 
     * }; IssueResolutionsList.setSelectionMode(ListSelectionModel.
     * SINGLE_INTERVAL_SELECTION); IssueResolutionsList.setBounds(5, 125, 400,
     * 400); IssueResolutionsList.setFillsViewportHeight(true);
     * IssueResolutionsList.getSelectionModel() .addListSelectionListener(new
     * IssueTableSelectionListener(IssueResolutionsList, em, this));
     * 
     * JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
     * tabbedPane.setBounds(5, 125, 400, 400); mainPanel.add(tabbedPane);
     * 
     * activeIssuesScrollPane = new JScrollPane(IssueResolutionsList);
     * tabbedPane.addTab("Active Issue Resolutions", null,
     * activeIssuesScrollPane, null); tabbedPane.setEnabledAt(0, true);
     * 
     * refreshWindow(); }
     */
    public void loadPlannerFrame(List<String> plans, Ensemble e) {
	Vector<Vector<String>> data = convertPlannerForJtable(plans);
	// List of Active Issue Resolutions
	Vector<String> columnNames = new Vector<String>();
	columnNames.add("State");

	planningList = new JTable(data, columnNames) {
	    @Override
	    public boolean isCellEditable(int r, int c) {
		return false; // Disallow the editing of any cell
	    }

	};
	planningList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	planningList.setBounds(850, 310, 200, 130);
	planningList.setFillsViewportHeight(true);

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(850, 310, 200, 130);
	mainPanel.add(tabbedPane);

	plannerLogScrollPane = new JScrollPane(planningList);
	tabbedPane.addTab("Planning", null, plannerLogScrollPane, null);
	tabbedPane.setEnabledAt(0, true);

	refreshWindow();
    }

    public void loadExecuteFrame(List<String> exec, Ensemble e) {
	Vector<Vector<String>> data = convertExecuteForJtable(exec);
	// List of Active Issue Resolutions
	Vector<String> columnNames = new Vector<String>();
	columnNames.add("State");

	executeList = new JTable(data, columnNames) {
	    @Override
	    public boolean isCellEditable(int r, int c) {
		return false; // Disallow the editing of any cell
	    }

	};
	executeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	executeList.setBounds(850, 125, 200, 130);
	executeList.setFillsViewportHeight(true);

	JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	tabbedPane.setBounds(850, 125, 200, 130);
	mainPanel.add(tabbedPane);

	executeLogScrollPane = new JScrollPane(executeList);
	tabbedPane.addTab("Executing", null, executeLogScrollPane, null);
	tabbedPane.setEnabledAt(0, true);

	refreshWindow();
    }

    public void updateActiveIssueResolutionTable(String capID, IssueResolution ir, EnsembleManager em) {

	String counter = Integer.toString(this.counter);
	Vector<String> row = convertAndFilterForJtable(counter, capID, ir, em);

	Vector<String> columnNames = new Vector<String>();
	columnNames.add("N");
	columnNames.add("Role Type");
	columnNames.add("Role ID");
	columnNames.add("Issue Type");
	columnNames.add("Ensemble");
	// IssueResolutionsList.setModel(new DefaultTableModel(row,
	// columnNames));

	refreshWindow();
    }

    public String getSelectedIssueInTable() {
	int sr = IssueResolutionsList.getSelectedRow();
	if (sr == -1 || sr >= IssueResolutionsList.getModel().getRowCount()) {
	    return "";
	}
	return (String) IssueResolutionsList.getModel().getValueAt(sr, 0);
    }

    public void resetIssueInstances() {
	IssueResolutionsList.setModel(null);

    }

    public void showTree(CATree cat) {

	// frame to see the issue resolution tree mxGraphComponent

	// cat.insertVertex(cat.getDefaultParent(), null, "ee", 0, 0, 80, 30,
	// null);
	// frame to see the issue resolution tree mxGraphComponent

	graphComponent.setGraph(cat);

	// layout = new mxParallelEdgeLayout(graphComponent.getGraph());

	// layout = new mxHierarchicalLayout(graphComponent.getGraph());

	graphComponent.setBounds(280, 125, 250, 400);
	graphComponent.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));

	graphComponent.setEnabled(true);
	layout.execute(graphComponent.getGraph().getDefaultParent());

    }

    public void refreshWindow() {
	mainPanel.validate();
	mainPanel.repaint();
    }

    public void resetTreeFrame() {
	if (graphComponent != null) {
	    graphComponent.removeAll();
	}
	refreshWindow();

    }

}