package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndCharge extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndCharge(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndCharge(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndCharge;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
