package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckCancelMove extends AckMessage {
	private RobotID robotID;
	
	public AckCancelMove(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckCancelMove(RobotID robotID) {
		this.messageType = MessageType.AckCancelMove;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
