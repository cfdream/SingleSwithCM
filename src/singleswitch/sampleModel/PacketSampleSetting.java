package singleswitch.sampleModel;

import singleswitch.main.TargetFlowSetting;

public class PacketSampleSetting {
	public static long TOTAL_VOLUME_IN_ONE_TIME_INTERVAL = 6181204282L;
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
	public static double INITIAL_FLOW_SAMPLING_RATE_FOR_SH = 1;	//this is for normal S&H
	public static double TARGET_FLOW_SAMPLING_RATE = 100;
	
	//initial byte sampling rate for S&H
	public static double DEAFULT_BYTE_SAMPLE_RATE = 
			OVER_SAMPLING_RATIO * INITIAL_FLOW_SAMPLING_RATE_FOR_SH 
			/ TargetFlowSetting.TARGET_FLOW_TOTAL_VOLUME_THRESHOLD;
	
	//buckets in hashmap
	public static double SHRINK_RATIO = 0.01;
	public static int SH_BUCKET_SIZE = (int)( SHRINK_RATIO * 
			DEAFULT_BYTE_SAMPLE_RATE * TOTAL_VOLUME_IN_ONE_TIME_INTERVAL);
	
	// 10007 105943, 1000003,
	// 1200007(1.2M) 13567(13k)
	// 240007(240k)
}
