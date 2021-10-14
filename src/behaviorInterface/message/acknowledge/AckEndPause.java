package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndPause extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndPause(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckEndPause(RobotID robotID) {
		this.messageType = MessageType.AckEndPause;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
