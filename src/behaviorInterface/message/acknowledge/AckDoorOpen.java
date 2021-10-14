package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class AckDoorOpen extends AckMessage {
	private DoorID doorID;
	
	public AckDoorOpen(int doorID) {
		this(DoorID.getEnum(doorID));
	}
	
	public AckDoorOpen(DoorID doorID) {
		this.messageType = MessageType.AckDoorOpen;
		this.doorID = doorID;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
}
