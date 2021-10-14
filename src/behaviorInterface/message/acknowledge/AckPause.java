package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckPause extends AckMessage {
	private RobotID robotID;
	
	public AckPause(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckPause(RobotID robotID) {
		this.messageType = MessageType.AckPause;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
