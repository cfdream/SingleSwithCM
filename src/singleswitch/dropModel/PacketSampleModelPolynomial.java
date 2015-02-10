package singleswitch.dropModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;
import singleswitch.main.GlobalSetting;

public class PacketSampleModelPolynomial extends PacketSampleModel{
	
	// 2.9629564e-13 * x^3 + 2.2e-6			loss volume v1
	// 2.962844444e-13*x.^3 + 4e-5;			loss volume v2
	// y = 124.975 * x ^3 + 2e-4			loss rate v1
		
	public PacketSampleModelPolynomial(HashMap<FlowKey, Long> lost_flow_map, 
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, sampledFlowVolumeMap);
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		FlowKey flowKey = new FlowKey(packet);
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
		//// 2.9629564e-13 * x^3 + 2.2e-6
		double byteSamplingRate = GlobalSetting.DEAFULT_BYTE_SAMPLE_RATE;
		if (GlobalSetting.OBJECT_VOLUME_OR_RATE == 1 && null != flowLostVolume) {
			//byteSamplingRate += (2.9629564e-13 * Math.pow(flowLostVolume, 3));
			byteSamplingRate += (2.962844444e-13 * Math.pow(flowLostVolume, 3));
		}
		if (GlobalSetting.OBJECT_VOLUME_OR_RATE == 2) {
			double lossRate = getLossRate(flowKey);
			
			byteSamplingRate = 124.975 * Math.pow(lossRate, 3) + 2e-4;
			if (GlobalSetting.DEBUG && packet.srcip == 805469142) {
				ithPacketForOneFlow++;
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("Pol_lossRate_samplingRate.txt", true));
					writer.write(ithPacketForOneFlow + " " + flowLostVolume +" "+lossRate + " " + byteSamplingRate + "\n\r");
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (3 == GlobalSetting.OBJECT_VOLUME_OR_RATE) {
			//TODO
		}
		double packetSampleRate = packet.length * byteSamplingRate;
		
		double randDouble = random.nextDouble();
		if (randDouble < packetSampleRate) {
			return true;
		}
		return false;
	}

}
