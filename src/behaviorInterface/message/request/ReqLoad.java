package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.message.acknowledge.AckEndLoad;
import behaviorInterface.message.acknowledge.AckLoad;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

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
	
	public String makeResponse() {
		String response = null;
		System.out.println("here1");
		if(this.responseMessage instanceof AckLoad) {
			System.out.println("here2");
			response = "(ok)";
		}
		if(this.responseMessage instanceof AckEndLoad) {
			System.out.println("here3");
			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
			Expression actionResult;
			int result = ((AckEndLoad) this.responseMessage).getResult();
			if(result == 0) {
				actionResult = GLFactory.newExpression(GLFactory.newValue("success"));
			}
			else {
				actionResult = GLFactory.newExpression(GLFactory.newValue("fail"));
			}
			
			GeneralizedList gl = GLFactory.newGL(this.getActionType().toString(), acionID, actionResult);
			response = gl.toString();
		}
		return response;
	}
}
