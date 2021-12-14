package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckPreciseMove extends AckMessage {
	private RobotID robotID;
	
	public AckPreciseMove(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckPreciseMove(RobotID robotID) {
		this.messageType = MessageType.AckPreciseMove;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
