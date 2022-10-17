package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqEnterPalletizer extends ReqMessage {
	private RobotID palletizerID;
	private RobotID robotID;
	private int nodeID;
	
	public ReqEnterPalletizer(String sender, String actionID, int palletizerID, int robotID, int nodeID) {
		this(sender, actionID, RobotID.getEnum(palletizerID), RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqEnterPalletizer(String sender, String actionID, RobotID palletizerID, int robotID, int nodeID) {
		this(sender, actionID, palletizerID, RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqEnterPalletizer(String sender, String actionID, int palletizerID, RobotID robotID, int nodeID) {
		this(sender, actionID, RobotID.getEnum(palletizerID), robotID, nodeID);
	}
	
	public ReqEnterPalletizer(String sender, String actionID, RobotID palletizerID, RobotID robotID, int nodeID) {
		super(sender, actionID);
		this.messageType = MessageType.ReqEnterPalletizer;
		this.palletizerID = palletizerID;
		this.robotID = robotID;
		this.nodeID = nodeID;
	}
	
	public RobotID getPalletizerID() {
		return this.palletizerID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public int getNodeID() {
		return this.nodeID;
	}
	
	public ActionType getActionType() {
		return ActionType.EnterPalletizer;
	}
}
