package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndStraightBackMove extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndStraightBackMove(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndStraightBackMove(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndStraightBackMove;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
