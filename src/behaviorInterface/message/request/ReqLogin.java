package behaviorInterface.message.request;

import behaviorInterface.message.acknowledge.AckEndLogin;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.LoginID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class ReqLogin extends ReqMessage {
	private LoginID mcArbiID;
	
	public ReqLogin(LoginID mcArbiID) {
		super(null, null);
		this.messageType = MessageType.ReqLogin;
		this.mcArbiID = mcArbiID;
	}
	
	public LoginID getLoginID() {
		return this.mcArbiID;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.login;
	}
	
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
