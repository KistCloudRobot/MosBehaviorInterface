package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqPalletizerStop extends ReqMessage {
	private RobotID robotID;
	
	public ReqPalletizerStop(String sender, String actionID, int robotID) {
		this(sender, actionID, RobotID.getEnum(robotID));
	}
	
	public ReqPalletizerStop(String sender, String actionID, RobotID robotID) {
		super(sender, actionID);
		this.messageType = MessageType.ReqPalletizerStop;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public ActionType getActionType() {
		return ActionType.PalletizerStop;
	}
}
