package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndChargeStop extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndChargeStop(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndChargeStop(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndChargeStop;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
