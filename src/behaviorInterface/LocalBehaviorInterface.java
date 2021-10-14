package behaviorInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import behaviorInterface.mosInterface.MosInterface;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.framework.ArbiFrameworkServer;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class LocalBehaviorInterface extends BehaviorInterface {
	private DataSource ds;
	private MosInterface mi;

	private String brokerURL;
	private String serverName;
	private String mosURL;
	
	public LocalBehaviorInterface(String brokerURL, String serverName, String mosURL) {
		this.brokerURL = brokerURL;
		this.serverName = serverName;
		this.mosURL = mosURL;
	}
	
	@Override
	public void onStart() {
		try {
			String dataSourceURI = "ds://www.arbi.com/" + serverName + "/BehaviorInterface";
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
	public void onRTSR(String robotID, String status, int x, int y, int theta, int speed, int battery, String loading) {
		try {
			sendRobotInfo(robotID, x, y, loading);
			Thread.sleep(30);
			sendCurrentRobotInfo(robotID, x, y, loading);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendRobotInfo(String robotID, int x, int y, String loading) {
		String gl = "(RobotInfo \"" + robotID + "\" " + x + " " + y + " \"" + loading + "\" \"" + System.currentTimeMillis() + "\")";
//		System.out.println(gl);
		ds.assertFact(gl);
	}

	private void sendCurrentRobotInfo(String robotID, int x, int y, String loading) {
		String before = "(CurrentRobotInfo \"" + robotID + "\" $x $y $loading)";
		String after = "(CurrentRobotInfo \"" + robotID + "\" " + x + " " + y + " \"" + loading + "\")";
		String updateGL = "(update " + before + " " + after + ")";
//		System.out.println(updateGL);
		ds.updateFact(updateGL);
		
	}

	@Override
	public void sendAckEnd(String actionType, String actionID) throws Exception {
		throw new Exception();
	}

	@Override
	public void sendAckEnd(String actionType, String actionID, String result) throws Exception {
		throw new Exception();
	}
	
	@Override
	public void sendPersonCall(int locationID, int callID) throws Exception {
		String gl = "(MosPersonCall " + locationID + " " + callID + ")";
		ds.assertFact(gl);
	}
	
	public static void main(String[] args) {
		String brokerURL = System.getenv("JMS_BROKER");
		String mosURL = System.getenv("MOS");
		String brokerName = System.getenv("AGENT");
		brokerURL = "tcp://127.0.0.1:61316";
		if(mosURL == null) {
			mosURL = "127.0.0.1:30001";
		}
		if(brokerName == null) {
			brokerName = "Local";
		}
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/" + brokerName + "/BehaviorInterface";
		
		BehaviorInterface bi = new LocalBehaviorInterface(brokerURL, brokerName, mosURL);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
	}
}
