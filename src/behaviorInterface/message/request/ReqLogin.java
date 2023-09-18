package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckEndLogin;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class ReqLogin extends ReqMessage {
	private RobotID robotiD;
	
	public ReqLogin(RobotID robotiD) {
		super(null, null);
		this.messageType = MessageType.ReqLogin;
		this.robotiD = robotiD;
	}
	
	public RobotID getLoginID() {
		return this.robotiD;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.Login;
	}
	
	@Override
	public String makeAckEndResponse() {
		String response = null;
		int result = this.ackEndMessage.getResult();
		if(result == 0) {
			response = "success";
		}
		else {
			response = "fail";
		}
		return response;
	}
}
