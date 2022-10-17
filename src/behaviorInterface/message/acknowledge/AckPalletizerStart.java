package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckPalletizerStart extends AckMessage {
	private RobotID palletizerID;
	
	public AckPalletizerStart(int palletizerID) {
		this(RobotID.getEnum(palletizerID));
	}
	
	public AckPalletizerStart(RobotID palletizerID) {
		this.messageType = MessageType.AckPalletizerStart;
		this.palletizerID = palletizerID;
	}
	
	public RobotID getPalletizerID() {
		return palletizerID;
	}
}
