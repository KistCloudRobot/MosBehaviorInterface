package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckGuideMove extends AckMessage {
	private RobotID robotID;
	
	public AckGuideMove(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckGuideMove(RobotID robotID) {
		this.messageType = MessageType.AckGuideMove;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
