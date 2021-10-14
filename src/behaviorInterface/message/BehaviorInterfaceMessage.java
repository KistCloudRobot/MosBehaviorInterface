package behaviorInterface.message;

import behaviorInterface.mosInterface.mosValue.MessageType;

public abstract class BehaviorInterfaceMessage {
	protected MessageType messageType;
	
	public MessageType getType() {
		return messageType;
	}
}
