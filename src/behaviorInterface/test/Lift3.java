package behaviorInterface.test;

import java.util.UUID;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.RobotBehaviorInterface;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Lift3 {
	public static void main(String[] args) {
		String brokerURL = "tcp://172.16.165.141:61114";
//		String brokerURL = "tcp://127.0.0.1:61116";
//		String mosURL = "172.16.165.208:36666";
		String mosURL = "172.16.165.102:30001";
//		String mosURL = "127.0.0.1:30003";
		String robotID = "AMR_LIFT3";
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";

		BehaviorInterface bi = new RobotBehaviorInterface(brokerURL, mosURL, robotID);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
	}
}