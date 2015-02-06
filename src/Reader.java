import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader{
	/*
	 * About experiment data
	 */
	public static String FILE_HEADER = "C:\\workspace\\data\\equinix-sanjose.dirA.20120920-";
	public static String FILE_TAILER = ".UTC.anon.pcap.csv";
	public static int MINUTE_STRING_START = 130000;
	public static int MINUTE_STRING_DELTA = 100;	//file of ith minute: MINUTE_START + MINUTE_INTERVAL * I  
	public static int SECONDS_IN_ONE_FILE = 60;
	public static long START_USECOND = 21600000000L;	//microsecond
		
	long currentUSecond; //data in currentSecond at reader starts at reader
	int fileString;
	BufferedReader reader;
	
	
	public Reader() {
		super();
		this.currentUSecond = START_USECOND;
		this.fileString = MINUTE_STRING_START;
		this.reader = null;
	}
	
	/*
	 * @return: line read, or null
	 */
	private String readOneLineFromFile(Integer ithInterval) {
		String line = null;
		try {
			if (null == reader) {
				//open the first file
				int firstFileNO = ithInterval * GlobalData.INTERVAL_SECONDS / SECONDS_IN_ONE_FILE;
				fileString = MINUTE_STRING_START + firstFileNO * MINUTE_STRING_DELTA;
				String filePath = FILE_HEADER + fileString + FILE_TAILER;
				File file = new File(filePath);
				if (file.exists() && !file.isDirectory()) {
					reader = new BufferedReader(new FileReader(filePath));	
				}
			} else {
				//reader is just the exact file
			}
			
			//try reading one line is ok
			while ((line = reader.readLine()) == null) {
				//close current file;
				//open next file and read;
				reader.close();
				
				//open the next file;
				fileString += MINUTE_STRING_DELTA;
				String filePath = FILE_HEADER + fileString + FILE_TAILER;
				File file = new File(filePath);
				if (!file.exists() || file.isDirectory()) {
					return null;
				}
				reader = new BufferedReader(new FileReader(filePath));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}
	
	/*
	 * ithInterval: [0, SIMULATE_INTERVALS)
	 * TODO: to test
	 * @return: -1: error, 0: succ
	 */
	public int readIthIntervalPackets(int ithInterval) {
		long startUSecond = START_USECOND + ithInterval * GlobalData.INTERVAL_SECONDS * GlobalData.SECOND_2_USECOND;
		long endUSecond = startUSecond + GlobalData.INTERVAL_SECONDS * GlobalData.SECOND_2_USECOND;
		PacketDropConsecutivePackets packetDropConsecutivePackets = new PacketDropConsecutivePackets();
		
		long totalVolume = 0;			//header + body
		long totalLostVolume = 0;
		int lines = 0;
		
		
		///BufferedWriter lostWriter;
//		try {
//			lostWriter = new BufferedWriter(new FileWriter("data\\lostPacketsInOneInterval.txt"));
//
//			BufferedWriter normalWriter = 
//					new BufferedWriter(new FileWriter("data\\normalPacketsInOneInterval.txt"));
			
			String line = null;
			while ((line = readOneLineFromFile(ithInterval)) != null) {
				Packet packet = Packet.parsePacket(line);
				if (null == packet) {
					continue;
				}
				
				//read starting from startUSecond, stopping at endUSecond
				if (packet.microsec < startUSecond) {
					continue;
				}
				if (packet.microsec > endUSecond) {
					break;
				}
				
				//get the flow for the packet
				FlowKey flow = new FlowKey(packet.srcip);
				Controller.volumeInTheInterval += packet.length;
				Controller.addAllPacketsForFlow(flow, 1);
				Controller.addVolumeOfFlow(flow, packet.length);
				Controller.updateStartEndTimeForFlow(flow, packet.microsec);
				
				//handle the packet
				lines++;
				totalVolume += packet.length;
				
				//calculate prob of dropping the packet
				boolean isDropped = packetDropConsecutivePackets.drop(packet);
				if (isDropped == PacketDropModel.KEEP) {
					//----normal packet 
					packet.setType(Packet.MSG_NORMAL);
					Switch.LIST_PACKETS.add(packet);
					
					//ground truth of normal packet 
					Controller.addNormalPacketsForFlow(flow, 1);
					Controller.addNormalVolumeOfFlow(flow, packet.length);
					
					//record in normalWriter
					//normalWriter.write(line+"\r\n");
				} else {
					//----lost packet
					packet.setType(Packet.MSG_LOST);
					Switch.LIST_PACKETS.add(packet);
					System.out.println(packet.srcip + " " + packet.microsec);
					
					//ground truth of lost packet  
					Controller.addLostPacketsForFlow(flow, 1);
					Controller.addLostVolumeOfFlow(flow, packet.length);
					totalLostVolume += packet.length;
					
					//record in lostwriter
					//lostWriter.write(line+"\r\n");
				}
			}
			//lostWriter.close();
			//normalWriter.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("summary info for " + ithInterval + " interval: "
				+ "\r\n lines=" + lines
				+ "\r\n totalVolumeForAllFlows=" + totalVolume
				+ "\r\n totoalLostVolume=" + totalLostVolume 
				+ "\r\n VolumeLostRatio=" + 1.0 * totalLostVolume / totalVolume
				+ "\r\n numFlows=" + Controller.getFlowSize());
		return 0;
	}
	
	/*
	 * read all packet information from filePath
	 * randomly drop packets, and add packet headers (both normal and loss packets) into QUEUE 
	 * when the file is finished read, add END packet header into QUEUE
	 */
	/*
	public void readOneFile(String filePath) {
		Random rand = new Random();
		Long interval_stime = Long.MIN_VALUE;
		int seconds = 0;
		int packetSizeInOneSec = 0;			//header + body
		int lines = 0;
	
		try {
			reader = new BufferedReader(new FileReader(filePath));
			
			String line;
			while ((line = reader.readLine()) != null) {
				String[] subStrs = line.split(",");
				if (subStrs.length != 7) {
					continue;
				}
				Long microsec = Long.parseLong(subStrs[0]);
				Long srcip = Long.parseLong(subStrs[1]);
				Long destip = Long.parseLong(subStrs[2]);
				int srcport = Integer.parseInt(subStrs[3]);
				int destport = Integer.parseInt(subStrs[4]);
				if(subStrs[5].equals("null")){
					continue;
				}
				short protocol = Short.parseShort(subStrs[5]);
				int length = Integer.parseInt(subStrs[6]);		//body size
				length += Packet.HEADER_SIZE;					//total packet size
				
				lines++;
				packetSizeInOneSec += length;
				if (interval_stime == Long.MIN_VALUE) {
					interval_stime = microsec;
				}
				if ((microsec - interval_stime) > 1000000) {
					seconds++;
					System.out.println("Reader:"+seconds+"s, lines:"+lines+", flows:"+Controller.getFlowSize()
							+",packetSizeInOneSec:"+packetSizeInOneSec);
					
					//----send the collected data to Controller
					interval_stime = microsec;
					packetSizeInOneSec = 0;
				}
				if (seconds >= GlobalData.SIMULATE_SECONDS) {
					break;
				}
				
				//calculate prob of dropping the packet
				double prob = rand.nextDouble();
				if (prob > GlobalData.PACKET_LOSS_RATE) {
					//----normal packet 
					Packet pkg = new Packet(microsec, srcip, destip, srcport, destport, protocol, length, Packet.MSG_NORMAL);
					Switch.PACKET_QUEUE.put(pkg);
					Switch.listPackets.add(pkg);
					
					//get the flow for the packet
					FlowKey flow = new FlowKey(pkg);
					
					//ground truth of normal packet  
					Controller.addNormalPacketsForFlow(flow, 1);
				} else {
					//----lost packet
					Packet pkg = new Packet(microsec, srcip, destip, srcport, destport, protocol, length, Packet.MSG_LOST);
					Switch.PACKET_QUEUE.put(pkg);
					Switch.listPackets.add(pkg);
					
					//get the flow for the packet
					FlowKey flow = new FlowKey(pkg);
					
					//ground truth of lost packet  
					Controller.addLostPacketsForFlow(flow, 1);
				}
			}//end while ((line = reader.readLine()) != null)
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//insert the END signal to the queue
		Packet pkg = new Packet(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, 0, 0, (short) 0, 0, Packet.MSG_END);
		try {
			Switch.PACKET_QUEUE.put(pkg);
			Switch.listPackets.add(pkg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run(){

			String filePath = "C:\\workspace\\data\\equinix-sanjose.dirA.20120920-130000.UTC.anon.pcap.csv";
			
			readOneFile(filePath);
			
			System.out.println("read completed");
	}
	*/
}
