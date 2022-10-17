package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckPalletizerStop extends AckMessage {
	private RobotID palletizerID;
	
	public AckPalletizerStop(int palletizerID) {
		this(RobotID.getEnum(palletizerID));
	}
	
	public AckPalletizerStop(RobotID palletizerID) {
		this.messageType = MessageType.AckPalletizerStop;
		this.palletizerID = palletizerID;
	}
	
	public RobotID getPalletizerID() {
		return palletizerID;
	}
}
