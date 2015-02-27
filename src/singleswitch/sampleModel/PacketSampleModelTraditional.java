package singleswitch.sampleModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;
import singleswitch.main.GlobalSetting;

public class PacketSampleModelTraditional extends PacketSampleModel{
	
	public PacketSampleModelTraditional(HashMap<FlowKey, Long> lost_flow_map, 
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, sampledFlowVolumeMap);
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		double byteSamplingRate = PacketSampleSetting.DEAFULT_BYTE_SAMPLE_RATE;
		double packetSampleRate = packet.length * byteSamplingRate;		
		double randDouble = random.nextDouble();
		
		if (GlobalSetting.DEBUG && packet.srcip == GlobalSetting.DEBUG_SRCIP) {
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
