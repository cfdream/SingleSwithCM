package singleswitch.sampleModel;

public class PacketSampleModelLogSetting {
	//y = log(ax+1) + b
	public static double LOG_B = PacketSampleSetting.INITIAL_FLOW_SAMPLING_RATE_FOR_SH;
	public static double LOG_A = 
			Math.pow(Math.E, PacketSampleSetting.TARGET_FLOW_SAMPLING_RATE - LOG_B)
			- 1;
	
}
