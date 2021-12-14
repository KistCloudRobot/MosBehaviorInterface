package behaviorInterface.message.request;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import behaviorInterface.message.BehaviorInterfaceMessage;
import behaviorInterface.message.acknowledge.AckMessage;
import behaviorInterface.mosInterface.mosValue.ActionType;

public abstract class ReqMessage extends BehaviorInterfaceMessage {
	private String sender;
	private String actionID;
	protected AckMessage responseMessage;

	private final ResponseLock responceLock;

	public ReqMessage(String sender, String actionID) {
		responceLock = new ResponseLock();
		this.sender = sender;
		this.actionID = actionID;
	}
	
	private class ResponseLock {
		private final Lock						lock;
		private final Condition					responseArrived;
		public ResponseLock() {
			this.lock = new ReentrantLock();
			responseArrived = lock.newCondition();
		}
		private void lock(){
			this.lock.lock();
		}
		private void signal(){
			this.responseArrived.signalAll();
		}
		private void await() throws InterruptedException {
			this.responseArrived.await();
			
		}
		private void unlock() {
			this.lock.unlock();
			
		}
	}
	
	public void setResponse(AckMessage responseMessage) {
		responceLock.lock();
		try {
			this.responseMessage = responseMessage;
			responceLock.signal();
		} finally {
			responceLock.unlock();
		}
	}
	
	abstract public String makeResponse();
	
	public String getResponse() {
		responceLock.lock();
		String response = null;
		try {
			do {
				if(this.responseMessage == null) {
					responceLock.await();
				}
				if(this.responseMessage != null) {
					response = this.makeResponse();
					if(response == null) {
						this.responseMessage = null;
						continue;
					}
				}
			} while(responseMessage == null);
		} 
		catch(InterruptedException ignore) {
			
		}
		finally {
			responceLock.unlock();
		}

		this.responseMessage = null;
		return response;
	}
	
	
	public String getActionID() {
		return this.actionID;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public abstract ActionType getActionType();
}
