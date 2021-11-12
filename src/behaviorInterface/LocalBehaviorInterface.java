package behaviorInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import behaviorInterface.mosInterface.MosInterface;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.LoginID;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class LocalBehaviorInterface extends BehaviorInterface {
	private DataSource ds;
	private MosInterface mi;

	private String brokerURL;
	private String mcArbiID;
	private String mosURL;

	private String taskManagerURI;
	
	public LocalBehaviorInterface(String brokerURL, String mcArbiID, String mosURL) {
		this.brokerURL = brokerURL;
		this.mcArbiID = mcArbiID;
		this.mosURL = mosURL;
	}
	
	@Override
	public void onStart() {
		try {
			taskManagerURI = "agent://www.arbi.com/Local/TaskManager";
			String dataSourceURI = "ds://www.arbi.com/" + this.mcArbiID + "/BehaviorInterface";
			ds = new DataSource();
			ds.connect(brokerURL, dataSourceURI, 2);

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
			System.out.println(mi.login(LoginID.valueOf(this.mcArbiID)));
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
		System.out.println("on request : " + request.toString());
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(request);
			ActionType actionType = ActionType.valueOf(gl.getName());
			String actionID = gl.getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
			String response = null;
			String doorID = null;
			switch(actionType) {
			case doorOpen:
				doorID = gl.getExpression(1).asValue().stringValue();
				response = this.mi.doorOpen(actionID, doorID);
				break;
			case doorClose:
				doorID = gl.getExpression(1).asValue().stringValue();
				response = this.mi.doorClose(actionID, doorID);
				break;
			default:
				response = "(fail)";
				break;
			}
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
		try {
			sendRobotInfo(robotID, x, y, loading);
			Thread.sleep(30);
			sendCurrentRobotInfo(robotID, x, y, loading);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public void sendMessageToTM(String message) throws Exception {
		this.send(taskManagerURI, message);
	}
	
	@Override
	public void sendPersonCall(int locationID, int callID) throws Exception {
		String gl = "(MosPersonCall " + locationID + " " + callID + ")";
		ds.assertFact(gl);
	}
	
	public static void main(String[] args) {
		String brokerURL = "tcp://" + System.getenv("JMS_BROKER");
		String mosURL = System.getenv("MOS");
		String brokerName = System.getenv("AGENT");
//		brokerURL = "tcp://127.0.0.1:61316";
		if(mosURL == null) {
			mosURL = "192.168.0.11:30001";
		}
		if(brokerName == null) {
			brokerName = "Local";
		}
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/" + brokerName + "/BehaviorInterface";
		
		BehaviorInterface bi = new LocalBehaviorInterface(brokerURL, brokerName, mosURL);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
	}
}