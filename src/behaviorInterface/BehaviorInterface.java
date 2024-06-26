package behaviorInterface;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public abstract class BehaviorInterface extends ArbiAgent {
	public abstract void onRTSR(String robotID, String status, float x, float y, int theta, int speed, int battery, String loading);
	
	public abstract void palletizerPackingFinish(String palletizerID, int nodeID);

	public abstract void palletizerReleasingFinish(String palletizerID, int nodeID);
	
	public void sendMessage(String receiver, String message) throws Exception {
		System.out.println("[send]\t\t: " + receiver + " --> " + message + "\ttimestamp :" + System.currentTimeMillis() + "\n");
		this.send(receiver, message);
	}
	
	public abstract void sendPersonCall(int locationID, int cmdID) throws Exception;
}
