package behaviorInterface.test;

import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.*;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.LocalBehaviorInterface;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class Local {
	public static void main(String[] args) throws InterruptedException {
        
//		String brokerURL = "tcp://172.16.165.141:61313";
		String brokerURL = "tcp://127.0.0.1:61313";
		String mosURL = "127.0.0.1:30003";
//		String mosURL = "172.16.165.101:20006";
		
		String BehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";
		
		BehaviorInterface bi = new LocalBehaviorInterface(brokerURL, mosURL);
		
		ArbiAgentExecutor.execute(brokerURL, BehaviorInterfaceURI, bi, 2);
		
	}
}
