package behaviorInterface.message.request;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqPalletizerStart extends ReqMessage {
	private RobotID palletizerID;
	
	public ReqPalletizerStart(String sender, String actionID, int palletizerID) {
		this(sender, actionID, RobotID.getEnum(palletizerID));
	}
	
	public ReqPalletizerStart(String sender, String actionID, RobotID palletizerID) {
		super(sender, actionID);
		this.messageType = MessageType.ReqPalletizerStart;
		this.palletizerID = palletizerID;
	}
	
	public RobotID getPalletizerID() {
		return palletizerID;
	}
	
	public ActionType getActionType() {
		return ActionType.PalletizerStart;
	}
}
