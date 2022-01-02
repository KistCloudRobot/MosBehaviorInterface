package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckEndGuideMove;
import behaviorInterface.message.acknowledge.AckGuideMove;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.Direction;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqGuideMove extends ReqMessage {
	private RobotID robotID;
	private int nodeID;
	private Direction direction;
	
	public ReqGuideMove(String sender, String actionID, int robotID, int nodeID, String direction) {
		this(sender, actionID, RobotID.getEnum(robotID), nodeID, Direction.valueOf(direction));
	}
	
	public ReqGuideMove(String sender, String actionID, int robotID, int nodeID, Direction direction) {
		this(sender, actionID, RobotID.getEnum(robotID), nodeID, direction);
	}
	
	public ReqGuideMove(String sender, String actionID, RobotID robotID, int nodeID, String direction) {
		this(sender, actionID, robotID, nodeID, Direction.valueOf(direction));
	}
	
	public ReqGuideMove(String sender, String actionID, RobotID robotID, int nodeID, Direction direction) {
		super(sender, actionID);
		this.messageType = MessageType.ReqGuideMove;
		this.robotID = robotID;
		this.nodeID = nodeID;
		this.direction = direction;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public int getNodeID() {
		return this.nodeID;
	}
	
	public Direction getDirection() {
		return this.direction;
	}
	
	public ActionType getActionType() {
		return ActionType.guideMove;
	}
	
	public String makeResponse() {
		String response = null;
		if(this.responseMessage instanceof AckGuideMove) {
			response = "(ok)";
		}
		else if(this.responseMessage instanceof AckEndGuideMove) {
			Expression id = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
			Expression acionID = GLFactory.newExpression(GLFactory.newGL("actionID", id));
			Expression actionResult;
			int result = ((AckEndGuideMove) this.responseMessage).getResult();
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
