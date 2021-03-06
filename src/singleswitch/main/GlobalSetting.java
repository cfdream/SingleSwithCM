package singleswitch.main;
public class GlobalSetting {
	public static boolean DEBUG = false; // debug model
	public static boolean DEBUG2 = true; // debug model
	public static Long DEBUG_SRCIP = 2967874518L;
	
	public static boolean USE_GROUND_TRUTH_FLOW_VOLUME = true;
	public static String RESULT_FILE_NAME = "data\\intervalResultsExponential.txt";

	/* experiment setup */
	// packet loss rate
	// public static double PACKET_LOSS_RATE = 0.01;
	// volume drop rate
	public static int SECOND_2_USECOND = 1000000;
	public static int RTT = 1; // millisecond
	public static double VOLUME_DROP_RATE = 0.15;
	//public static double VOLUME_DROP_RATE = 0.15;
	
	public static int INTERVAL_SECONDS = 5; // the number of seconds in one interval
	public static int SIMULATE_INVERVALS = 2; // how many intervals to test
	
	public static int IS_USE_REPLACE_MECHANISM = 1; // 1: yes, 0: no
	
	public static int NUM_PACKETS_TO_COMPUTE_LOSS_RATE = 5;
	/*experiment setup*/

	// valid only when OBJECT_VOLUME_OR_RATE=2
	// METHOD_NUMBER=0->overwrite when collision happens; =1->replace mechanism
	public static int METHOD_NUMBER = 1; 

	/* timeout parameters */
	// threshold of accumulated normal volume for computing loss ratio
	// average packet size: 619bytes=>1000bytes; loss rate:15%;
	// least loss packet number: 15, least normal packets: 100
	// 10kbytes this is used in PacketSampleModelLinear/Exp/Log...
	public static long NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO = 0; 
	public static double NUMBER_MICSECONDS_TO_WAIT_BEFORE_DELETE = 1000000;

}
