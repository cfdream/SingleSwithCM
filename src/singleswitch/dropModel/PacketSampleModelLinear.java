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

public class PacketSampleModelLinear extends PacketSampleModel{
	
	// loss packet volume: 0, byteSamplingRate = GlobalData.DEAFULT_BYTE_SAMPLE_RATE
	// loss packet volume: 15kbyte, byteSamplingRate = 1
	//static double deltaRateForOneLostByte = 1.075e-7;
	
	//linearY=6.67e-5*x+2.2e-6;  							//loss volume	v1, change GlobalData.DEAFULT_BYTE_SAMPLE_RATE
	//static double deltaRateForOneLostByte = 6.67e-5;
	
	//linearY=0.000066664*x + 4e-5;							//loss volume	v2, change GlobalData.DEAFULT_BYTE_SAMPLE_RATE
	static double deltaRateForOneLostByte = 0.000066664;
	
	// y = 4.999*x + 2e-4									//loss rate v1, GlobalData.DEAFULT_BYTE_SAMPLE_RATE
	
	
	Random random;
	
	public PacketSampleModelLinear(HashMap<FlowKey, Long> lost_flow_map, 
			HashMap<FlowKey, Long> normal_flow_map,
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, normal_flow_map, sampledFlowVolumeMap);
		
		random = new Random(System.currentTimeMillis());
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		FlowKey flowKey = new FlowKey(packet);
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
		double byteSamplingRate = GlobalData.DEAFULT_BYTE_SAMPLE_RATE;
		if (GlobalData.OBJECT_VOLUME_OR_RATE == 1 && null != flowLostVolume) {
			byteSamplingRate += (deltaRateForOneLostByte * flowLostVolume);
		}
		if (GlobalData.OBJECT_VOLUME_OR_RATE == 2) {
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
			if (totalVolume <= GlobalData.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO) {
				lossRate = 0;
			} else {
				lossRate = 1.0 * flowLostVolume / totalVolume;
			}
			
			byteSamplingRate = 4.999 * lossRate + 2e-4;
			if (GlobalData.DEBUG && packet.srcip == 805469142) {
				ithPacketForOneFlow++;
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("Linear_lossRate_samplingRate.txt", true));
					writer.write(ithPacketForOneFlow + " " + totalVolume+" "+lossRate + " " + byteSamplingRate + "\n\r");
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		double packetSampleRate = packet.length * byteSamplingRate;
		
		double randDouble = random.nextDouble();
		if (randDouble < packetSampleRate) {
			return true;
		}
		return false;
	}

}
