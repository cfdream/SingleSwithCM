import java.util.Random;


/*
 * drop consecutive packets (one RTT time) when queue overflow happens
 */
public class PacketDropConsecutivePackets extends PacketDropModel{
	static long NOT_START = 0;
	static long MILLISECOND = 1000;	//1ms = 1000 us
	
	boolean isIntervalRandomed;  //whether the interval has used random function to decide drop or not.
	boolean isIntervalDropped;
	long ongoingMilliSecond;
	Random random;
	
	public PacketDropConsecutivePackets() {
		super();
		this.isIntervalRandomed = false;
		this.isIntervalDropped = false;
		ongoingMilliSecond = NOT_START;
		//TODO: set back;
		random = new Random(System.currentTimeMillis());
		if (GlobalData.DEBUG){
			random.setSeed(123456789);
		}
	}

	@Override
	public boolean drop(Packet packet) {
		long ithMillsecond = packet.microsec / (MILLISECOND * GlobalData.RTT);
		if (ithMillsecond != ongoingMilliSecond) {
			ongoingMilliSecond = ithMillsecond;
			isIntervalRandomed = false;
			isIntervalDropped = false;
		}
		if (!isIntervalRandomed) {
			isIntervalRandomed = true;
			//random to decide whether dropping the interval or not
			double randNum = random.nextDouble();
			if (randNum < GlobalData.VOLUME_DROP_RATE) {
				isIntervalDropped = true;
			}
		}
		return isIntervalDropped;
	}
	
}
