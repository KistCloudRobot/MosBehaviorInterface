package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndLoad extends AckEndMessage {
	private RobotID robotID;
	
	public AckEndLoad(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndLoad(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndLoad;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
