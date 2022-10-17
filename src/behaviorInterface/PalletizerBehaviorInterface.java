package behaviorInterface;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import behaviorInterface.mosInterface.MosInterface;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class PalletizerBehaviorInterface extends BehaviorInterface {
	private DataSource ds;
	private MosInterface mi;

	private final String brokerURL;
	private final String mosURL;
	private final String palletizerID;
	
	public PalletizerBehaviorInterface(String brokerURL, String mosURL, String palletizerID) {
		this.brokerURL = brokerURL;
		this.mosURL = mosURL;
		this.palletizerID = palletizerID;
	}
	
	@Override
	public void onStart() {
		try {
			String dataSourceURI = "ds://www.arbi.com/BehaviorInterface";
			ds = new DataSource();
			ds.connect(brokerURL, dataSourceURI, BrokerType.ZEROMQ);

			mi = new MosInterface(RobotID.valueOf(this.palletizerID), this);
			String[] mosComponents = mosURL.split(":");
			String mosURL = "";
			for(int i = 0; i < mosComponents.length - 1; i++) {
				mosURL += mosComponents[i] + ":";
			}
			mosURL = mosURL.substring(0, mosURL.length() - 1);
			int mosPort = Integer.parseInt(mosComponents[mosComponents.length - 1]);
			this.initDS();
			mi.connect(mosURL, mosPort);
			System.out.println("login...");
			System.out.println(mi.login(RobotID.valueOf(this.palletizerID)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void initDS() {
//		try {
//			ds.assertFact("(robotLoading \"" + robotID + "\" \"loading\")");
//			Thread.sleep(50);
//			ds.assertFact("(robotStatus \"" + robotID + "\" \"Ready\")");
//			Thread.sleep(50);
//			ds.assertFact("(robotSpeed \"" + robotID + "\" 0)");
//			Thread.sleep(50);
//			ds.assertFact("(batteryRemain \"" + robotID + "\" 0)");
//			Thread.sleep(50);
//			ds.assertFact("(robotDegree \"" + robotID + "\" 0)");
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@Override
	public String onRequest(String sender, String request) {
		System.out.println("[request]\t: " + request.toString());
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(request);
			ActionType actionType = ActionType.valueOf(gl.getName());
			String actionID = gl.getExpression(0).asValue().stringValue();
			String robotID = null;
			int nodeID = 0;
			String response = null;
			switch(actionType) {
			case PalletizerStart:
				response = this.mi.palletizerStart(sender, actionID);
				break;
			case PalletizerStop:
				response = this.mi.palletizerStop(sender, actionID);
				break;
			case EnterPalletizer:
				robotID = gl.getExpression(1).asValue().stringValue();
				nodeID = gl.getExpression(2).asValue().intValue();
				response = this.mi.enterPalletizer(sender, actionID, RobotID.valueOf(robotID), nodeID);
				break;
			case ExitPalletizer:
				robotID = gl.getExpression(1).asValue().stringValue();
				nodeID = gl.getExpression(2).asValue().intValue();
				response = this.mi.exitPalletizer(sender, actionID, RobotID.valueOf(robotID), nodeID);
				break;
			default:
				response = "(fail)";
				break;
			}
			System.out.println("[response]\t: " + response);
			System.out.println();
			return response;
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "(fail)";
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "(fail)";
		}
	}
	
	public void onRTSR(String robotID, String status, float x, float y, int theta, int speed, int battery, String loading) {
		System.out.println("what RTSR?");
		return;
	}
	
	@Override
	public void palletizerPackingFinish(String palletizerID, int nodeID) {
		System.out.println("palletizerPackingFinish " + palletizerID + " " + nodeID);
		this.send("agent://www.arbi.com/TaskManager", "(palletizerPackingFinish \"" + palletizerID + " " + nodeID + ")");
	}
	
	public void sendPersonCall(int locationID, int cmdID) {
		System.out.println("what person call?");
		return;
	}	
	
	public static void main(String[] args) {
		String palletizerID = null;
		String brokerURL = null;
		String mosURL = null;
//		String brokerURL = "tcp://" + System.getenv("JMS_BROKER");
//		String mosURL = System.getenv("MOS");
//		String brokerName = System.getenv("AGENT");
//		String robotID = System.getenv("ROBOT");
////		brokerURL = "tcp://127.0.0.1:61116";
//		if(mosURL == null) {
//			mosURL = "127.0.0.1:30001";
//		if(robotID == null) {
//			robotID = "AMR_LIFT1";
//		}
		if(args == null || args.length == 0) {
			palletizerID = "Palletizer1";
			brokerURL = "tcp://127.0.0.1:61116";
			mosURL = "127.0.0.1:30001";
		}
		else {
			palletizerID = args[0];
			brokerURL = args[1];
			mosURL = args[2];
		}
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";

		BehaviorInterface bi = new PalletizerBehaviorInterface(brokerURL, mosURL, palletizerID);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, BrokerType.ZEROMQ);
		
		try {
//			Thread.sleep(5000);
//			bi.onRequest("", "(PalletizerStart \"asdf\")");
//			Thread.sleep(15000);
//			bi.onRequest("", "(EnterPalletizer \"asdf\" \"AMR_LIFT1\" 1)");
//			Thread.sleep(5000);
//			bi.onRequest("", "(ExitPalletizer \"asdf\" \"AMR_LIFT1\" 1)");
			Thread.sleep(5000);
			bi.onRequest("", "(PalletizerStop \"asdf\")");
			Thread.sleep(5000);
			bi.onRequest("", "(PalletizerStart \"asdf\")");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
