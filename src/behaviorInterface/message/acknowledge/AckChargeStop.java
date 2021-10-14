package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckChargeStop extends AckMessage {
	private RobotID robotID;
	
	public AckChargeStop(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckChargeStop(RobotID robotID) {
		this.messageType = MessageType.AckChargeStop;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
