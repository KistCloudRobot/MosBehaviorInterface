package behaviorInterface.message.request;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import behaviorInterface.message.BehaviorInterfaceMessage;
import behaviorInterface.message.acknowledge.AckDoorClose;
import behaviorInterface.message.acknowledge.AckEndDoorClose;
import behaviorInterface.message.acknowledge.AckEndMessage;
import behaviorInterface.message.acknowledge.AckMessage;
import behaviorInterface.mosInterface.mosValue.ActionType;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public abstract class ReqMessage extends BehaviorInterfaceMessage {
	private String sender;
	private String actionID;
	protected AckMessage ackMessage;
	protected AckEndMessage ackEndMessage;

	private final ResponseLock ackLock, ackEndLock;

	public ReqMessage(String sender, String actionID) {
		this.ackLock = new ResponseLock();
		this.ackEndLock = new ResponseLock();
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
	
	public void setAckMessage(AckMessage ackMessage) {
		this.ackLock.lock();
		try {
			this.ackMessage = ackMessage;
			this.ackLock.signal();
		} finally {
			this.ackLock.unlock();
		}
	}
	
	public void setAckEndMessage(AckEndMessage ackEndMessage) {
		this.ackEndLock.lock();
		try {
			this.ackEndMessage = ackEndMessage;
			this.ackEndLock.signal();
		} finally {
			this.ackEndLock.unlock();
		}
	}
	
	public String makeAckResponse() {
		return "(ok)";
	}
	
	public String makeAckEndResponse() {
		String response = "(fail)";
		Expression acionID = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
		Expression actionResult;
		
		int result = this.ackEndMessage.getResult();
		if(result == 0) actionResult = GLFactory.newExpression(GLFactory.newValue("success"));
		else actionResult = GLFactory.newExpression(GLFactory.newValue("fail"));
		
		GeneralizedList gl = GLFactory.newGL("ActionResult", acionID, actionResult);
		response = GLFactory.unescape(gl.toString());
		return response;
	}
	
	public String getAckResponse() {
		this.ackLock.lock();
		String response = null;
		try {
			do {
				if(this.ackMessage == null) this.ackLock.await();
				if(this.ackMessage != null) {
					response = this.makeAckResponse();
					if(response == null) {
						this.ackMessage = null;
						continue;
					}
				}
			} while(this.ackMessage == null);
		} 
		catch(InterruptedException ignore) {
			
		}
		finally {
			this.ackLock.unlock();
		}
		return response;
	}
	
	public String getAckEndResponse() {
		this.ackEndLock.lock();
		String response = null;
		try {
			do {
				if(this.ackEndMessage == null) this.ackEndLock.await();
				if(this.ackEndMessage != null) {
					response = this.makeAckEndResponse();
					if(response == null) {
						this.ackEndMessage = null;
						continue;
					}
				}
			} while(ackEndMessage == null);
		} 
		catch(InterruptedException ignore) {
			
		}
		finally {
			this.ackEndLock.unlock();
		}
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
