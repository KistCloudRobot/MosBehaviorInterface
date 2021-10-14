package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class ReqDoorOpen extends ReqMessage {
	private DoorID doorID;
	
	public ReqDoorOpen(String actionID, int doorID) {
		this(actionID, DoorID.getEnum(doorID));
	}
	
	public ReqDoorOpen(String actionID, DoorID doorID) {
		super(actionID);
		this.messageType = MessageType.ReqDoorOpen;
		this.doorID = doorID;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
	
	public ActionType getActionType() {
		return ActionType.doorOpen;
	}
}
