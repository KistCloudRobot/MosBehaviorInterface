package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class ReqDoorClose extends ReqMessage {
	private DoorID doorID;
	
	public ReqDoorClose(String actionID, int doorID) {
		this(actionID, DoorID.getEnum(doorID));
	}
	
	public ReqDoorClose(String actionID, DoorID doorID) {
		super(actionID);
		this.messageType = MessageType.ReqDoorClose;
		this.doorID = doorID;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
	
	public ActionType getActionType() {
		return ActionType.doorClose;
	}
}
