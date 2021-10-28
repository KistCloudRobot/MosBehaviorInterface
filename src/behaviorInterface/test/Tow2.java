package behaviorInterface.test;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.RobotBehaviorInterface;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Tow2 {
	public static void main(String[] args) {
		String brokerURL = "tcp://127.0.0.1:61412";
		String mosURL = "192.168.0.11:30001";
		String brokerName = "Tow2";
		String robotID = "AMR_TOW2";
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/" + brokerName + "/BehaviorInterface";

		BehaviorInterface bi = new RobotBehaviorInterface(brokerURL, brokerName, mosURL, robotID);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
	}
}
