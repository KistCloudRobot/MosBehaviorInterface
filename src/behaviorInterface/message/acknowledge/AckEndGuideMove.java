package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndGuideMove extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndGuideMove(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndGuideMove(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndGuideMove;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
