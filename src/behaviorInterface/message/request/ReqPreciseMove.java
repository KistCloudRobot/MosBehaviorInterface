package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckEndPreciseMove;
import behaviorInterface.message.acknowledge.AckPreciseMove;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqPreciseMove extends ReqMessage {
	private RobotID robotID;
	private int nodeID;
	
	public ReqPreciseMove(String sender, String actionID, int robotID, int nodeID) {
		this(sender, actionID, RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqPreciseMove(String sender, String actionID, RobotID robotID, int nodeID) {
		super(sender, actionID);
		this.messageType = MessageType.ReqPreciseMove;
		this.robotID = robotID;
		this.nodeID = nodeID;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public int getNodeID() {
		return this.nodeID;
	}
	
	public ActionType getActionType() {
		return ActionType.PreciseMove;
	}
	
//	public String makeResponse() {
//		String response = null;
//		if(this.responseMessage instanceof AckPreciseMove) {
//			response = "(ok)";
//		}
//		else if(this.responseMessage instanceof AckEndPreciseMove) {
//			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
//			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
//			Expression actionResult;
//			int result = ((AckEndPreciseMove) this.responseMessage).getResult();
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
