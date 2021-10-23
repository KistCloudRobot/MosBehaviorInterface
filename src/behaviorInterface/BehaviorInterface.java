package behaviorInterface;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public abstract class BehaviorInterface extends ArbiAgent {
	public abstract void onRTSR(String robotID, String status, float x, float y, int theta, int speed, int battery, String loading);
	
	public abstract void sendMessageToTM(String message) throws Exception;
	
	public abstract void sendPersonCall(int locationID, int cmdID) throws Exception;
}
