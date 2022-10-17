package behaviorInterface.mosInterface.mosValue;

public enum ActionType {
	Move, 		CancelMove, 
	Load, 		Unload, 
	Charge, 	ChargeStop, 
	Pause, 		Resume, 
	DoorOpen, 	DoorClose,
	Login,
	GuideMove,
	PreciseMove,
	StraightBackMove,
	PalletizerStart, PalletizerStop,
	EnterPalletizer, ExitPalletizer;
}
