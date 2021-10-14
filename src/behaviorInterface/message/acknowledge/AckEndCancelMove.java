package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndCancelMove extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndCancelMove(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndCancelMove(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndCancelMove;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
