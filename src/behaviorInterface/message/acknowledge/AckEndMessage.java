package behaviorInterface.message.acknowledge;

abstract public class AckEndMessage extends ResMessage {
	protected int result;
	
	public int getResult() {
		return result;
	}
}
