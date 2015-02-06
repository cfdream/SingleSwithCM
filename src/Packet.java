
public class Packet {
	public static String MSG_END = "END";
	public static String MSG_NORMAL = "NORMAL";
	public static String MSG_LOST = "LOST";
	public static int HEADER_SIZE = 64;	//head bytes
	
	//header
	long microsec;	//usecond
	long srcip;
	long destip;
	int srcport;
	int destport;
	short protocol;
	Long length;	//in bytes
	
	//information
	String type;	//to record some control information
	
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public Long getLength() {
		return length;
	}

	public boolean isEndPacket(){
		if (null == type) {
			return false;
		}
		if (type.equals(MSG_END)) {
			return true;
		}
		return false;
	}

	public Packet(Long microsec, Long srcip, Long destip, int srcport, int destport, short protocol, 
			Long length){
		this.microsec = microsec;
		this.srcip = srcip;
		this.destip = destip;
		this.srcport = srcport;
		this.destport = destport;
		this.protocol = protocol;
		this.length = length;
	}
	
	public static Packet parsePacket(String line) {
		String[] subStrs = line.split(",");
		if (subStrs.length != 7) {
			return null;
		}
		Long microsec = Long.parseLong(subStrs[0]);
		Long srcip = Long.parseLong(subStrs[1]);
		Long destip = Long.parseLong(subStrs[2]);
		int srcport = Integer.parseInt(subStrs[3]);
		int destport = Integer.parseInt(subStrs[4]);
		if(subStrs[5].equals("null")){
			return null;
		}
		short protocol = Short.parseShort(subStrs[5]);
		Long length = Long.parseLong(subStrs[6]);		//body size
		length += Packet.HEADER_SIZE;					//total packet size
		
		return new Packet(microsec, srcip, destip, srcport, destport, protocol, length);
	}
}
