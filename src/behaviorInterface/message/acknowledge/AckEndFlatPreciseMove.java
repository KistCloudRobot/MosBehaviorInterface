package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndFlatPreciseMove extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndFlatPreciseMove(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndFlatPreciseMove(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndFlatPreciseMove;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
