package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

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
	
	public String makeResponse() {
		String response = null;
		if(this.responseMessage instanceof AckEndCancelMove) {
			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
			Expression actionResult;
			int result = ((AckEndCancelMove) this.responseMessage).getResult();
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
