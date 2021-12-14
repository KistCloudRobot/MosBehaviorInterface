package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndPreciseMove extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndPreciseMove(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndPreciseMove(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndPreciseMove;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
