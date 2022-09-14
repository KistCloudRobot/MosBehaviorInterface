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
	
	public String makeResponse() {
		String response = "(fail)";
		if(this.responseMessage instanceof AckEndMessage) {
			Expression acionID = GLFactory.newExpression(GLFactory.newValue(this.getActionID()));
			Expression actionResult;
			int result = ((AckEndMessage) this.responseMessage).getResult();
			if(result == 0) {
				actionResult = GLFactory.newExpression(GLFactory.newValue("success"));
			}
			else {
				actionResult = GLFactory.newExpression(GLFactory.newValue("fail"));
			}
			
			GeneralizedList gl = GLFactory.newGL("ActionResult", acionID, actionResult);
			response = GLFactory.unescape(gl.toString());
		}
		else if(this.responseMessage instanceof AckMessage) {
			response = "(ok)";
		}
		return response;
	}
	
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
