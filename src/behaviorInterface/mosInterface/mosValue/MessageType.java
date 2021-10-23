package behaviorInterface.mosInterface.mosValue;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {
	RTSR(0x53469DFD), 
	AckMove(0x37911A75), 		AckEndMove(0x403A1BC4), 
	AckCancelMove(0x4AB1FE2D), 	AckEndCancelMove(0x31F00931), 
	AckLoad(0xC7C3BE2C), 		AckEndLoad(0x441C94CE), 
	AckUnload(0x868F59B6), 		AckEndUnload(0x85D82ED1), 
	AckCharge(0x9A427515), 		AckEndCharge(0x7F34A48F), 
	AckChargeStop(0x3412A63E), 	AckEndChargeStop(0x06DE35AC), 
	AckPause(0XD96EDE0B),		AckResume(0X1E8509B2),
	AckEndPause(0x57295C2B), 	AckEndResume(0xBD2D122E), 
	AckDoorOpen(0x71B2005B), 	AckEndDoorOpen(0xC9D2476A), 
	AckDoorClose(0x70F29DDD), 	AckEndDoorClose(0x98378F4B), 
	AckLogin(0x32BFB368), 		AckEndLogin(0x190ADBF8),
	PersonCall(0xA88DA5FE), 
	ReqMove(0x559657E8), 		ReqCancelMove(0x83EDC641), 
	ReqLoad(0xF8957DC8), 		ReqUnload(0xF03739D3), 
	ReqCharge(0x16B960EE), 		ReqChargeStop(0xCBC6486F), 
	ReqPause(0x28EC89C1), 		ReqResume(0xE09AF0FB), 
	ReqDoorOpen(0x078F486F), 	ReqDoorClose(0x4DDA0F69),
	ReqLogin(0x74A3BD60);
	
	private final int id;
	private static final Map<Integer, MessageType> BY_VALUE = new HashMap<>();
	
	MessageType(int id) {
		this.id = id;
	}
	
	static {
		for (MessageType id : values()) {
			BY_VALUE.put(id.id, id);
		}
	}
	
	public int getValue() {
		return id;
	}
	
	public static MessageType getEnum(int id) {
		return BY_VALUE.get(id);
	}
}
