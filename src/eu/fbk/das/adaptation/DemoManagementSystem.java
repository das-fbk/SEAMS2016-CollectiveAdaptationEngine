package eu.fbk.das.adaptation;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import eu.fbk.das.adaptation.ensemble.Ensemble;

/**
 * This is the class that handles everything happening in the demo, including
 * instance control and storage and message exchange
 * 
 * @author Antonio
 * 
 */
public class DemoManagementSystem {

    /**
     * path to the repository of definitions
     */
    private static String REPO_PATH = null;
    // private CollectiveAdaptationManager cam;

    private static int idEnsembles = 0;
    private static int idRoles = 0;

    private static DemoManagementSystem ONLY_INSTANCE = null;

    /**
     * private constructor to prevent uncontrolled instantiation
     */
    private DemoManagementSystem() {
	// cam = new CollectiveAdaptationManager();

    }

    public static DemoManagementSystem initializeSystem(String repoPath) throws FileNotFoundException {
	if (ONLY_INSTANCE != null) {
	    throw new IllegalStateException("Only one instance of DemoManagementSystem can be initialized");
	}
	if (!Files.exists(Paths.get(repoPath))) {
	    throw new FileNotFoundException("Repository path is not a valid path");
	}
	DemoManagementSystem.REPO_PATH = repoPath;
	DemoManagementSystem dms = new DemoManagementSystem();

	return dms;
    }

    private ArrayList<Ensemble> ensembleInstances;

    public ArrayList<Ensemble> getEnsembleInstances() {

	if (ensembleInstances == null) {
	    ensembleInstances = new ArrayList<Ensemble>();
	    return ensembleInstances;
	} else {
	    return ensembleInstances;
	}
    }

    public void setEnsembleInstances(ArrayList<Ensemble> ensembleInstances) {
	this.ensembleInstances = ensembleInstances;
    }

    public Ensemble getEnsembleInstance(String type) {
	Ensemble en = null;

	if (ensembleInstances == null) {
	    ensembleInstances = new ArrayList<Ensemble>();
	    //System.out.println("Create a new Ensemble Instance of type: " + type + " with ID: " + idEnsembles);

	    en = this.getEnsemble(type);

	    idEnsembles++;
	    ensembleInstances.add(en);

	} else {
	    //System.out.println("Create a new Ensemble Instance of type: " + type + " with ID: " + idEnsembles);

	    en = this.getEnsemble(type);

	    idEnsembles++;
	    ensembleInstances.add(en);
	}

	return en;
    }

    public Ensemble getEnsemble(String type) {
	Ensemble ei = null;

	if (ensembleInstances == null) {
	    ensembleInstances = new ArrayList<Ensemble>();

	    if (type == null) {
		throw new NullPointerException("Ensemble Type is null");
	    }
	    File dir = new File(REPO_PATH);
	    if (!dir.isDirectory()) {
		throw new NullPointerException("Impossibile to load the ensemble type, mainDir not found " + dir);
	    }
	    File f = new File(REPO_PATH + "ensembles" + File.separator + type + ".xml");

	    // retrieve the type from file
	    EnsembleParser parser = new EnsembleParser();
	    ei = parser.parseEnsemble(f);
	    ensembleInstances.add(ei);

	} else {

	    File f = new File(REPO_PATH + "ensembles" + File.separator + type + ".xml");

	    EnsembleParser parser = new EnsembleParser();
	    ei = parser.parseEnsemble(f);

	    ensembleInstances.add(ei);
	}
	//System.out.println("Ensemble " + type + " created");

	return ei;
    }

    /*
     * public Role getRole(String type) { Role r = null;
     * 
     * if (roleInstances == null) { roleInstances = new ArrayList<Role>();
     * //System.out.println("Create anew!"); // building path String separator =
     * System.getProperty("file.separator"); String filePath = REPO_PATH +
     * separator + "types" + separator + "roles" + separator + type + ".xml";
     * ObjectMapper mapper = new ObjectMapper(); try { r = mapper.readValue(new
     * File(filePath), Role.class); } catch (IOException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } UUID id =
     * UUID.randomUUID(); // r.setId(id); roleInstances.add(r); } else {
     * //System.out.println("Create anew!"); // building path String separator =
     * System.getProperty("file.separator"); String filePath = REPO_PATH +
     * separator + "types" + separator + "roles" + separator + type + ".xml";
     * ObjectMapper mapper = new ObjectMapper(); try { r = mapper.readValue(new
     * File(filePath), Role.class); } catch (IOException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } UUID id =
     * UUID.randomUUID(); r.setId(id); roleInstances.add(r); }
     * 
     * return r; }
     */
}
