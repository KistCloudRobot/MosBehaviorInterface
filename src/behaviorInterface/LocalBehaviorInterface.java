package behaviorInterface;

import java.io.IOException;

import behaviorInterface.mosInterface.MosInterface;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class LocalBehaviorInterface extends BehaviorInterface {
	private DataSource ds;
	private MosInterface mi;

	private String brokerURL;
	private String mosURL;
	
	public LocalBehaviorInterface(String brokerURL, String mosURL) {
		this.brokerURL = brokerURL;
		this.mosURL = mosURL;
	}
	
	@Override
	public void onStart() {
		try {
			String dataSourceURI = "ds://www.arbi.com/BehaviorInterface";
			ds = new DataSource();
			ds.connect(brokerURL, dataSourceURI, BrokerType.ZEROMQ);

			mi = new MosInterface(this);
			String[] mosComponents = mosURL.split(":");
			String url = "";
			for(int i = 0; i < mosComponents.length - 1; i++) {
				url += mosComponents[i] + ":";
			}
			url = url.substring(0, url.length() - 1);
			int port = Integer.parseInt(mosComponents[mosComponents.length - 1]);
			this.initDS();
			mi.connect(url, port);
			System.out.println("login...");
			System.out.println(mi.login(RobotID.LOCAL));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initDS() {
		for(RobotID robotID : RobotID.getValues()) {
			try {
				ds.assertFact("(CurrentRobotInfo \"" + robotID + "\" 0 0 \"Unload\")");
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String onRequest(String sender, String request) {
		System.out.println("[request]\t: " + request.toString());
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(request);
			ActionType actionType = ActionType.valueOf(gl.getName());
			String actionID = gl.getExpression(0).asValue().stringValue();
			String response = null;
			String doorID = null;
			switch(actionType) {
			case DoorOpen:
				doorID = gl.getExpression(1).asValue().stringValue();
				response = this.mi.doorOpen(sender, actionID, doorID);
				break;
			case DoorClose:
				doorID = gl.getExpression(1).asValue().stringValue();
				response = this.mi.doorClose(sender, actionID, doorID);
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
	
	@Override
	public void onRTSR(String robotID, String status, float x, float y, int theta, int speed, int battery, String loading) {
		sendRobotInfo(robotID, x, y, loading);
		sendCurrentRobotInfo(robotID, x, y, loading);
	}
	
	@Override
	public void palletizerPackingFinish(String palletizerID, int nodeID) {
		System.out.println("what? palletizer packing finish");
	}
	
	private void sendRobotInfo(String robotID, float x, float y, String loading) {
		String gl = "(RobotInfo \"" + robotID + "\" " + x + " " + y + " \"" + loading + "\" \"" + System.currentTimeMillis() + "\")";
//		System.out.println(gl);
		ds.assertFact(gl);
	}

	private void sendCurrentRobotInfo(String robotID, float x, float y, String loading) {
		String before = "(CurrentRobotInfo \"" + robotID + "\" $x $y $loading)";
		String after = "(CurrentRobotInfo \"" + robotID + "\" " + x + " " + y + " \"" + loading + "\")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
		
	}
	
	@Override
	public void sendPersonCall(int locationID, int callID) throws Exception {
		String gl = "(MosPersonCall " + locationID + " " + callID + ")";
		ds.assertFact(gl);
	}
	
	public static void main(String[] args) {
		String brokerURL = null;
		String mosURL = null;
//		String brokerURL = "tcp://" + System.getenv("JMS_BROKER");
//		String mosURL = System.getenv("MOS");
//		brokerURL = "tcp://127.0.0.1:61316"
		if(args == null) {
			brokerURL = "tcp://127.0.0.1:61116";
			mosURL = "127.0.0.1:30001";
		}
		else {
			brokerURL = args[0];
			mosURL = args[1];
		}
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";
		
		BehaviorInterface bi = new LocalBehaviorInterface(brokerURL, mosURL);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, BrokerType.ZEROMQ);
	}
}
