package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckStraightBackMove extends AckMessage {
	private RobotID robotID;
	
	public AckStraightBackMove(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckStraightBackMove(RobotID robotID) {
		this.messageType = MessageType.AckStraightBackMove;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
