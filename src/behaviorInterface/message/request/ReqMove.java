package behaviorInterface.message.request;

import java.util.LinkedList;
import java.util.List;

import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ReqMove extends ReqMessage {
	private RobotID robotID;
	private int pathSize;
	private List<Integer> path;
	
	public ReqMove(String actionID, int robotID, int pathSize, List<Integer> path) {
		this(actionID, RobotID.getEnum(robotID), pathSize, path);
	}
	
	public ReqMove(String actionID, RobotID robotID, int pathSize, List<Integer> path) {
		super(actionID);
		this.messageType = MessageType.ReqMove;
		this.robotID = robotID;
		this.pathSize = pathSize;
		this.path = path;
	}
	
	public RobotID getRobotID() {
		return robotID;
	}
	
	public int getPathSize() {
		return pathSize;
	}
	
	public void appendPath(int nodeID) {
		this.path.add(nodeID);
	}
	
	public List<Integer> getPath() {
		return path;
	}
	
	public ActionType getActionType() {
		return ActionType.move;
	}
}
