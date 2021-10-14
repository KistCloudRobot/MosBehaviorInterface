package behaviorInterface.mosInterface.mosValue;

import java.util.HashMap;
import java.util.Map;

public enum DoorID {
	Door1(0);
	
	private final int id;
	private static final Map<Integer, DoorID> BY_VALUE = new HashMap<>();
	
	DoorID(int id) {
		this.id = id;
	}
	
	static {
		for (DoorID id : values()) {
			BY_VALUE.put(id.id, id);
		}
	}
	
	public int getValue() {
		return id;
	}
	
	public static DoorID getEnum(int id) {
		return BY_VALUE.get(id);
	}
}
