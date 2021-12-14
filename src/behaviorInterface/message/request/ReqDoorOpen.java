package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckDoorOpen;
import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.message.acknowledge.AckEndDoorOpen;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqDoorOpen extends ReqMessage {
	private DoorID doorID;
	
	public ReqDoorOpen(String sender, String actionID, int doorID) {
		this(sender, actionID, DoorID.getEnum(doorID));
	}
	
	public ReqDoorOpen(String sender, String actionID, DoorID doorID) {
		super(sender, actionID);
		this.messageType = MessageType.ReqDoorOpen;
		this.doorID = doorID;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
	
	public ActionType getActionType() {
		return ActionType.doorOpen;
	}
	
	public String makeResponse() {
		String response = null;
		if(this.responseMessage instanceof AckDoorOpen) {
			response = "(ok)";
		}
		if(this.responseMessage instanceof AckEndDoorOpen) {
			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
			Expression actionResult;
			int result = ((AckEndDoorOpen) this.responseMessage).getResult();
			if(result == 0) {
				actionResult = GLFactory.newExpression(GLFactory.newValue("success"));
			}
			else {
				actionResult = GLFactory.newExpression(GLFactory.newValue("fail"));
			}
			
			GeneralizedList gl = GLFactory.newGL(this.getActionType().toString(), acionID, actionResult);
			response = GLFactory.unescape(gl.toString());
		}
		return response;
	}
}
