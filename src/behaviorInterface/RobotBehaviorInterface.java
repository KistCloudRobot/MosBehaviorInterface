package behaviorInterface;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import behaviorInterface.mosInterface.MosInterface;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.LoginID;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class RobotBehaviorInterface extends BehaviorInterface {
	private DataSource ds;
	private MosInterface mi;

	private final String brokerURL;
	private final String mcArbiID;
	private final String mosURL;
	private final String robotID;
	
	private final String taskManagerURI;
	
	public RobotBehaviorInterface(String brokerURL, String mcArbiID, String mosURL, String robotID) {
		this.brokerURL = brokerURL;
		this.mcArbiID = mcArbiID;
		this.mosURL = mosURL;
		this.robotID = robotID;
		
		this.taskManagerURI = "agent://www.arbi.com/" + this.mcArbiID + "/TaskManager";
	}
	
	@Override
	public void onStart() {
		try {
			String dataSourceURI = "ds://www.arbi.com/" + this.mcArbiID + "/BehaviorInterface";
			ds = new DataSource();
			ds.connect(brokerURL, dataSourceURI, 2);

			mi = new MosInterface(this.robotID, this);
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
			System.out.println(mi.login(LoginID.valueOf(this.mcArbiID)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void initDS() {
		try {
			ds.assertFact("(RobotLoading \"" + robotID + "\" \"loading\")");
			Thread.sleep(50);
			ds.assertFact("(RobotStatus \"" + robotID + "\" \"Ready\")");
			Thread.sleep(50);
			ds.assertFact("(RobotSpeed \"" + robotID + "\" 0)");
			Thread.sleep(50);
			ds.assertFact("(RobotBattery \"" + robotID + "\" 0)");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String onRequest(String sender, String request) {
		System.out.println("on request : " + request.toString());
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(request);
			ActionType actionType = ActionType.valueOf(gl.getName());
			String actionID = gl.getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
			int nodeID = 0;
			String doorID = null;
			String response = null;
			System.out.println("[" + robotID.toString() + "] " + actionType);
			switch(actionType) {
			case move:
				GeneralizedList pathGL = gl.getExpression(1).asGeneralizedList();
				List<Integer> path = new LinkedList<Integer>();
				int pathSize = pathGL.getExpressionsSize();
				for(int i = 0; i < pathSize; i++) {
					path.add(pathGL.getExpression(i).asValue().intValue());
				}
				response = this.mi.move(actionID, pathSize, path);
				break;
			case cancelMove:
				response = this.mi.cancelMove(actionID);
				break;
			case load:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.load(actionID, nodeID);
				break;
			case unload:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.unload(actionID, nodeID);
				break;
			case charge:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.charge(actionID, nodeID);
				break;
			case chargeStop:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.chargeStop(actionID, nodeID);
				break;
			case pause:
				response = this.mi.pause(actionID);
				break;
			case resume:
				response = this.mi.resume(actionID);
				break;
			default:
				response = "(fail)";
				break;
			}
			System.out.println("response : " + response);
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
		sendRobotPosition(robotID, x, y);
		sendRobotLoading(robotID, loading);
		sendRobotSatus(robotID, status);
		sendRobotSpeed(robotID, speed);
		sendRobotBattery(robotID, battery);
	}
	
	private void sendRobotPosition(String robotID, float x, float y) {
		String gl = "(RobotPosition \"" + robotID + "\" " + x + " " + y + ")";
//		System.out.println(gl);
		ds.assertFact(gl);
	}
	
	private void sendRobotLoading(String robotID, String loading) {
		String before = "(RobotLoading \"" + robotID + "\" $loading)";
		String after = "(RobotLoading \"" + robotID + "\" \"" + loading + "\")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	private void sendRobotSatus(String robotID, String status) {
		String before = "(RobotStatus \"" + robotID + "\" $status)";
		String after = "(RobotStatus \"" + robotID + "\" \"" + status + "\")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
		
	}
	
	private void sendRobotSpeed(String robotID, int speed) {
		String before = "(RobotSpeed \"" + robotID + "\" $speed)";
		String after = "(RobotSpeed \"" + robotID + "\" " + speed + ")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	private void sendRobotBattery(String robotID, int battery) {
		String before = "(RobotBattery \"" + robotID + "\" $battery)";
		String after = "(RobotBattery \"" + robotID + "\" " + battery + ")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	@Override
	public void sendMessageToTM(String message) throws Exception {
		System.out.println("send : " + message);
		this.send(taskManagerURI, message);
	}
	
	public void sendPersonCall(int locationID, int cmdID) {
		return;
	}	
	
	public static void main(String[] args) {
		String brokerURL = "tcp://" + System.getenv("JMS_BROKER");
		String mosURL = System.getenv("MOS");
		String brokerName = System.getenv("AGENT");
		String robotID = System.getenv("ROBOT");
//		brokerURL = "tcp://127.0.0.1:61116";
		if(mosURL == null) {
			mosURL = "192.168.0.11:30001";
		}
		if(brokerName == null) {
			brokerName = "Lift1";
		}
		if(robotID == null) {
			robotID = "AMR_LIFT1";
		}
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/" + brokerName + "/BehaviorInterface";

		BehaviorInterface bi = new RobotBehaviorInterface(brokerURL, brokerName, mosURL, robotID);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
	}
}