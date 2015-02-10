package singleswitch.dropModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;
import singleswitch.main.GlobalSetting;

public class PacketSampleModelPolynomial extends PacketSampleModel{
	
	// 2.9629564e-13 * x^3 + 2.2e-6			loss volume v1
	// 2.962844444e-13*x.^3 + 4e-5;			loss volume v2
	// y = 124.975 * x ^3 + 2e-4			loss rate v1
	
	Random random;
	
	public PacketSampleModelPolynomial(HashMap<FlowKey, Long> lost_flow_map, 
			HashMap<FlowKey, Long> normal_flow_map,
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, normal_flow_map, sampledFlowVolumeMap);
		
		random = new Random(System.currentTimeMillis());
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
			double lossRate = 0;
			if (null == flowLostVolume) {
				flowLostVolume = 0L;
			}
			Long sampledVolume = sampledFlowVolumeMap.get(flowKey);
			if (null == sampledVolume) {
				sampledVolume = 0L;
			}
			Long normalVolume = normalFlowVolumeMap.get(flowKey);
			if (null == normalVolume) {
				normalVolume = 0L;
			}
			Long totalVolume = flowLostVolume + normalVolume;
			if (totalVolume <= GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO) {
				lossRate = 0;
			} else {
				lossRate = 1.0 * flowLostVolume / totalVolume;
			}
			
			byteSamplingRate = 124.975 * Math.pow(lossRate, 3) + 2e-4;
			if (GlobalSetting.DEBUG && packet.srcip == 805469142) {
				ithPacketForOneFlow++;
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("Pol_lossRate_samplingRate.txt", true));
					writer.write(ithPacketForOneFlow + " " + flowLostVolume + " " + totalVolume+" "+lossRate + " " + byteSamplingRate + "\n\r");
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
