package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.LoginID;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckLogin extends AckMessage {
	private LoginID mcArbiID;
	
	public AckLogin(int mcArbiID) {
		this(LoginID.getEnum(mcArbiID));
	}
	
	public AckLogin(LoginID mcArbiID) {
		this.messageType = MessageType.AckLogin;
		this.mcArbiID = mcArbiID;
	}
	
	public LoginID getLoginID() {
		return this.mcArbiID;
	}
}
