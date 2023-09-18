package behaviorInterface.test;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.PalletizerBehaviorInterface;
import behaviorInterface.RobotBehaviorInterface;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class BehaviorInterface_Docker {
	public static void main(String[] args) {
		String brokerURL = System.getenv("BROKER_ADDRESS");
		String robotID = System.getenv("ROBOT");
		String stringPort = System.getenv("BROKER_PORT");
		String mosURL = System.getenv("MOS_ADDRESS");
		BehaviorInterface bi = null;
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";
		if(robotID == null) {
			brokerURL = "127.0.0.1";
			robotID = "AMR_LIFT1";
			stringPort = "61116";
			mosURL = "127.0.0.1:30005";
		}

		int port = Integer.parseInt(stringPort);
		if(robotID.contains("LIFT")) {
			bi = new RobotBehaviorInterface(brokerURL, port, mosURL, robotID);
		} else if(robotID.contains("Palletizer")) {
			bi = new PalletizerBehaviorInterface(brokerURL, port, mosURL, robotID);
		}
		
		
		ArbiAgentExecutor.execute(brokerURL, port, BehaviorInterfaceURI, bi, BrokerType.ACTIVEMQ);
	}
}
