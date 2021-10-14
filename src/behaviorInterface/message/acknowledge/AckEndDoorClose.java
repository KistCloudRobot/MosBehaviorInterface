package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class AckEndDoorClose extends AckEndMessage {
	private DoorID doorID;
	
	public AckEndDoorClose(int doorID, int result) {
		this(DoorID.getEnum(doorID), result);
	}
	
	public AckEndDoorClose(DoorID doorID, int result) {
		this.messageType = MessageType.AckEndDoorClose;
		this.doorID = doorID;
		this.result = result;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
}
