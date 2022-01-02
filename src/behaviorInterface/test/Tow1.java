package behaviorInterface.test;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.RobotBehaviorInterface;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Tow1 {
	public static void main(String[] args) {
		String brokerURL = "tcp://172.16.165.106:61114";
		String mosURL = "127.0.0.1:30001";
		String brokerName = "Tow1";
		String robotID = "AMR_TOW1";
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/" + brokerName + "/BehaviorInterface";

		BehaviorInterface bi = new RobotBehaviorInterface(brokerURL, brokerName, mosURL, robotID);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
	}
}
