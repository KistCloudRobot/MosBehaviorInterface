package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckUnload extends AckMessage {
	private RobotID robotID;
	
	public AckUnload(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckUnload(RobotID robotID) {
		this.messageType = MessageType.AckUnload;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
