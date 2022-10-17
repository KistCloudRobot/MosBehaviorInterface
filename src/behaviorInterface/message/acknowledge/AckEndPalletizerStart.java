package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;

public class AckEndPalletizerStart extends AckEndMessage {
	private RobotID palletizerID;
	
	public AckEndPalletizerStart(int palletizerID, int result) {
		this(RobotID.getEnum(palletizerID), result);
	}
	
	public AckEndPalletizerStart(RobotID palletizerID, int result) {
		this.messageType = MessageType.AckEndPalletizerStart;
		this.palletizerID = palletizerID;
		this.result = result;
	}
	
	public RobotID getPalletizerID() {
		return palletizerID;
	}
}
