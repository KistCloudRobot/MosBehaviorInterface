package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.message.acknowledge.AckEndUnload;
import behaviorInterface.message.acknowledge.AckUnload;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqUnload extends ReqMessage {
	private RobotID robotID;
	private int nodeID;
	
	public ReqUnload(String sender, String actionID, int robotID, int nodeID) {
		this(sender, actionID, RobotID.getEnum(robotID), nodeID);
	}
	
	public ReqUnload(String sender, String actionID, RobotID robotID, int nodeID) {
		super(sender, actionID);
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
		return ActionType.Unload;
	}
	
//	public String makeResponse() {
//		String response = null;
//		if(this.responseMessage instanceof AckUnload) {
//			response = "(ok)";
//		}
//		if(this.responseMessage instanceof AckEndUnload) {
//			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
//			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
//			Expression actionResult;
//			int result = ((AckEndUnload) this.responseMessage).getResult();
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
