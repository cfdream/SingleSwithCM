
public abstract class PacketDropModel {
	public static boolean DROP = true;
	public static boolean KEEP = false;
	/*
	 * return KEEP/DROP to sign whether to drop the packet or not 
	 */
	abstract public boolean drop(Packet packet);
}
