package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckResume extends AckMessage {
	private RobotID robotID;
	
	public AckResume(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckResume(RobotID robotID) {
		this.messageType = MessageType.AckResume;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
