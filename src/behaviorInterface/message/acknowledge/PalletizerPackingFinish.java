package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class PalletizerPackingFinish extends AckMessage {
	private RobotID palletizerID;
	private int nodeID;
	
	public PalletizerPackingFinish(int palletizerID, int nodeID) {
		this(RobotID.getEnum(palletizerID), nodeID);
	}
	
	public PalletizerPackingFinish(RobotID palletizerID, int nodeID) {
		this.messageType = MessageType.PalletizerPackingFinish;
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
