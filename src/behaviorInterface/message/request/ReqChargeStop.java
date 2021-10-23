package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckChargeStop;
import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.message.acknowledge.AckEndChargeStop;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqChargeStop extends ReqMessage {
	private RobotID robotID;
	private int nodeID;
	
	public ReqChargeStop(String actionID, int robotID, int nodeID) {
		this(actionID, RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqChargeStop(String actionID, RobotID robotID, int nodeID) {
		super(actionID);
		this.messageType = MessageType.ReqChargeStop;
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
		return ActionType.chargeStop;
	}
	
	public String makeResponse() {
		String response = null;
		if(this.responseMessage instanceof AckChargeStop) {
			response = "(ok)";
		}
		if(this.responseMessage instanceof AckEndChargeStop) {
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
