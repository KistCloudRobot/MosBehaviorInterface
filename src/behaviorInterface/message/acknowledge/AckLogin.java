package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.RobotID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class AckLogin extends AckMessage {
	private RobotID robotID;
	
	public AckLogin(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckLogin(RobotID robotID) {
		this.messageType = MessageType.AckLogin;
		this.robotID = robotID;
	}
	
	public RobotID getLoginID() {
		return this.robotID;
	}
}
