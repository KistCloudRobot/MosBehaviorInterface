package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqCancelMove extends ReqMessage {
	private RobotID robotID;
	
	public ReqCancelMove(String actionID, int robotID) {
		this(actionID, RobotID.getEnum(robotID));
	}
	
	public ReqCancelMove(String actionID, RobotID robotID) {
		super(actionID);
		this.messageType = MessageType.ReqCancelMove;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public ActionType getActionType() {
		return ActionType.cancelMove;
	}
}
