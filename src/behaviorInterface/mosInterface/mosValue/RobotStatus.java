package behaviorInterface.mosInterface.mosValue;

import java.util.HashMap;
import java.util.Map;

public enum RobotStatus {
	Login(0), Ready(1), Move(2), Paused(3), Loading(4), Unloading(5),
	ChargeIn(6), ChargeOut(7), Charging(8), ChargeStopping(9), 
	Error(10), EmergencyStop(11),
	GuideMove(11), PreciseMove(12), StraightBackMove(13);
	
	private final int status;
	private static final Map<Integer, RobotStatus> BY_VALUE = new HashMap<>();
	
	RobotStatus(int status) {
		this.status = status;
	}
	
	static {
		for (RobotStatus status : values()) {
			BY_VALUE.put(status.status, status);
		}
	}
	
	public int getValue() {
		return status;
	}
	
	public static RobotStatus getEnum(int status) {
		return BY_VALUE.get(status);
	}
}
