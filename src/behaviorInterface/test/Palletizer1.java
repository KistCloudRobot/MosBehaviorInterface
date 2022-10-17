package behaviorInterface.test;

import java.util.UUID;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.PalletizerBehaviorInterface;
import behaviorInterface.RobotBehaviorInterface;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Palletizer1 {
	public static void main(String[] args) {
		String brokerURL = "tcp://172.16.165.141:61112";
//		String brokerURL = "tcp://127.0.0.1:61116";
//		String mosURL = "172.16.165.208:36666";
//		String mosURL = "172.16.165.102:30001";
		String mosURL = "127.0.0.1:30001";
		String palletizerID = "Palletizer1";
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";

		BehaviorInterface bi = new PalletizerBehaviorInterface(brokerURL, mosURL, palletizerID);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, BrokerType.ZEROMQ);
	}
}