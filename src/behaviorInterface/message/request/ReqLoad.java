package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqLoad extends ReqMessage {
	private RobotID robotID;
	private int nodeID;
	
	public ReqLoad(String actionID, int robotID, int nodeID) {
		this(actionID, RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqLoad(String actionID, RobotID robotID, int nodeID) {
		super(actionID);
		this.messageType = MessageType.ReqLoad;
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
		return ActionType.load;
	}
}
