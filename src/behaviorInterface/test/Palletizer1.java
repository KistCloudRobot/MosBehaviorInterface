package behaviorInterface.test;

import java.util.UUID;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.PalletizerBehaviorInterface;
import behaviorInterface.RobotBehaviorInterface;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Palletizer1 {
	public static void main(String[] args) {
//		String brokerURL = "tcp://172.16.165.141:61112";
		String brokerURL = "127.0.0.1";
//		String brokerURL = "tcp://192.168.100.10:62112";
//		String mosURL = "172.16.165.208:36666";
//		String mosURL = "172.16.165.102:30001";
//		String mosURL = "127.0.0.1:30001";
		String mosURL = "192.168.100.3:30001";
//		String mosURL = "192.168.100.15:30001";
		String palletizerID = "Palletizer1";
		int port = 61112;
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";

		BehaviorInterface bi = new PalletizerBehaviorInterface(brokerURL, port, mosURL, palletizerID);
		
		ArbiAgentExecutor.execute(brokerURL, 61112, BehaviorInterfaceURI, bi, BrokerType.ACTIVEMQ);
	}
}