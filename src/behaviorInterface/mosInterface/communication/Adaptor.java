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
import behaviorInterface.message.acknowledge.AckEndFlatPreciseMove;
import behaviorInterface.message.acknowledge.AckEndGuideMove;
import behaviorInterface.message.acknowledge.AckEndLoad;
import behaviorInterface.message.acknowledge.AckEndLogin;
import behaviorInterface.message.acknowledge.AckEndMessage;
import behaviorInterface.message.acknowledge.AckEndMove;
import behaviorInterface.message.acknowledge.AckEndPalletizerStart;
import behaviorInterface.message.acknowledge.AckEndPalletizerStop;
import behaviorInterface.message.acknowledge.AckEndPause;
import behaviorInterface.message.acknowledge.AckEndPreciseMove;
import behaviorInterface.message.acknowledge.AckEndResume;
import behaviorInterface.message.acknowledge.AckEndStraightBackMove;
import behaviorInterface.message.acknowledge.AckEndUnload;
import behaviorInterface.message.acknowledge.AckFlatPreciseMove;
import behaviorInterface.message.acknowledge.AckGuideMove;
import behaviorInterface.message.acknowledge.AckLoad;
import behaviorInterface.message.acknowledge.AckLogin;
import behaviorInterface.message.acknowledge.AckMessage;
import behaviorInterface.message.acknowledge.AckMove;
import behaviorInterface.message.acknowledge.AckPalletizerStart;
import behaviorInterface.message.acknowledge.AckPalletizerStop;
import behaviorInterface.message.acknowledge.AckPause;
import behaviorInterface.message.acknowledge.AckPreciseMove;
import behaviorInterface.message.acknowledge.AckResume;
import behaviorInterface.message.acknowledge.AckStraightBackMove;
import behaviorInterface.message.acknowledge.AckUnload;
import behaviorInterface.message.acknowledge.PalletizerPackingFinish;
import behaviorInterface.message.acknowledge.PalletizerReleasingFinish;
import behaviorInterface.message.acknowledge.PersonCall;
import behaviorInterface.message.acknowledge.RTSR;
import behaviorInterface.message.request.ReqCancelMove;
import behaviorInterface.message.request.ReqCharge;
import behaviorInterface.message.request.ReqChargeStop;
import behaviorInterface.message.request.ReqDoorClose;
import behaviorInterface.message.request.ReqDoorOpen;
import behaviorInterface.message.request.ReqEnterPalletizer;
import behaviorInterface.message.request.ReqExitPalletizer;
import behaviorInterface.message.request.ReqFlatPreciseMove;
import behaviorInterface.message.request.ReqGuideMove;
import behaviorInterface.message.request.ReqLoad;
import behaviorInterface.message.request.ReqLogin;
import behaviorInterface.message.request.ReqMessage;
import behaviorInterface.message.request.ReqMove;
import behaviorInterface.message.request.ReqPalletizerStart;
import behaviorInterface.message.request.ReqPalletizerStop;
import behaviorInterface.message.request.ReqPreciseMove;
import behaviorInterface.message.request.ReqStraightBackMove;
import behaviorInterface.message.request.ReqUnload;
import behaviorInterface.mosInterface.MosInterface;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.RobotID;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotStatus;

public class Adaptor extends Thread {
	private final int nThread = 5;
	private ExecutorService messageThreadPool;
	
	RobotID robotID;
	MosInterface mi;
	int mosPort;
	boolean isLocal;

	private String ip;
	private int port;
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
		this.ip = ip;
		this.port = port;
		while(true) {
	        try {
				socket = new Socket(ip, port);
		        out = socket.getOutputStream();
		        in = socket.getInputStream();
		        this.start();
		        if(this.socket.isConnected()) {
		    		System.out.println("MOS connected : " + ip + ":" + port);
		        	break;
		        }
		        else {
					System.out.println("waiting...");
					Thread.sleep(5000);
					continue;
		        }
			} catch (IOException e) {
				System.out.println("waiting...");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
//				if(messageType != MessageType.RTSR) {
//					System.out.println(sb.toString());
//				}	
				if(messageType == null) {
					System.out.println("undefined message type : " + Integer.toHexString(messageTypeID));
					this.socket.close();
					break;
				}
				else {
//					sb.append("messageType : " + messageType + "\n");
//					if(messageType != MessageType.RTSR) {
//						System.out.println(sb.toString());
//					}
					int packetSize = byteBuffer.getInt();
					byte[] packetData = this.readByte(packetSize - HEADER_SIZE);
					
//					MessageHandleTask task = new MessageHandleTask(protocolID, messageType, packetData);
//					messageThreadPool.execute(task);
					
					this.parseMessage(protocolID, messageType, packetData);
				}
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
			case PalletizerPackingFinish:
			case PalletizerReleasingFinish:
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
			case AckGuideMove:
			case AckEndGuideMove:
			case AckPreciseMove:
			case AckEndPreciseMove:
			case AckFlatPreciseMove:
			case AckEndFlatPreciseMove:
			case AckStraightBackMove:
			case AckEndStraightBackMove:
			case AckLogin:
			case AckEndLogin:
			case AckPalletizerStart:
			case AckEndPalletizerStart:
			case AckPalletizerStop:
			case AckEndPalletizerStop:
				if(this.robotID != null) {
					RobotID robotID = RobotID.getEnum(id);
					return this.robotID.equals(robotID);
				}
				else return false;
			case AckDoorOpen:
			case AckEndDoorOpen:
			case AckDoorClose:
			case AckEndDoorClose:
				if(this.isLocal) return true;
				else return false;
			case PersonCall:
				return true;
			default:
				return false;
			}
		}
		else return false;
	}
	
	private void parseMessage(int protocolID, MessageType messageType, byte[] packetData) throws Exception {
//		System.out.println("[onMsg]\t: " + messageType.toString());
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(packetData).order(ByteOrder.LITTLE_ENDIAN);
		int id = byteBuffer.getInt();

		AckMessage ackMessage;
		AckEndMessage ackEndMessage;
		RobotID robotID;
		RobotID palletizerID;
		DoorID doorID;
		RobotStatus robotStatus;
		int result;
		int nodeID;

		if(isLocal) {
			switch(messageType) {
			case RTSR:
				robotID = RobotID.getEnum(id);
				robotStatus = RobotStatus.getEnum(byteBuffer.getInt());
				float x = byteBuffer.getInt();
				float y = byteBuffer.getInt();
				x = x / 1000;
				y = y / 1000;
				int theta = byteBuffer.getInt();
				int speed = byteBuffer.getInt();
				int battery = byteBuffer.getInt();
				boolean loading = byteBuffer.get() != 0;
				ackMessage = new RTSR(robotID, robotStatus, x, y, theta, speed, battery, loading);
				this.mi.onMessage(ackMessage);
				break;
			case AckDoorOpen:
				doorID = DoorID.getEnum(id);
				ackMessage = new AckDoorOpen(doorID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndDoorOpen:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndDoorOpen(doorID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckDoorClose:
				doorID = DoorID.getEnum(id);
				ackMessage = new AckDoorClose(doorID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndDoorClose:
				doorID = DoorID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndDoorClose(doorID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckLogin:
				robotID = RobotID.getEnum(id);
				if(robotID == RobotID.LOCAL) {
					ackMessage = new AckLogin(robotID);
					this.mi.onMessage(ackMessage);
				}
				break;
			case AckEndLogin:
				result = byteBuffer.getInt();
				robotID = RobotID.getEnum(id);
				if(robotID == RobotID.LOCAL) {
					ackEndMessage = new AckEndLogin(robotID, result);
					this.mi.onMessage(ackEndMessage);
				}
				break;
			default:
				break;
			}
		}
		else if(isReceiver(protocolID, messageType, id)) {
			if(messageType != MessageType.RTSR) {
				System.out.println("[" + this.robotID + "] " + messageType.toString() + "timestamp :" + System.currentTimeMillis());
			}
			switch(messageType) {
			case RTSR:
				robotID = RobotID.getEnum(id);
				robotStatus = RobotStatus.getEnum(byteBuffer.getInt());
				float x = byteBuffer.getInt();
				float y = byteBuffer.getInt();
				x = x / 1000;
				y = y / 1000;
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
				ackEndMessage = new AckEndMove(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckCancelMove:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckCancelMove(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndCancelMove:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndCancelMove(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckLoad:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckLoad(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndLoad:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndLoad(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckUnload:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckUnload(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndUnload:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndUnload(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckCharge:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckCharge(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndCharge:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndCharge(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckChargeStop:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckChargeStop(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndChargeStop:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndChargeStop(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckPause:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckPause(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndPause:
				robotID = RobotID.getEnum(id);
				ackEndMessage = new AckEndPause(robotID);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckResume:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckResume(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndResume:
				robotID = RobotID.getEnum(id);
				ackEndMessage = new AckEndResume(robotID);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckLogin:
				ackMessage = new AckLogin(RobotID.getEnum(id));
				this.mi.onMessage(ackMessage);
				break;
			case AckEndLogin:
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndLogin(RobotID.getEnum(id), result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckGuideMove:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckGuideMove(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndGuideMove:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndGuideMove(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckPreciseMove:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckPreciseMove(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndPreciseMove:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndPreciseMove(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckFlatPreciseMove:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckFlatPreciseMove(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndFlatPreciseMove:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndFlatPreciseMove(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckStraightBackMove:
				robotID = RobotID.getEnum(id);
				ackMessage = new AckStraightBackMove(robotID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndStraightBackMove:
				robotID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndStraightBackMove(robotID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckPalletizerStart:
				palletizerID = RobotID.getEnum(id);
				ackMessage = new AckPalletizerStart(palletizerID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndPalletizerStart:
				palletizerID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndPalletizerStart(palletizerID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case AckPalletizerStop:
				palletizerID = RobotID.getEnum(id);
				ackMessage = new AckPalletizerStop(palletizerID);
				this.mi.onMessage(ackMessage);
				break;
			case AckEndPalletizerStop:
				palletizerID = RobotID.getEnum(id);
				result = byteBuffer.getInt();
				ackEndMessage = new AckEndPalletizerStop(palletizerID, result);
				this.mi.onMessage(ackEndMessage);
				break;
			case PalletizerPackingFinish:
				palletizerID = RobotID.getEnum(id);
				nodeID = byteBuffer.getInt();
				ackMessage = new PalletizerPackingFinish(palletizerID, nodeID);
				this.mi.onMessage(ackMessage);
				break;
			case PalletizerReleasingFinish:
				palletizerID = RobotID.getEnum(id);
				nodeID = byteBuffer.getInt();
				ackMessage = new PalletizerReleasingFinish(palletizerID, nodeID);
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
//			System.out.print("send message : ");
//			for(byte b : data) {
//				System.out.print(String.format("%02X ", b));
//			}
//			System.out.println();
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
		int direction;
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
//			System.out.print("move packet : ");
//			for(byte b : bf.array()) {
//				System.out.print(String.format("%02X ", b));
//			}
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
		case ReqLogin:
			ReqLogin reqLogin = (ReqLogin)message;
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(reqLogin.getLoginID().getValue());
			send(bf);
			break;
		case ReqGuideMove:
			ReqGuideMove reqGuideMove = (ReqGuideMove)message;
			packetSize = 24;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqGuideMove.getNodeID();
			bf.putInt(nodeID);
			System.out.println(reqGuideMove.getDirection());
			System.out.println(reqGuideMove.getDirection().getValue());
			direction = reqGuideMove.getDirection().getValue();
			bf.putInt(direction);
			send(bf);
			break;
		case ReqPreciseMove:
			ReqPreciseMove reqPreciseMove = (ReqPreciseMove)message;
			packetSize = 20;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqPreciseMove.getNodeID();
			bf.putInt(nodeID);
			send(bf);
			break;
		case ReqFlatPreciseMove:
			ReqFlatPreciseMove reqflatPreciseMove = (ReqFlatPreciseMove)message;
			packetSize = 20;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqflatPreciseMove.getNodeID();
			bf.putInt(nodeID);
			send(bf);
			break;
		case ReqStraightBackMove:
			ReqStraightBackMove reqStraightBackMove = (ReqStraightBackMove)message;
			packetSize = 20;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			nodeID = reqStraightBackMove.getNodeID();
			bf.putInt(nodeID);
			send(bf);
			break;
		case ReqPalletizerStart:
			ReqPalletizerStart reqPalletizerStart = (ReqPalletizerStart)message;
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			send(bf);
			break;
		case ReqPalletizerStop:
			ReqPalletizerStop reqPalletizerStop = (ReqPalletizerStop)message;
			packetSize = 16;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			send(bf);
			break;
		case ReqEnterPalletizer:
			ReqEnterPalletizer reqEnterPalletizer = (ReqEnterPalletizer)message;
			packetSize = 24;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			bf.putInt(reqEnterPalletizer.getRobotID().getValue());
			bf.putInt(reqEnterPalletizer.getNodeID());
			send(bf);
			break;
		case ReqExitPalletizer:
			ReqExitPalletizer reqExitPalletizer = (ReqExitPalletizer)message;
			packetSize = 24;
			
			bf = ByteBuffer.allocate(packetSize).order(ByteOrder.LITTLE_ENDIAN);
			bf.putInt(30000);
			bf.putInt(messageType.getValue());
			bf.putInt(packetSize);
			bf.putInt(robotID.getValue());
			bf.putInt(reqExitPalletizer.getRobotID().getValue());
			bf.putInt(reqExitPalletizer.getNodeID());
			send(bf);
			break;
		default:
			System.out.println("what??");
			break;
		}
	}

}
