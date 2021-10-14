package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckLoad extends AckMessage {
	private RobotID robotID;
	
	public AckLoad(int robotID) {
		this(RobotID.getEnum(robotID));
	}
	
	public AckLoad(RobotID robotID) {
		this.messageType = MessageType.AckLoad;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
}
