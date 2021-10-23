package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;

public class PersonCall extends AckMessage {
	private int locationID;
	private int callID;
	
	public PersonCall(int locationID, int callID) {
		this.messageType = MessageType.PersonCall;
		this.locationID = locationID;
		this.callID = callID;
	}
	
	public int getLocationID() {
		return locationID;
	}
	
	public int getCallID() {
		return callID;
	}

}
