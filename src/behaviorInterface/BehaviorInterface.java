package behaviorInterface;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;

public abstract class BehaviorInterface extends ArbiAgent {
	public abstract void onRTSR(String robotID, String status, int x, int y, int theta, int speed, int battery, String loading);
	
	public abstract void sendAckEnd(String actionType, String actionID) throws Exception;
	
	public abstract void sendAckEnd(String actionType, String actionID, String result) throws Exception;
	
	public abstract void sendPersonCall(int locationID, int cmdID) throws Exception;
}
