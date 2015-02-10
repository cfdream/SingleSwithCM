package singleswitch.dropModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;
import singleswitch.main.GlobalData;

public class PacketSampleModelTraditional extends PacketSampleModel{
	
	
	Random random;
	
	public PacketSampleModelTraditional(HashMap<FlowKey, Long> lost_flow_map, 
			HashMap<FlowKey, Long> normal_flow_map,
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, normal_flow_map, sampledFlowVolumeMap);
		
		random = new Random(System.currentTimeMillis());
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		double byteSamplingRate = GlobalData.DEAFULT_BYTE_SAMPLE_RATE;
		double packetSampleRate = packet.length * byteSamplingRate;		
		double randDouble = random.nextDouble();
		
		if (GlobalData.DEBUG && packet.srcip == 856351067) {
			ithPacketForOneFlow++;
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter("tradi_lossRate_samplingRate.txt", true));
				writer.write(packet.srcip + " " + ithPacketForOneFlow + " "
						+ packet.length + " " + byteSamplingRate + " " 
						+ packetSampleRate + " " + randDouble + "\n\r");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (randDouble < packetSampleRate) {
			return true;
		}
		return false;
	}

}
