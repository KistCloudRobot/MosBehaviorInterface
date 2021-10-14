package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class AckDoorClose extends AckMessage {
	private DoorID doorID;
	
	public AckDoorClose(int doorID) {
		this(DoorID.getEnum(doorID));
	}
	
	public AckDoorClose(DoorID doorID) {
		this.messageType = MessageType.AckDoorClose;
		this.doorID = doorID;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
}
