package singleswitch.main;
public class GlobalSetting {
	public static boolean DEBUG = false; // debug model
	public static boolean FAKE_LOSS_RATE_FROM_CONTROLLER = true;

	// public static String RESULT_FILE_NAME =
	// "data\\intervalResultsDiffLossRatioMethod1.txt";
	public static String RESULT_FILE_NAME = "data\\intervalResultsExponential.txt";

	public static int SECOND_2_USECOND = 1000000;
	public static int RTT = 1; // millisecond

	//
	public static int IS_USE_REPLACE_MECHANISM = 0; // 1: yes, 0: no
	// 1:volume > threshold, 
	// 2:loss rate > threshold && volume > threshold
	// 3:loss rate > threshold && volume > threshold. Use confidence level to adjust flow sampling rate
	public static int OBJECT_VOLUME_OR_RATE = 3; 
	// METHOD_NUMBER=0->overwrite when collision happens; =1->replace mechanism
	public static int METHOD_NUMBER = 0; // valid only when
											// OBJECT_VOLUME_OR_RATE=2

	/*
	 * About experiment setup
	 */
	// packet loss rate
	// public static double PACKET_LOSS_RATE = 0.01;
	// volume drop rate
	public static double VOLUME_DROP_RATE = 0.15;

	// target flow lost volume
	public static double TARGET_FLOW_LOST_VOLUME_THRESHOLD = 15000;
	// target flow loss rate
	public static double TARGET_FLOW_LOST_RATE_THRESHOLD = 0.20;
	public static double TARGET_FLOW_TOTAL_VOLUME_THRESHOLD = 20000; // only
																		// care
																		// flows
																		// larger
																		// than
																		// 20k
																		// in
																		// 30s

	public static int INTERVAL_SECONDS = 30; // the number of seconds in one
												// interval
	public static int SIMULATE_INVERVALS = 20; // how many intervals to test

	// sample and hold: default sample rate per bit
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 2.2e-6; //loss volume v1
	// prob
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 4e-5; //loss volume v2
	// prob
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 2e-3; //loss rate
	// replace(method1) prob
	public static double DEAFULT_BYTE_SAMPLE_RATE = 2e-4; // loss rate v1 prob
	// public static double DEAFULT_BYTE_SAMPLE_RATE = 2.2e-3;

	/* timeout parameters */
	// threshold of accumulated normal volume for computing loss ratio
	// average packet size: 619bytes=>1000bytes; loss rate:15%;
	// least loss packet number: 15, least normal packets: 100
	public static long NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO = 0; // 10kbytes
																			// this
																			// is
																			// used
																			// in
																			// PacketSampleModelLinear/Exp/Log...
	public static double NUMBER_MICSECONDS_TO_WAIT_BEFORE_DELETE = 1000000;
	/*
	 * //sample and hold: default increase of sample rate per bit, when meeting
	 * one loss packet public static double DEFAULT_DELTA_BYTE_SAMPLE_RATE =
	 * 1.7e-6; //DEFAULT_DELTA_BYTE_SAMPLE_RATE : start value public static
	 * double DEFAULT_DELTA_BYTE_SAMPLE_RATE_START = 2.2e-10;
	 * //DEFAULT_DELTA_BYTE_SAMPLE_RATE : end value public static double
	 * DEFAULT_DELTA_BYTE_SAMPLE_RATE_END = 2.2e-1;
	 * //DEFAULT_DELTA_BYTE_SAMPLE_RATE : increase times public static int
	 * DEFAULT_DELTA_BYTE_SAMPLE_RATE_INCREASE_TIMES = 10;
	 */

	/*
	 * About target flows
	 */
	// #lost packet for one flow / #all lost packet > ratio, hold the flow
	// public static double LEAST_LOSS_PROPORTION = 0.001;
	// lower threshold for #loss packet of flows to hold

}
