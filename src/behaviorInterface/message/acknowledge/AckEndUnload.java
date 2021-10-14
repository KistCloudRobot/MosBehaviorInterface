package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndUnload extends AckEndMessage {
	private RobotID robotID;
	private int result;
	
	public AckEndUnload(int robotID, int result) {
		this(RobotID.getEnum(robotID), result);
	}
	
	public AckEndUnload(RobotID robotID, int result) {
		this.messageType = MessageType.AckEndUnload;
		this.robotID = robotID;
		this.result = result;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}

	public int getResult() {
		return result;
	}
}
