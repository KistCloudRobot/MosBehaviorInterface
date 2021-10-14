package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqPause extends ReqMessage {
	private RobotID robotID;
	
	public ReqPause(String actionID, int robotID) {
		this(actionID, RobotID.getEnum(robotID));
	}
	
	public ReqPause(String actionID, RobotID robotID) {
		super(actionID);
		this.messageType = MessageType.ReqPause;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public ActionType getActionType() {
		return ActionType.pause;
	}
}
