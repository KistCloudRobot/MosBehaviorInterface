package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class PalletizerReleasingFinish extends AckMessage {
	private RobotID palletizerID;
	private int nodeID;
	
	public PalletizerReleasingFinish(int palletizerID, int nodeID) {
		this(RobotID.getEnum(palletizerID), nodeID);
	}
	
	public PalletizerReleasingFinish(RobotID palletizerID, int nodeID) {
		this.messageType = MessageType.PalletizerReleasingFinish;
		this.palletizerID = palletizerID;
		this.nodeID = nodeID;
	}
	
	public RobotID getPalletizerID() {
		return palletizerID;
	}
	
	public int getNodeID() {
		return nodeID;
	}
}
