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

public class PacketSampleModelExponential extends PacketSampleModel{
	
	// 2.2e-6*E^(8.6847e-4*x)		//for lost volume, v1
	//4e-5*exp(6.7510874e-4*x);		//for lost volume, v2
	//2e-4*exp(42.58596596*x)		//for lost rate, v1
	Random random;
	
	public PacketSampleModelExponential(HashMap<FlowKey, Long> lost_flow_map, 
			HashMap<FlowKey, Long> normal_flow_map,
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, normal_flow_map, sampledFlowVolumeMap);
		
		random = new Random(System.currentTimeMillis());
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		FlowKey flowKey = new FlowKey(packet);
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
		double byteSamplingRate = GlobalSetting.DEAFULT_BYTE_SAMPLE_RATE;
		if (GlobalSetting.OBJECT_VOLUME_OR_RATE == 1 && null != flowLostVolume) {
			//byteSamplingRate = 2.2e-6*Math.pow(Math.E, 8.6847e-4*flowLostVolume);
			byteSamplingRate = 4e-5*Math.pow(Math.E, 6.7510874e-4*flowLostVolume);
		}
		double lossRate = 0;
		long totalVolume = 0;
		if (GlobalSetting.OBJECT_VOLUME_OR_RATE == 2) {
			
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
			totalVolume = flowLostVolume + normalVolume;
			if (totalVolume <= GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO) {
				lossRate = 0;
			} else {
				lossRate = 1.0 * flowLostVolume / totalVolume;
			}			
			
			byteSamplingRate = 2e-4 * Math.pow(Math.E, 42.58596596*lossRate);

		}
		if (3 == GlobalSetting.OBJECT_VOLUME_OR_RATE) {
			//TODO
		}
		double packetSampleRate = packet.length * byteSamplingRate;
		double randDouble = random.nextDouble();
		
		if (GlobalSetting.DEBUG && packet.srcip == 856351067) {
			ithPacketForOneFlow++;
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter("Exp_lossRate_samplingRate.txt", true));
				writer.write(packet.srcip + " " + ithPacketForOneFlow + " " + flowLostVolume + " "
						+ totalVolume+" "+lossRate + " " 
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
