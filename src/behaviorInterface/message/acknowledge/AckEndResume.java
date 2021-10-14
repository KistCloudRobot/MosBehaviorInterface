package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndResume extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndResume(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckEndResume(RobotID robotID) {
		this.messageType = MessageType.AckEndResume;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
