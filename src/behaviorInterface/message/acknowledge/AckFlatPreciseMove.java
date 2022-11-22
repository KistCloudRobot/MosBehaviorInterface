package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckFlatPreciseMove extends AckMessage {
	private RobotID robotID;
	
	public AckFlatPreciseMove(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckFlatPreciseMove(RobotID robotID) {
		this.messageType = MessageType.AckFlatPreciseMove;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
