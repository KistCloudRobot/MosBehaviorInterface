package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class AckEndDoorOpen extends AckEndMessage {
	private DoorID doorID;
	
	public AckEndDoorOpen(int doorID, int result) {
		this(DoorID.getEnum(doorID), result);
	}
	
	public AckEndDoorOpen(DoorID doorID, int result) {
		this.messageType = MessageType.AckEndDoorOpen;
		this.doorID = doorID;
		this.result = result;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
}
