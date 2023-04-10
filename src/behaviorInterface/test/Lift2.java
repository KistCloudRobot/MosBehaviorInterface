package behaviorInterface.test;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.RobotBehaviorInterface;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Lift2 {
	public static void main(String[] args) {
//		String brokerURL = "tcp://172.16.165.141:61115";
//		String brokerURL = "127.0.0.1";
		String brokerURL = "172.16.165.164";
		//String mosURL = "172.16.165.164:36666";
		String mosURL = "172.16.165.160:30001";
//		String mosURL = "172.16.165.102:30001";
//		String mosURL = "127.0.0.1:30001";
//		String mosURL = "192.168.100.3:30001";
//		String mosURL = "192.168.100.15:30001";
		String robotID = "AMR_LIFT2";
		int port = 61115;
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";

		BehaviorInterface bi = new RobotBehaviorInterface(brokerURL, port, mosURL, robotID);
		
		ArbiAgentExecutor.execute(brokerURL,port, BehaviorInterfaceURI, bi, BrokerType.ACTIVEMQ);
	}
}
