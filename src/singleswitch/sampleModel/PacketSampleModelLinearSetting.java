package singleswitch.sampleModel;

public class PacketSampleModelLinearSetting {
	/* OBJECT_VOLUME_OR_RATE = 3 */
	//flow sampling rate (p) = A * confidence + p0
	public static double FLOW_SAMPLE_RATE_A = 1.0 - PacketSampleSetting.INITIAL_FLOW_SAMPLING_RATE_FOR_SH;  
}
