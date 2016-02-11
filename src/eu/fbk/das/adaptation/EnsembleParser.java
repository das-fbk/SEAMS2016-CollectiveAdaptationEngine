package eu.fbk.das.adaptation;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.fbk.das.adaptation.ensemble.Ensemble;

/**
 * Parser utility class for {@link EnsembleType}
 * 
 */
public class EnsembleParser {

    private static final Logger logger = LogManager.getLogger(EnsembleParser.class);

    public EnsembleParser() {
    }

    /*
     * public ProcessDiagram convertToProcessDiagram( List<ActivityType>
     * activities, String processName) throws InvalidFlowInitialStateException,
     * InvalidFlowActivityException, FlowDuplicateActivityException {
     * ProcessDiagram pd = new ProcessDiagram();
     * pd.addAllActivity(parseActivities(activities)); // TODO: manca il setting
     * delle variabili, per ora non gestiste pd.setName(processName); return pd;
     * }
     */
    /*
     * public List<ProcessActivity> parseActivities(List<ActivityType>
     * activities) throws InvalidFlowInitialStateException,
     * InvalidFlowActivityException, FlowDuplicateActivityException {
     * List<ProcessActivity> result = new ArrayList<ProcessActivity>(); int a =
     * 0; for (ActivityType processActivity : activities) { Set<Integer> states
     * = new HashSet<Integer>(); Integer initialState = 0;
     * states.add(initialState);
     * 
     * if (isReceiveType(processActivity)) {
     * eu.fbk.das.process.engine.api.domain.ReplyActivity act = new
     * eu.fbk.das.process.engine.api.domain.ReplyActivity( a, a + 1,
     * processActivity.getName()); act.setReceive(true);
     * act.setEffect(processActivity.getEffect());
     * act.setPrecondition(processActivity.getPrecondition()); result.add(act);
     * } if (isInvokeType(processActivity)) {
     * eu.fbk.das.process.engine.api.domain.InvokeActivty act = new
     * eu.fbk.das.process.engine.api.domain.InvokeActivty( a, a + 1,
     * processActivity.getName()); act.setSend(true);
     * act.setEffect(processActivity.getEffect());
     * act.setPrecondition(processActivity.getPrecondition()); result.add(act);
     * } if (isSwitchType(processActivity)) { ProcessActivity act =
     * this.parseSwitchActivity(processActivity, a, states, 1);
     * act.setSwitch(true); result.add(act); } if
     * (isConcreteType(processActivity)) { ConcreteActivity act =
     * (ConcreteActivity) parseSimpleActivity( processActivity, a, states);
     * act.setPrecondition(processActivity.getPrecondition());
     * act.setEffect(processActivity.getEffect()); result.add(act); } if
     * (isPickType(processActivity)) { ProcessActivity act =
     * parsePickActivity(processActivity, a, states, 1); result.add(act); } if
     * (isAbstractType(processActivity)) { ProcessActivity act =
     * parseSimpleActivity(processActivity, a, states); AbstractActivity absact
     * = (AbstractActivity) act; absact.setEffect(processActivity.getEffect());
     * absact.setPrecondition(processActivity.getPrecondition());
     * result.add(act); } if (isWhileType(processActivity)) { ProcessActivity
     * act = parseWhileActivity(processActivity, a, states, 1);
     * act.setPrecondition(processActivity.getPrecondition());
     * act.setEffect(processActivity.getEffect()); result.add(act); } a++; }
     * return result; }
     */

    /*
     * public List<ProcessActivity> parseActivities(List<ActivityType>
     * activities, ScopeType scope) throws InvalidFlowInitialStateException,
     * InvalidFlowActivityException, FlowDuplicateActivityException {
     * List<ProcessActivity> acts = parseActivities(activities); for
     * (ProcessActivity pa : acts) { pa.setScope(scope); } return acts; }
     */

    public Ensemble parseEnsemble(File f) {
	Ensemble en = null;
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(Ensemble.class);
	    en = (Ensemble) jaxbContext.createUnmarshaller().unmarshal(f);
	} catch (JAXBException e) {
	    logger.error(e);
	}
	return en;
    }

}
