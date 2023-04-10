package behaviorInterface.test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import behaviorInterface.test.TestAgentLift1.TestAgent;
import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;


public class TestAgentLift4 {
	private static class TestAgent extends ArbiAgent implements Runnable {
		private final static String LocalBehaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";
		private String behaviorInterfaceURI;

		private final ResponseLock responceLock;
		private String response;
		
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
		
		public String getResponse() {
			responceLock.lock();
			try {
				while(this.response == null) {
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
		
		public TestAgent(String brokerName) {
			this.behaviorInterfaceURI = "agent://www.arbi.com/BehaviorInterface";
			this.responceLock = new ResponseLock();
			this.response = null;
		}
		
		@Override
		public void onData(String sender, String data) {
			System.out.println("data : " + data);
			this.setResponse(data);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			Thread t = new Thread(this);
			t.start();
		}
		

		private class MoveTask extends Thread {
			private int[] paths;
			
			public MoveTask(int... paths) {
				this.paths = paths;
				this.start();
			}
			
			@Override
			public void run() {
				System.out.println("start!");
				String reqMsg = "(Move \"" + UUID.randomUUID().toString() + "\" (path";
				for(int path : paths) {
					reqMsg += " " + path;
				}
				reqMsg += "))";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class CancelMoveTask extends Thread {
			public CancelMoveTask() {
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(CancelMove \"" + UUID.randomUUID().toString() + "\")";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class PauseTask extends Thread {
			public PauseTask() {
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(Pause (actionID \"" + UUID.randomUUID().toString() + "\"))";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class ResumeTask extends Thread {
			public ResumeTask() {
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(Resume (actionID \"" + UUID.randomUUID().toString() + "\"))";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
	
		private class LoadTask extends Thread {
			private int nodeID;
			
			public LoadTask(int nodeID) {
				this.nodeID = nodeID;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(Load \"" + UUID.randomUUID().toString() + "\" " + this.nodeID + ")";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
	
		private class UnloadTask extends Thread {
			private int nodeID;
			
			public UnloadTask(int nodeID) {
				this.nodeID = nodeID;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(Unload \"" + UUID.randomUUID().toString() + "\" " + this.nodeID + ")";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class DoorOpenTask extends Thread {
			private String doorID;
			
			public DoorOpenTask(String doorID) {
				this.doorID = doorID;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(DoorOpen (actionID \"" + UUID.randomUUID().toString() + "\") \"" + this.doorID + "\")";
				System.out.println("response : " + request(LocalBehaviorInterfaceURI, reqMsg));
			}
		}
		
		private class DoorCloseTask extends Thread {
			private String doorID;
			
			public DoorCloseTask(String doorID) {
				this.doorID = doorID;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(DoorClose (actionID \"" + UUID.randomUUID().toString() + "\") \"" + this.doorID + "\")";
				System.out.println("response : " + request(LocalBehaviorInterfaceURI, reqMsg));
			}
		}
		
		private class GuideMoveTask extends Thread {
			private int node;
			private String direction;
			
			public GuideMoveTask(int node, String direction) {
				this.node = node;
				this.direction = direction;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(GuideMove \"" + UUID.randomUUID().toString() + "\" " + this.node + " \"" + this.direction + "\")";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class PreciseMoveTask extends Thread {
			private int node;
			private String direction;
			
			public PreciseMoveTask(int node) {
				this.node = node;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(PreciseMove \"" + UUID.randomUUID().toString() + "\" " + this.node + ")";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class StraightBackMoveTask extends Thread {
			private int node;
			private String direction;
			
			public StraightBackMoveTask(int node) {
				this.node = node;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(StraightBackMove \"" + UUID.randomUUID().toString() + "\" " + this.node + ")";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}


		@Override
		public void run() {
			try {
				System.out.println("start!");
				new MoveTask(121, 122, 124, 125, 126, 127, 128);
				System.out.println("action result : " + this.getResponse());
				new PreciseMoveTask(49);
				System.out.println("action result : " + this.getResponse());
				new LoadTask(49);
				System.out.println("action result : " + this.getResponse());
				new StraightBackMoveTask(128);
				System.out.println("action result : " + this.getResponse());
				new MoveTask(127, 126, 125, 124, 122, 121, 158);
				System.out.println("action result : " + this.getResponse());
//				new MoveTask(120, 119, 118, 117, 116, 115);
//				System.out.println("action result : " + this.getResponse());
//				new PreciseMoveTask(20);
//				System.out.println("action result : " + this.getResponse());
//				new LoadTask(20);
//				System.out.println("action result : " + this.getResponse());
//				new StraightBackMoveTask(115);
//				System.out.println("action result : " + this.getResponse());
//				new MoveTask(114, 113, 112, 111, 109, 107, 106, 105, 104, 103, 102, 101, 142, 146);
//				System.out.println("action result : " + this.getResponse());
//				new UnloadTask(26);
//				System.out.println("action result : " + this.getResponse());
//				new StraightBackMoveTask(142);
//				System.out.println("action result : " + this.getResponse());
//				new MoveTask(101, 102, 103, 104, 105, 106, 107, 109, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 156);
//				System.out.println("action result : " + this.getResponse());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("172.16.165.164", 61113,"agent://www.arbi.com/Test", new TestAgent("Lift4"), BrokerType.ACTIVEMQ);
	}
}
