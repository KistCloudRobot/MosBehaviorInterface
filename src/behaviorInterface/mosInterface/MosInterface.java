package behaviorInterface.mosInterface;

import java.io.IOException;
import java.util.List;

import behaviorInterface.BehaviorInterface;
import behaviorInterface.message.acknowledge.AckEndMove;
import behaviorInterface.message.acknowledge.AckMessage;
import behaviorInterface.message.acknowledge.PalletizerPackingFinish;
import behaviorInterface.message.acknowledge.PersonCall;
import behaviorInterface.message.acknowledge.RTSR;
import behaviorInterface.message.request.ReqCancelMove;
import behaviorInterface.message.request.ReqCharge;
import behaviorInterface.message.request.ReqChargeStop;
import behaviorInterface.message.request.ReqDoorClose;
import behaviorInterface.message.request.ReqDoorOpen;
import behaviorInterface.message.request.ReqEnterPalletizer;
import behaviorInterface.message.request.ReqExitPalletizer;
import behaviorInterface.message.request.ReqGuideMove;
import behaviorInterface.message.request.ReqLoad;
import behaviorInterface.message.request.ReqLogin;
import behaviorInterface.message.request.ReqMessage;
import behaviorInterface.message.request.ReqMove;
import behaviorInterface.message.request.ReqPalletizerStart;
import behaviorInterface.message.request.ReqPalletizerStop;
import behaviorInterface.message.request.ReqPause;
import behaviorInterface.message.request.ReqPreciseMove;
import behaviorInterface.message.request.ReqResume;
import behaviorInterface.message.request.ReqStraightBackMove;
import behaviorInterface.message.request.ReqUnload;
import behaviorInterface.mosInterface.communication.Adaptor;
import behaviorInterface.mosInterface.mosValue.ActionType;
import behaviorInterface.mosInterface.mosValue.DoorID;
import behaviorInterface.mosInterface.mosValue.MessageType;
import behaviorInterface.mosInterface.mosValue.RobotID;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class MosInterface {
	RobotID robotID = null;;
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
		switch(messageType) {
		case RTSR:
			RTSR rtsr = (RTSR)message;
			String loading;
			if(rtsr.isLoading()) loading = "Loading";
			else loading = "Unloading";
//			System.out.println("RTSR x : " + rtsr.getX());
//			System.out.println("RTSR y : " + rtsr.getY());
			behaviorInterface.onRTSR(rtsr.getRobotID().toString(), rtsr.getRobotStatus().toString(), rtsr.getX(), rtsr.getY(), rtsr.getTheta(), rtsr.getSpeed(), rtsr.getBattery(), loading);
			break;
		case PalletizerPackingFinish:
			PalletizerPackingFinish msg = (PalletizerPackingFinish)message;
			behaviorInterface.palletizerPackingFinish(msg.getPalletizerID().toString(), msg.getNodeID());
			break;
		case AckPause:
		case AckResume:
		case AckLogin:
//			if(this.robotID == null) {
//				System.out.println("[LOCAL] " + messageType);
//			}
//			else {
//				System.out.println("[" + robotID.toString() + "] " + messageType);
//			}
			break;
		case AckMove:
		case AckCancelMove:
		case AckLoad:
		case AckUnload:
		case AckCharge:
		case AckChargeStop:
		case AckDoorOpen:
		case AckDoorClose:
		case AckEndPause:
		case AckEndResume:
		case AckGuideMove:
		case AckPreciseMove:
		case AckStraightBackMove:
		case AckPalletizerStart:
		case AckPalletizerStop:
//			if(this.robotID != null){
//				System.out.println("[" + robotID.toString() + "] " + messageType);
//			}
//			else {
//				System.out.println("[LOCAL] " + messageType);
//			}
			this.waitingResponse.setResponse(message);
			break;
		case AckEndMove:
		case AckEndCancelMove:
		case AckEndLoad:
		case AckEndUnload:
		case AckEndCharge:
		case AckEndChargeStop:
		case AckEndDoorOpen:
		case AckEndDoorClose:
		case AckEndGuideMove:
		case AckEndPreciseMove:
		case AckEndStraightBackMove:
		case AckEndPalletizerStart:
		case AckEndPalletizerStop:
//			if(this.robotID == null) {
//				System.out.println("[LOCAL] " + messageType);
//			}
//			else {
//				System.out.println("[" + robotID.toString() + "] " + messageType);
//			}
			this.waitingResponse.setResponse(message);
			this.behaviorInterface.sendMessage(this.waitingResponse.getSender(), this.waitingResponse.getResponse());
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
	
	public String login(RobotID robotID) {
		if(!this.isLogin) {
			this.waitingResponse = new ReqLogin(robotID);
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
	
	public String move(String sender, String actionID, int pathSize, List<Integer> path) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqMove(sender, actionID, this.robotID, pathSize, path);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String cancelMove(String sender, String actionID) throws Exception {
		if(this.isLogin) {
			this.pausedWaitingResponse = this.waitingResponse;
			this.waitingResponse = new ReqCancelMove(sender, actionID, this.robotID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			String response = this.waitingResponse.getResponse();
			if(response.contains("fail")) {
				this.waitingResponse = this.pausedWaitingResponse;
				this.pausedWaitingResponse = null;
			}
			else {
				if(this.pausedWaitingResponse != null) {
					this.pausedWaitingResponse.setResponse(new AckEndMove(this.robotID.getValue(), 1));
					this.pausedWaitingResponse = null;
				}
			}
			return response;
		}
//		else if(this.currentActionType == null) {
//			Expression id = GLFactory.newExpression(GLFactory.newValue(actionID));
//			Expression acion = GLFactory.newExpression(GLFactory.newGL("actionID", id));
//			Expression actionResult = GLFactory.newExpression(GLFactory.newValue("success"));
//			GeneralizedList gl = GLFactory.newGL(ActionType.cancelMove.toString(), acion, actionResult);
//			return gl.toString();
//		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String load(String sender, String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqLoad(sender, actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String unload(String sender, String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqUnload(sender, actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String charge(String sender, String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqCharge(sender, actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String chargeStop(String sender, String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqChargeStop(sender, actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String pause(String sender, String actionID) throws Exception {
		if(this.isLogin && this.currentActionType != ActionType.Pause && this.currentActionType != null) {
			this.pausedWaitingResponse = this.waitingResponse;
			this.waitingResponse = new ReqPause(sender, actionID, this.robotID);
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
	
	public String resume(String sender, String actionID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == ActionType.Pause) {
			this.waitingResponse = new ReqResume(sender, actionID, this.robotID);
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
	
	public String doorOpen(String sender, String actionID, String doorID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqDoorOpen(sender, actionID, DoorID.valueOf(doorID));
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String doorClose(String sender, String actionID, String doorID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqDoorClose(sender, actionID, DoorID.valueOf(doorID));
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail)";
		}
	}
	
	public String guideMove(String sender, String actionID, int nodeID, String direction) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqGuideMove(sender, actionID, this.robotID, nodeID, direction);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String preciseMove(String sender, String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqPreciseMove(sender, actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String straightBackMove(String sender, String actionID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqStraightBackMove(sender, actionID, this.robotID, nodeID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String palletizerStart(String sender, String actionID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqPalletizerStart(sender, actionID, this.robotID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String palletizerStop(String sender, String actionID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			this.waitingResponse = new ReqPalletizerStop(sender, actionID, this.robotID);
			this.currentActionType = this.waitingResponse.getActionType();
			this.adaptor.send(this.waitingResponse);
			return this.waitingResponse.getResponse();
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String enterPalletizer(String sender, String actionID, RobotID robotID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			ReqMessage msg = new ReqEnterPalletizer(sender, actionID, this.robotID, robotID, nodeID);
			this.adaptor.send(msg);
			this.currentActionType = null;
			return "(ok)";
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
	
	public String exitPalletizer(String sender, String actionID, RobotID robotID, int nodeID) throws Exception {
		if(this.isLogin && this.waitingResponse == null && this.currentActionType == null) {
			ReqMessage msg = new ReqExitPalletizer(sender, actionID, this.robotID, robotID, nodeID);
			this.adaptor.send(msg);
			this.currentActionType = null;
			return "(ok)";
		}
		else {
			return "(fail (actionID \"" + actionID + "\"))";
		}
	}
}
