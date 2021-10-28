package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckCharge;
import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.message.acknowledge.AckEndCharge;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqCharge extends ReqMessage {
	private RobotID robotID;
	private int nodeID;
	
	public ReqCharge(String actionID, int robotID, int nodeID) {
		this(actionID, RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqCharge(String actionID, RobotID robotID, int nodeID) {
		super(actionID);
		this.messageType = MessageType.ReqCharge;
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
		return ActionType.charge;
	}
	
	public String makeResponse() {
		String response = null;
		if(this.responseMessage instanceof AckCharge) {
			response = "(ok)";
		}
		if(this.responseMessage instanceof AckEndCharge) {
			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
			Expression actionResult;
			int result = ((AckEndCharge) this.responseMessage).getResult();
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
