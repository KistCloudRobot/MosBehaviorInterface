package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.LoginID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class AckEndLogin extends AckEndMessage {
	private LoginID mcArbiID;
	private int result;
	
	public AckEndLogin(int mcArbiID, int result) {
		this(LoginID.getEnum(mcArbiID), result);
	}
	
	public AckEndLogin(LoginID mcArbiID, int result) {
		this.messageType = MessageType.AckEndLogin;
		this.mcArbiID = mcArbiID;
		this.result = result;
	}
	
	public LoginID getLoginID() {
		return this.mcArbiID;
	}
	
	public int getResult() {
		return this.result;
	}
}
