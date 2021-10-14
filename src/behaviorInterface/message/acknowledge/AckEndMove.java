package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndMove extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndMove(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndMove(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndMove;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
