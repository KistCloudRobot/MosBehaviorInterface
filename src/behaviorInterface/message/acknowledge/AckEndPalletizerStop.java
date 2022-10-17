package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndPalletizerStop extends AckEndMessage {
	private RobotID palletizerID;
	
	public AckEndPalletizerStop(int palletizerID, int result) {
		this(RobotID.getEnum(palletizerID), result);
	}
	
	public AckEndPalletizerStop(RobotID palletizerID, int result) {
		this.messageType = MessageType.AckEndPalletizerStop;
		this.palletizerID = palletizerID;
		this.result = result;
	}
	
	public RobotID getPalletizerID() {
		return palletizerID;
	}
}
