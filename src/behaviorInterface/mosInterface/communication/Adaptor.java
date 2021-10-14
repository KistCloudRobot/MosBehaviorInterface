package behaviorInterface.mosInterface.communication;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import behaviorInterface.message.acknowledge.AckCancelMove;
import behaviorInterface.message.acknowledge.AckCharge;
import behaviorInterface.message.acknowledge.AckChargeStop;
import behaviorInterface.message.acknowledge.AckDoorClose;
import behaviorInterface.message.acknowledge.AckDoorOpen;
import behaviorInterface.message.acknowledge.AckEndCancelMove;
import behaviorInterface.message.acknowledge.AckEndCharge;
import behaviorInterface.message.acknowledge.AckEndChargeStop;
import behaviorInterface.message.acknowledge.AckEndDoorClose;
import behaviorInterface.message.acknowledge.AckEndDoorOpen;
import behaviorInterface.message.acknowledge.AckEndLoad;
import behaviorInterface.message.acknowledge.AckEndMove;
import behaviorInterface.message.acknowledge.AckEndPause;
import behaviorInterface.message.acknowledge.AckEndResume;
import behaviorInterface.message.acknowledge.AckEndUnload;
import behaviorInterface.message.acknowledge.AckLoad;
import behaviorInterface.message.acknowledge.AckMessage;
import behaviorInterface.message.acknowledge.AckMove;
import behaviorInterface.message.acknowledge.AckPause;
import behaviorInterface.message.acknowledge.AckResume;
import behaviorInterface.message.acknowledge.AckUnload;
import behaviorInterface.message.acknowledge.PersonCall;
import behaviorInterface.message.acknowledge.RTSR;
import behaviorInterface.message.request.ReqCancelMove;
import behaviorInterface.message.request.ReqCharge;
import behaviorInterface.message.request.ReqChargeStop;
import behaviorInterface.message.request.ReqDoorClose;
import behaviorInterface.message.request.ReqDoorOpen;
import behaviorInterface.message.request.ReqLoad;
import behaviorInterface.message.request.ReqMessage;
import behaviorInterface.message.request.ReqMove;
import behaviorInterface.message.request.ReqUnload;
import behaviorInterface.mosInterface.MosInterface;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import behaviorInterface.mosInterface.mosValue.RobotStatus;

public class Adaptor extends Thread {
	private final int nThread = 5;
	private ExecutorService messageThreadPool;
	
	RobotID robotID;
	MosInterface mi;
	int mosPort;
	boolean isLocal;

    private Socket socket;
    private OutputStream out;
    private InputStream in;
	
	private static final int HEADER_SIZE = 12;

	public Adaptor(RobotID robotID, MosInterface mi) {
		this.robotID = robotID;
		this.mi = mi;
		this.isLocal = false;
//		this.messageThreadPool = Executors.newFixedThreadPool(nThread);
	}
	
	public Adaptor(MosInterface mi) {
		this.robotID = null;
		this.mi = mi;
		this.isLocal = true;
//		this.messageThreadPool = Executors.newFixedThreadPool(nThread);
	}
	
	public void connect(String ip, int port) {
        try {
			socket = new Socket(ip, port);
	        out = socket.getOutputStream();
	        in = socket.getInputStream();
	        this.start();
		} catch (IOException e) {
			System.out.println("waiting...");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.connect(ip, port);
		}
	}
	
    private byte[] readByte(int length) throws IOException {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	DataOutputStream dos = new DataOutputStream(bos);
    	for(int i = 0; i < length; i++) {
    		dos.write(in.read());
    	}
    	dos.flush();
    	return bos.toByteArray();
    }
	
	@Override
	public void run() {
		while(true) {
			try {
				ByteBuffer byteBuffer = ByteBuffer.wrap(this.readByte(HEADER_SIZE)).order(ByteOrder.LITTLE_ENDIAN);
				StringBuilder sb = new StringBuilder();
				for(byte b : byteBuffer.array()) {
					sb.append(String.format("%02X ", b));
				}
				sb.append("\n");
				int protocolID = byteBuffer.getInt();
				int messageTypeID = byteBuffer.getInt();
				sb.append("messageTypeID : " + messageTypeID + "\n");
				MessageType messageType = MessageType.getEnum(messageTypeID);
				sb.append("messageType : " + messageType + "\n");
//				System.out.println(sb.toString());
				int packetSize = byteBuffer.getInt();
				byte[] packetData = this.readByte(packetSize - HEADER_SIZE);
				
//				MessageHandleTask task = new MessageHandleTask(protocolID, messageType, packetData);
//				messageThreadPool.execute(task);
				
				this.parseMessage(protocolID, messageType, packetData);
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class MessageHandleTask implements Runnable {
		private int protocolID;
		private MessageType messageType;
		private byte[] packetData;
		public MessageHandleTask(int protocolID, MessageType messageType, byte[] packetData) {
			this.protocolID = protocolID;
			this.messageType = messageType;
			this.packetData = packetData;
		}
		
		@Override
		public void run() {
			try {
				parseMessage(protocolID, messageType, packetData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private boolean isReceiver(int protocolID, MessageType messageType, int id) {
		if(protocolID == 40000) {
			switch(messageType) {
			case RTSR:
			case AckMove:
			case AckEndMove:
			case AckCancelMove:
			case AckEndCancelMove:
			case AckLoad:
			case AckEndLoad:
			case AckUnload:
			case AckEndUnload:
			case AckCharge:
			case AckEndCharge:
			case AckChargeStop:
			case AckEndChargeStop:
			case AckPause:
			case AckEndPause:
			case AckResume:
			case AckEndResume:
				RobotID robotID = RobotID.getEnum(id);
				return robotID == null ? false : this.robotID.equals(robotID);
			case AckDoorOpen:
			case AckEndDoorOpen:
			case AckDoorClose:
			case AckEndDoorClose:
			case AckPersonCall:
				return true;
			default:
				return false;
			}
		}
		else return false;
	}
	
	private void parseMessage(int protocolID, MessageType messageType, byte[] packetData) throws Exception {
		ByteBuffer byteBuffer = ByteBuffer.wrap(packetData).order(ByteOrder.LITTLE_ENDIAN);
		int id = byteBuffer.getInt();

		AckMessage ackMessage;
		RobotID robotID;
		DoorID doorID;
		RobotStatus robotStatus;
		int result;
		
		if(isLocal) {
			switch(messageType) {
			case RTSR:
				robotID = RobotID.getEnum(id);
				robotStatus = RobotStatus.getEnum(byteBuffer.getInt());
				int x = byteBuffer.getInt() / 1000;
				int y = byteBuffer.getInt() / 1000;
				int theta = byteBuffer.getInt();
				int speed = byteBuffer.getInt();
				int battery = byteBuffer.getInt();
				boolean loading = byteBuffer.get() != 0;
				ackMessage = new RTSR(robotID, robotStatus, x, y, theta, speed, battery, loading);
				this.mi.onMessage(ackMessage);
				break;
			case AckDoorOpen:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckDoorOpen(doorID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndDoorOpen:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndDoorOpen(doorID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckDoorClose:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckDoorClose(doorID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndDoorClose:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndDoorClose(doorID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckPersonCall:
				int locationID = id;
				int callID = byteBuffer.getInt();
				ackMessage = new PersonCall(locationID, callID);
				this.mi.onMessage(ackMessage);
				break;
			default:
				break;
			}
		}
		else if(isReceiver(protocolID, messageType, id)) {
			switch(messageType) {
			case RTSR:
				robotID = RobotID.getEnum(id);
				robotStatus = RobotStatus.getEnum(byteBuffer.getInt());
				int x = (int) (byteBuffer.getInt() & 0xffffffffL) / 1000;
				int y = (int) (byteBuffer.getInt() & 0xffffffffL) / 1000;
				int theta = (int) (byteBuffer.getInt() & 0xffffffffL);
				int speed = (int) (byteBuffer.getInt() & 0xffffffffL);
				int battery = (int) (byteBuffer.getInt() & 0xffffffffL);
				boolean loading = byteBuffer.get() != 0;
				ackMessage = new RTSR(robotID, robotStatus, x, y, theta, speed, battery, loading);
				this.mi.onMessage(ackMessage);
				break;
			case AckMove:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckMove(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndMove:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndMove(robotID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckCancelMove:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckCancelMove(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndCancelMove:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndCancelMove(robotID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckLoad:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckLoad(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndLoad:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndLoad(robotID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckUnload:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckUnload(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndUnload:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndUnload(robotID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckCharge:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckCharge(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndCharge:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndCharge(robotID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckChargeStop:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckChargeStop(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndChargeStop:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndChargeStop(robotID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckPause:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckPause(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndPause:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckEndPause(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckResume:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckResume(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndResume:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckEndResume(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckDoorOpen:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckDoorOpen(doorID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndDoorOpen:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndDoorOpen(doorID, result);
				this.mi.onMessage(ackMessage);
				break;
			case AckDoorClose:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckDoorClose(doorID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndDoorClose:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackMessage = new AckEndDoorClose(doorID, result);
				this.mi.onMessage(ackMessage);
				break;
			default:
				break;
			}
		}
		else {
			return;
		}
	}
	
	private void send(ByteBuffer bf) {
		try {
			byte[] data = bf.array();
			out.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(ReqMessage message) {
		MessageType messageType = message.getType();
		
		ByteBuffer bf;
		int packetSize;
		int nodeID;
		DoorID doorID;
		
		switch(messageType) {
		case ReqMove:
			ReqMove reqMove = (ReqMove)message;
			int pathSize = reqMove.getPathSize();
			packetSize = 20 + 4 * pathSize;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			bf.putInt(pathSize);
			for(Integer i : reqMove.getPath()) {
				bf.putInt(i);
			}
			send(bf);
			break;
		case ReqCancelMove:
			ReqCancelMove cancelMove = (ReqCancelMove)message;
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			send(bf);
			break;
		case ReqLoad:
			ReqLoad reqLoad = (ReqLoad)message;
			packetSize = 20;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqLoad.getNodeID();
			bf.putInt(nodeID);
			send(bf);
			break;
		case ReqUnload:
			ReqUnload reqUnload = (ReqUnload)message;
			packetSize = 20;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqUnload.getNodeID();
			bf.putInt(nodeID);
			send(bf);
			break;
		case ReqCharge:
			ReqCharge reqCharge = (ReqCharge)message;
			packetSize = 20;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqCharge.getNodeID();
			bf.putInt(nodeID);
			send(bf);
			break;
		case ReqChargeStop:
			ReqChargeStop reqChargeStop = (ReqChargeStop)message;
			packetSize = 20;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqChargeStop.getNodeID();
			bf.putInt(nodeID);
			send(bf);
			break;
		case ReqPause:
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			send(bf);
			break;
		case ReqResume:
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			send(bf);
			break;
		case ReqDoorOpen:
			ReqDoorOpen reqDoorOpen = (ReqDoorOpen)message;
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			doorID = reqDoorOpen.getDoorID();
			bf.putInt(doorID.getValue());
			send(bf);
			break;
		case ReqDoorClose:
			ReqDoorClose reqDoorClose = (ReqDoorClose)message;
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			doorID = reqDoorClose.getDoorID();
			bf.putInt(doorID.getValue());
			send(bf);
			break;
		default:
			System.out.println("what??");
			break;
		}
	}

}
