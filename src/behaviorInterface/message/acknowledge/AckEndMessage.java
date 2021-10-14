package behaviorInterface.message.acknowledge;

abstract public class AckEndMessage extends AckMessage {
	protected int result;
	
	public int getResult() {
		return result;
	}
}
