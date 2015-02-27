package singleswitch.sampleModel;

public class PacketSampleModelPolynomialSetting {
	//y = a * x^3 + b
	public static double POLYNOMINAL_B = PacketSampleSetting.INITIAL_FLOW_SAMPLING_RATE_FOR_SH;
	public static double POLYNOMINAL_A = PacketSampleSetting.TARGET_FLOW_SAMPLING_RATE - POLYNOMINAL_B;
}
