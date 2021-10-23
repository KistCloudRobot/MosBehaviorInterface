package behaviorInterface.message.acknowledge;

import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import behaviorInterface.mosInterface.mosValue.RobotStatus;

public class RTSR extends AckMessage {
	private RobotID robotID;
	private RobotStatus robotStatus;
	private float x;
	private float y;
	private int theta;
	private int speed;
	private int battery;
	private boolean loading;
	
	public RTSR(RobotID robotID, RobotStatus robotStatus, float x, float y, int theta, int speed, int battery, boolean loading) {
		this.messageType = MessageType.RTSR;
		this.robotID = robotID;
		this.robotStatus = robotStatus;
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.speed = speed;
		this.battery = battery;
		this.loading = loading;
	}

	public RobotStatus getRobotStatus() {
		return robotStatus;
	}

	public RobotID getRobotID() {
		return robotID;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getSpeed() {
		return speed;
	}

	public int getBattery() {
		return battery;
	}

	public boolean isLoading() {
		return loading;
	}

	public int getTheta() {
		return theta;
	}
}
