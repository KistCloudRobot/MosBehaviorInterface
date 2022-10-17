package behaviorInterface.test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kr.ac.uos.ai.arbi.BrokerType;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.ArbiAgentMessage;


public class TestAgentLift1 {
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
				String reqMsg = "(Pause \"" + UUID.randomUUID().toString() + "\")";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class ResumeTask extends Thread {
			public ResumeTask() {
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(Resume \"" + UUID.randomUUID().toString() + "\")";
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
				String reqMsg = "(DoorOpen \"" + UUID.randomUUID().toString() + "\" \"" + this.doorID + "\")";
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
				String reqMsg = "(DoorClose \"" + UUID.randomUUID().toString() + "\" \"" + this.doorID + "\")";
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
				new MoveTask(100, 101, 142, 141, 151, 152);
				Thread.sleep(5000);
				new CancelMoveTask();
				System.out.println("action result : " + this.getResponse());
				new MoveTask(100, 101, 142, 141, 151, 152);
				System.out.println("action result : " + this.getResponse());
				new GuideMoveTask(1, "Forward");
				System.out.println("action result : " + this.getResponse());
				new LoadTask(1);
				System.out.println("action result : " + this.getResponse());
				new StraightBackMoveTask(152);
				System.out.println("action result : " + this.getResponse());
				new MoveTask(151, 141, 140, 139, 138);
				System.out.println("action result : " + this.getResponse());
				new PreciseMoveTask(26);
				System.out.println("action result : " + this.getResponse());
				new UnloadTask(26);
				System.out.println("action result : " + this.getResponse());
				new StraightBackMoveTask(138);
				System.out.println("action result : " + this.getResponse());
				new MoveTask(139, 140, 141, 142, 101, 100, 143);
				System.out.println("action result : " + this.getResponse());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://172.16.165.141:61116", "agent://www.arbi.com/Test", new TestAgent("Lift1"), BrokerType.ZEROMQ);
	}
}
