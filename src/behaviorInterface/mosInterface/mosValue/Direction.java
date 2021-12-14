package behaviorInterface.mosInterface.mosValue;

import java.util.HashMap;
import java.util.Map;

public enum Direction {
	Forward(0), Backward(1);
	
	private final int id;
	private static final Map<Integer, Direction> BY_VALUE = new HashMap<>();
	
	Direction(int id) {
		this.id = id;
	}
	
	static {
		for (Direction id : values()) {
			BY_VALUE.put(id.id, id);
		}
	}
	
	public int getValue() {
		return id;
	}
	
	public static Direction getEnum(int id) {
		return BY_VALUE.get(id);
	}
}
