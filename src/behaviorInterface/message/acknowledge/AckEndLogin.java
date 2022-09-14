package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.RobotID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class AckEndLogin extends AckEndMessage {
	private RobotID robotID;
	private int result;
	
	public AckEndLogin(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndLogin(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndLogin;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getLoginID() {
		return this.robotID;
	}
	
	public int getResult() {
		return this.result;
	}
}
