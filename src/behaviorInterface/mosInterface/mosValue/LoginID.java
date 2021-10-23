package behaviorInterface.mosInterface.mosValue;

import java.util.HashMap;
import java.util.Map;

public enum LoginID {
	Local(0), Lift1(1), Lift2(2), Tow1(3), Tow2(4);
	
	private final int id;
	private static final Map<Integer, LoginID> BY_VALUE = new HashMap<>();
	
	LoginID(int id) {
		this.id = id;
	}
	
	static {
		for (LoginID id : values()) {
			BY_VALUE.put(id.id, id);
		}
	}
	
	public int getValue() {
		return id;
	}
	
	public static LoginID getEnum(int id) {
		return BY_VALUE.get(id);
	}
	
	public static LoginID[] getValues() {
		return values();
	}
}
