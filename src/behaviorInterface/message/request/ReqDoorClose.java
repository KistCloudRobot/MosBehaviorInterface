package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckDoorClose;
import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.message.acknowledge.AckEndDoorClose;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqDoorClose extends ReqMessage {
	private DoorID doorID;
	
	public ReqDoorClose(String sender, String actionID, int doorID) {
		this(sender, actionID, DoorID.getEnum(doorID));
	}
	
	public ReqDoorClose(String sender, String actionID, DoorID doorID) {
		super(sender, actionID);
		this.messageType = MessageType.ReqDoorClose;
		this.doorID = doorID;
	}
	
	public DoorID getDoorID() {
		return doorID;
	}
	
	public ActionType getActionType() {
		return ActionType.DoorClose;
	}
	
//	public String makeResponse() {
//		String response = null;
//		if(this.responseMessage instanceof AckDoorClose) {
//			response = "(ok)";
//		}
//		if(this.responseMessage instanceof AckEndDoorClose) {
//			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
//			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
//			Expression actionResult;
//			int result = ((AckEndDoorClose) this.responseMessage).getResult();
//			if(result == 0) {
//				actionResult = GLFactory.newExpression(GLFactory.newValue("success"));
//			}
//			else {
//				actionResult = GLFactory.newExpression(GLFactory.newValue("fail"));
//			}
//			
//			GeneralizedList gl = GLFactory.newGL(this.getActionType().toString(), acionID, actionResult);
//			response = GLFactory.unescape(gl.toString());
//		}
//		return response;
//	}
}
