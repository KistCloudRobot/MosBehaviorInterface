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
	public String makeResponse() {
		String response = null;
		if(this.responseMessage instanceof AckEndLogin) {
			int result = ((AckEndLogin) this.responseMessage).getResult();
			if(result == 0) {
				response = "success";
			}
			else {
				response = "fail";
			}
			
		}
		return response;
	}
}
