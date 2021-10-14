package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckMove extends AckMessage {
	private RobotID robotID;
	
	public AckMove(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckMove(RobotID robotID) {
		this.messageType = MessageType.AckMove;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
