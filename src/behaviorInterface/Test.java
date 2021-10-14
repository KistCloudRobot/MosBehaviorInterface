package behaviorInterface;

import java.util.List;
import java.util.UUID;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;


public class Test {
	private static class TestAgent extends ArbiAgent implements Runnable {
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
				new MoveTask(201, 213, 205, 206);
				Thread.sleep(5000);
				new PauseTask();
				Thread.sleep(5000);
				new MoveTask(213, 205, 206);
				Thread.sleep(1000);
				new PauseTask();
				Thread.sleep(5000);
				new ResumeTask();
				Thread.sleep(5000);
				new PauseTask();
				Thread.sleep(5000);
				new ResumeTask();
				Thread.sleep(5000);
				new CancelMoveTask();
				Thread.sleep(5000);
				new MoveTask(206, 207, 208);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		ArbiAgentExecutor.execute("tcp://127.0.0.1:61116", "agent://www.arbi.com/Lift1/TaskManager", new TestAgent("Lift1"), 2);
	}
}
