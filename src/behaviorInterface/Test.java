package behaviorInterface;

import java.util.List;
import java.util.UUID;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;


public class Test {
	private static class TestAgent extends ArbiAgent implements Runnable {
		private final static String LocalBehaviorInterfaceURI = "agent://www.arbi.com/Local/BehaviorInterface";
		private String behaviorInterfaceURI;
		
		private class MoveTask extends Thread {
			private int[] paths;
			
			public MoveTask(int... paths) {
				this.paths = paths;
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(move (actionID \"" + UUID.randomUUID().toString() + "\") (path";
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
				String reqMsg = "(cancelMove (actionID \"" + UUID.randomUUID().toString() + "\"))";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class PauseTask extends Thread {
			public PauseTask() {
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(pause (actionID \"" + UUID.randomUUID().toString() + "\"))";
				System.out.println("response : " + request(behaviorInterfaceURI, reqMsg));
			}
		}
		
		private class ResumeTask extends Thread {
			public ResumeTask() {
				this.start();
			}
			
			@Override
			public void run() {
				String reqMsg = "(resume (actionID \"" + UUID.randomUUID().toString() + "\"))";
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
				String reqMsg = "(doorOpen (actionID \"" + UUID.randomUUID().toString() + "\") " + this.nodeID + ")";
				System.out.println("response : " + request(LocalBehaviorInterfaceURI, reqMsg));
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
				String reqMsg = "(doorOpen (actionID \"" + UUID.randomUUID().toString() + "\") \"" + this.doorID + "\")";
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
				String reqMsg = "(doorClose (actionID \"" + UUID.randomUUID().toString() + "\") \"" + this.doorID + "\")";
				System.out.println("response : " + request(LocalBehaviorInterfaceURI, reqMsg));
			}
		}
			
		public TestAgent(String brokerName) {
			behaviorInterfaceURI = "agent://www.arbi.com/" + brokerName + "/BehaviorInterface";
		}
		
		@Override
		public void onStart() {
			super.onStart();
			Thread t = new Thread(this);
			t.start();
		}
		
		@Override
		public void onData(String sender, String data) {
			System.out.println("data : " + data);
		}

		@Override
		public void run() {
			try {
				new MoveTask(204, 232, 230);
//				Thread.sleep(5000);
//				new PauseTask();
//				Thread.sleep(5000);
//				new MoveTask(213, 205, 206);
//				Thread.sleep(1000);
//				new PauseTask();
//				Thread.sleep(5000);
//				new ResumeTask();
//				Thread.sleep(5000);
//				new PauseTask();
//				Thread.sleep(5000);
//				new ResumeTask();
//				Thread.sleep(10000);
//				new DoorCloseTask("Door1");
//				Thread.sleep(5000);
//				Thread.sleep(5000);
//				new LoadTask(23);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://172.16.165.171:61412", "agent://www.arbi.com/Tow2/TaskManager", new TestAgent("Tow2"), 2);
	}
}
