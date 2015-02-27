package singleswitch.sampleModel;

public class PacketSampleModelExponentialSetting {
	//y = a*e^(bx)
	public static double EXPONENTIAL_A = 
			PacketSampleSetting.INITIAL_FLOW_SAMPLING_RATE_FOR_SH;
	public static double EXPONENTIAL_B = Math.log(
			PacketSampleSetting.TARGET_FLOW_SAMPLING_RATE 
			/ PacketSampleSetting.INITIAL_FLOW_SAMPLING_RATE_FOR_SH); 
}
