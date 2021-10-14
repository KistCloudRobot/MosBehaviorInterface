package behaviorInterface.message.request;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import behaviorInterface.message.BehaviorInterfaceMessage;
import behaviorInterface.mosInterface.mosValue.ActionType;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public abstract class ReqMessage extends BehaviorInterfaceMessage {
	private String actionID;
	private String response;

	private final ResponseLock responceLock;

	public ReqMessage(String actionID) {
		responceLock = new ResponseLock();
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
	
	public void setResponse(String response) {
		responceLock.lock();
		try {
			this.response = response;
			responceLock.signal();
		} finally {
			responceLock.unlock();
		}
	}

	public String makeResponseMessage(String result) {
		Expression id = GLFactory.newExpression(GLFactory.newValue(this.actionID));
		Expression acion = GLFactory.newExpression(GLFactory.newGL("actionID", id));
		Expression actionResult = GLFactory.newExpression(GLFactory.newValue(result));
		GeneralizedList gl = GLFactory.newGL(this.getActionType().toString(), acion, actionResult);
		return gl.toString();
	}
	
	public String getResponse() {
		responceLock.lock();
		try {
			while(response == null) {
				try {
					responceLock.await();
				} catch(InterruptedException ignore) {}
			}
			String response = this.response;
			this.response = null;
			return response;
		} finally {
			responceLock.unlock();
		}
	}
	
	
	public String getActionID() {
		return this.actionID;
	}
	
	public abstract ActionType getActionType();
}
