package behaviorInterface.test;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.LocalBehaviorInterface;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Local {
	public static void main(String[] args) {
		String brokerURL = "tcp://172.16.165.106:61313";
		String mosURL = "127.0.0.1:30001";
		String brokerName = "Local";
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/" + brokerName + "/BehaviorInterface";
		
		BehaviorInterface bi = new LocalBehaviorInterface(brokerURL, brokerName, mosURL);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
	}
}
