package behaviorInterface.mosInterface.mosValue;

import java.util.HashMap;
import java.util.Map;

public enum RobotID {
	LOCAL(0), AMR_LIFT1(1), AMR_LIFT2(2), AMR_LIFT3(3), AMR_LIFT4(4), AMR_LIFT5(6), AMR_LIFT6(7), AMR_LIFT7(8),
	Palletizer1(5), Palletizer2(9), Palletizer3(10);
	
	private final int id;
	private static final Map<Integer, RobotID> BY_VALUE = new HashMap<>();
	
	RobotID(int id) {
		this.id = id;
	}
	
	static {
		for (RobotID id : values()) {
			BY_VALUE.put(id.id, id);
		}
	}
	
	public int getValue() {
		return id;
	}
	
	public static RobotID getEnum(int id) {
		return BY_VALUE.get(id);
	}
	
	public static RobotID[] getValues() {
		return values();
	}
}
