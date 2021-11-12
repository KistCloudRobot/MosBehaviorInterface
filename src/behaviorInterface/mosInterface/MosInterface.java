package behaviorInterface.mosInterface;

import java.io.IOException;
import java.util.List;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.message.acknowledge.AckEndMessage;
import behaviorInterface.message.acknowledge.AckMessage;
import behaviorInterface.message.acknowledge.PersonCall;
import behaviorInterface.message.acknowledge.RTSR;
import behaviorInterface.message.request.ReqCancelMove;
import behaviorInterface.message.request.ReqCharge;
import behaviorInterface.message.request.ReqChargeStop;
import behaviorInterface.message.request.ReqDoorClose;
import behaviorInterface.message.request.ReqDoorOpen;
import behaviorInterface.message.request.ReqLoad;
import behaviorInterface.message.request.ReqLogin;
import behaviorInterface.message.request.ReqMessage;
import behaviorInterface.message.request.ReqMove;
import behaviorInterface.message.request.ReqPause;
import behaviorInterface.message.request.ReqResume;
import behaviorInterface.message.request.ReqUnload;
import behaviorInterface.mosInterface.communication.Adaptor;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.LoginID;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class MosInterface {
	RobotID robotID;
	Adaptor adaptor;
	BehaviorInterface behaviorInterface;
	ActionType currentActionType;
	ReqMessage waitingResponse;
	ReqMessage pausedWaitingResponse;
	boolean isLogin;
	
	public MosInterface(int robotID, ArbiAgent agent) {
		this(RobotID.getEnum(robotID), agent);
	}
	
	public MosInterface(String robotID, ArbiAgent agent) {
		this(RobotID.valueOf(robotID), agent);
	}
	
	public MosInterface(RobotID robotID, ArbiAgent agent) {
		this.robotID = robotID;
		this.behaviorInterface = (BehaviorInterface)agent;
		this.adaptor = new Adaptor(this.robotID, this);
		this.currentActionType = null;
		this.waitingResponse = null;
		this.pausedWaitingResponse = null;
		this.isLogin = false;
	}
	
	public MosInterface(ArbiAgent agent) {
		this.robotID = null;
		this.behaviorInterface = (BehaviorInterface)agent;
		this.adaptor = new Adaptor(this);
		this.currentActionType = null;
		this.waitingResponse = null;
		this.pausedWaitingResponse = null;
		this.isLogin = false;
	}
	
	public void connect(String address, int port) throws IOException {
		this.adaptor.connect(address, port);
	}
	
	public void onMessage(AckMessage message) throws Exception {
		MessageType messageType = message.getType();
		int resultValue;
		String result;
		AckEndMessage ackEndMessage;
		switch(messageType) {
		case RTSR:
			RTSR rtsr = (RTSR)message;
			String loading;
			if(rtsr.isLoading()) loading = "Loading";
			else loading = "Unloading";
			behaviorInterface.onRTSR(rtsr.getRobotID().toString(), rtsr.getRobotStatus().toString(), rtsr.getX(), rtsr.getY(), rtsr.getTheta(), rtsr.getSpeed(), rtsr.getBattery(), loading);
			break;
		case AckMove:
		case AckCancelMove:
		case AckPause:
		case AckResume:
		case AckLogin:
			if(this.robotID == null) {
				System.out.println("[LOCAL] " + messageType);
			}
			else {
				System.out.println("[" + robotID.toString() + "] " + messageType);
			}
			break;
		case AckLoad:
		case AckUnload:
		case AckCharge:
		case AckChargeStop:
		case AckDoorOpen:
		case AckDoorClose:
		case AckEndMove:
		case AckEndCancelMove:
		case AckEndPause:
		case AckEndResume:
			if(this.robotID == null) {
				System.out.println("[LOCAL] " + messageType);
			}
			else {
				System.out.println("[" + robotID.toString() + "] " + messageType);
			}
			this.waitingResponse.setResponse(message);
			break;
		case AckEndLoad:
		case AckEndUnload:
		case AckEndCharge:
		case AckEndChargeStop:
		case AckEndDoorOpen:
		case AckEndDoorClose:
			if(this.robotID == null) {
				System.out.println("[LOCAL] " + messageType);
			}
			else {
				System.out.println("[" + robotID.toString() + "] " + messageType);
			}
			ackEndMessage = (AckEndMessage)message;
			resultValue = ackEndMessage.getResult();
			result = this.getResult(resultValue);
			this.waitingResponse.setResponse(message);
			this.behaviorInterface.sendMessageToTM(this.waitingResponse.getResponse());
			this.currentActionType = null;
			this.waitingResponse = null;
			break;
		case AckEndLogin:
			this.waitingResponse.setResponse(message);
			break;
		case PersonCall:
			if(this.robotID == null) {
				System.out.println("[LOCAL] " + messageType);
			}
			else {
				System.out.println("[" + robotID.toString() + "] " + messageType);
			}
			PersonCall personCallMessage = (PersonCall)message;
			int locationID = personCallMessage.getLocationID();
			int callID = personCallMessage.getCallID();
			this.behaviorInterface.sendPersonCall(locationID, callID);
			this.waitingResponse = null;
			break;
		default:
			System.out.println("[" + robotID.toString() + "] " + messageType + " error");
		}
	}
	
	private String getResult(int resultValue) {
		switch(resultValue) {
		case 0:
			return "success";
		default:
			return "error : " + resultValue;
		}
	}
	
	public String login(LoginID mcArbiID) {
		if(!this.isLogin) {
			this.waitingResponse = new ReqLogin(mcArbiID);
			this.adaptor.send(this.waitingResponse);
			String response = this.waitingResponse.getResponse();
			this.waitingResponse = null;
			this.currentActionType = null;
			if(response == "success") {
				this.isLogin = true;
				return "(ok)";
			}
			else {
				System.out.println("login failed --> " + response);
			}
		}
		return "(fail)";
	}
	
	public String move(String actionID, int pathSize, List<Integer> path) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqMove(actionID, this.robotID, pathSize, path);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			String response = this.waitingResponse.getResponse();
			this.waitingResponse = null;
			this.currentActionType = null;
			return response;
		}
		else {
			return "(fail)";
		}
	}
	
	public String cancelMove(String actionID) throws Exception {
		if(this.isLogin && this.currentActionType == ActionType.move) {
			this.waitingResponse = new ReqCancelMove(actionID, this.robotID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			String response = this.waitingResponse.getResponse();
			this.waitingResponse = null;
			this.currentActionType = null;
			return response;
		}
		else if(this.currentActionType == null) {
			Expression id = GLFactory.newExpression(GLFactory.newValue(actionID));
			Expression acion = GLFactory.newExpression(GLFactory.newGL("actionID", id));
			Expression actionResult = GLFactory.newExpression(GLFactory.newValue("success"));
			GeneralizedList gl = GLFactory.newGL(ActionType.cancelMove.toString(), acion, actionResult);
			return gl.toString();
		}
		else {
			return "(fail)";
		}
	}
	
	public String load(String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqLoad(actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String unload(String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqUnload(actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String charge(String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqCharge(actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String chargeStop(String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqChargeStop(actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String pause(String actionID) throws Exception {
		if(this.isLogin && this.currentActionType != ActionType.pause && this.currentActionType != null) {
			this.pausedWaitingResponse = this.waitingResponse;
			this.waitingResponse = new ReqPause(actionID, this.robotID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			String response = this.waitingResponse.getResponse();
			this.waitingResponse = null;
			return response;
		}
		else {
			return "(fail)";
		}
	}
	
	public String resume(String actionID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == ActionType.pause) {
			this.waitingResponse = new ReqResume(actionID, this.robotID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			String response = this.waitingResponse.getResponse();
			this.waitingResponse = this.pausedWaitingResponse;
			this.currentActionType = this.pausedWaitingResponse.getActionType();
			this.pausedWaitingResponse = null;
			return response;
		}
		else {
			return "(fail)";
		}
	}
	
	public String doorOpen(String actionID, String doorID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqDoorOpen(actionID, DoorID.valueOf(doorID));
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String doorClose(String actionID, String doorID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqDoorClose(actionID, DoorID.valueOf(doorID));
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
}
