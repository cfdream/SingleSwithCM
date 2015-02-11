package singleswitch.sampleModel;

import singleswitch.main.TargetFlowSetting;

public class PacketSampleSetting {
	public static int OVER_SAMPLING_RATIO = 4;
	
	// sample and hold: default sample rate per bit
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 2.2e-6; //loss volume v1
	// prob
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 4e-5; //loss volume v2
	// prob
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 2e-3; //loss rate
	// replace(method1) prob
	//public static double DEAFULT_BYTE_SAMPLE_RATE = 2e-4; // loss rate v1 prob, with target volume threshold 20k
		
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 2.2e-3;
	// loss rate v2 prob, with target volume threshold 100k
	//public static double DEAFULT_BYTE_SAMPLE_RATE = 4e-5;
	
	//initial FLOW sampling rate
	public static double INITIAL_FLOW_SAMPLING_RATE_FOR_SH = 0.001;
	
	public static double DEAFULT_BYTE_SAMPLE_RATE = 
			INITIAL_FLOW_SAMPLING_RATE_FOR_SH * 
			1.0 / TargetFlowSetting.TARGET_FLOW_TOTAL_VOLUME_THRESHOLD;
}
