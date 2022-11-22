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

public class RobotBehaviorInterface extends BehaviorInterface {
	private DataSource ds;
	private MosInterface mi;

	private final String brokerURL;
	private final String mosURL;
	private final String robotID;
	
	public RobotBehaviorInterface(String brokerURL, String mosURL, String robotID) {
		this.brokerURL = brokerURL;
		this.mosURL = mosURL;
		this.robotID = robotID;
	}
	
	@Override
	public void onStart() {
		try {
			String dataSourceURI = "ds://www.arbi.com/BehaviorInterface";
			ds = new DataSource();
			ds.connect(brokerURL, dataSourceURI, BrokerType.ACTIVEMQ);

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
			System.out.println(mi.login(RobotID.valueOf(this.robotID)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void initDS() {
		try {
			ds.assertFact("(robotLoading \"" + robotID + "\" \"loading\")");
			Thread.sleep(50);
			ds.assertFact("(robotStatus \"" + robotID + "\" \"Ready\")");
			Thread.sleep(50);
			ds.assertFact("(robotSpeed \"" + robotID + "\" 0)");
			Thread.sleep(50);
			ds.assertFact("(batteryRemain \"" + robotID + "\" 0)");
			Thread.sleep(50);
			ds.assertFact("(robotDegree \"" + robotID + "\" 0)");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String onRequest(String sender, String request) {
		System.out.println("[request]\t: " + request.toString());
		try {
			GeneralizedList gl = GLFactory.newGLFromGLString(request);
			ActionType actionType = ActionType.valueOf(gl.getName());
			String actionID = gl.getExpression(0).asValue().stringValue();
			int nodeID = 0;
			String direction = null;
			String response = null;
			switch(actionType) {
			case Move:
				GeneralizedList pathGL = gl.getExpression(1).asGeneralizedList();
				List<Integer> path = new LinkedList<Integer>();
				int pathSize = pathGL.getExpressionsSize();
				for(int i = 0; i < pathSize; i++) {
					path.add(pathGL.getExpression(i).asValue().intValue());
				}
				response = this.mi.move(sender, actionID, pathSize, path);
				break;
			case CancelMove:
				response = this.mi.cancelMove(sender, actionID);
				break;
			case Load:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.load(sender, actionID, nodeID);
				break;
			case Unload:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.unload(sender, actionID, nodeID);
				break;
			case Charge:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.charge(sender, actionID, nodeID);
				break;
			case ChargeStop:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.chargeStop(sender, actionID, nodeID);
				break;
			case Pause:
				response = this.mi.pause(sender, actionID);
				break;
			case Resume:
				response = this.mi.resume(sender, actionID);
				break;
			case GuideMove:
				nodeID = gl.getExpression(1).asValue().intValue();
				direction = gl.getExpression(2).asValue().stringValue();
				response = this.mi.guideMove(sender, actionID, nodeID, direction);
				break;
			case PreciseMove:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.preciseMove(sender, actionID, nodeID);
				break;
			case FlatPreciseMove:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.flatPreciseMove(sender, actionID, nodeID);
				break;
			case StraightBackMove:
				nodeID = gl.getExpression(1).asValue().intValue();
				response = this.mi.straightBackMove(sender, actionID, nodeID);
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
		sendRobotPosition(robotID, x, y);
		sendRobotLoading(robotID, loading);
		sendRobotSatus(robotID, status);
		sendRobotSpeed(robotID, speed);
		sendRobotBattery(robotID, battery);
		sendRobotDegree(robotID, theta);
	}
	
	@Override
	public void palletizerPackingFinish(String palletizerID, int nodeID) {
		System.out.println("what? palletizer packing finish");
	}
	
	private void sendRobotPosition(String robotID, float x, float y) {
		String before = "(robotPosition \"" + robotID + "\" $x $y)";
		String after = "(robotPosition \"" + robotID + "\" " + x + " " + y + ")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	private void sendRobotLoading(String robotID, String loading) {
		String before = "(robotLoading \"" + robotID + "\" $loading)";
		String after = "(robotLoading \"" + robotID + "\" \"" + loading + "\")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	private void sendRobotSatus(String robotID, String status) {
		String before = "(robotStatus \"" + robotID + "\" $status)";
		String after = "(robotStatus \"" + robotID + "\" \"" + status + "\")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
		
	}
	
	private void sendRobotSpeed(String robotID, int speed) {
		String before = "(robotSpeed \"" + robotID + "\" $speed)";
		String after = "(robotSpeed \"" + robotID + "\" " + speed + ")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	private void sendRobotBattery(String robotID, int battery) {
		String before = "(batteryRemain \"" + robotID + "\" $battery)";
		String after = "(batteryRemain \"" + robotID + "\" " + battery + ")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	private void sendRobotDegree(String robotID, int theta) {
		String before = "(robotDegree \"" + robotID + "\" $degree)";
		String after = "(robotDegree \"" + robotID + "\" " + theta + ")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
	}
	
	public void sendPersonCall(int locationID, int cmdID) {
		return;
	}	
	
	public static void main(String[] args) {
		String robotID = null;
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
		if(args == null) {
			robotID = "AMR_LIFT1";
			brokerURL = "tcp://172.16.165.141:61116";
			mosURL = "127.0.0.1:30001";
		}
		else {
			robotID = args[0];
			brokerURL = args[1];
			mosURL = args[2];
		}
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";

		BehaviorInterface bi = new RobotBehaviorInterface(brokerURL, mosURL, robotID);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, BrokerType.ZEROMQ);
	}
}
