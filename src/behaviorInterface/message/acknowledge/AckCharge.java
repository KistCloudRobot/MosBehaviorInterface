package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckCharge extends AckMessage {
	private RobotID robotID;
	
	public AckCharge(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckCharge(RobotID robotID) {
		this.messageType = MessageType.AckCharge;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
