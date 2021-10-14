package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqResume extends ReqMessage {
	private RobotID robotID;
	
	public ReqResume(String actionID, int robotID) {
		this(actionID, RobotID.getEnum(robotID));
	}
	
	public ReqResume(String actionID, RobotID robotID) {
		super(actionID);
		this.messageType = MessageType.ReqResume;
		this.robotID = robotID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public ActionType getActionType() {
		return ActionType.resume;
	}
}
