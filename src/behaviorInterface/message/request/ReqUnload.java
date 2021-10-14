package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqUnload extends ReqMessage {
	private RobotID robotID;
	private int nodeID;
	
	public ReqUnload(String actionID, int robotID, int nodeID) {
		this(actionID, RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqUnload(String actionID, RobotID robotID, int nodeID) {
		super(actionID);
		this.messageType = MessageType.ReqUnload;
		this.robotID = robotID;
		this.nodeID = nodeID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}

	public int getNodeID() {
		return nodeID;
	}
	
	public ActionType getActionType() {
		return ActionType.unload;
	}
}
