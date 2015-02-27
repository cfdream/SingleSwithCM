package singleswitch.main;

public class TargetFlowSetting {
	// 1:volume > threshold, 
	// 2:loss rate > threshold && volume > threshold
	// 3:loss rate > threshold && volume > threshold. Use confidence level to adjust flow sampling rate
	public static int OBJECT_VOLUME_OR_RATE = 3; 
	
	// OBJECT_VOLUME_OR_RATE = 1
	//target flow lost volume
	public static double TARGET_FLOW_LOST_VOLUME_THRESHOLD = 15000;

	// OBJECT_VOLUME_OR_RATE = 2/3
	//CONDITION I
	//target flow loss rate
	public static double TARGET_FLOW_LOST_RATE_THRESHOLD = 0.20;	
	//CONDITION II
	//loss volume v1, v2; loss rate v1
	//public static double TARGET_FLOW_TOTAL_VOLUME_THRESHOLD = 20000; // only care flows larger than 20k in 30s
	//loss rate v2 (use confidence)
	public static double TARGET_FLOW_TOTAL_VOLUME_THRESHOLD = 100000; // only care flows larger than 100k in 30s
}
