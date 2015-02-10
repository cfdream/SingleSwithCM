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

public class PacketSampleModelLog extends PacketSampleModel{
	//y = 0.104*log(e, x+1)				//loss volume v1, v2
	//y = 5.484814977* log(e, x+1)		//loss rate v1
	
	Random random;
	
	public PacketSampleModelLog(HashMap<FlowKey, Long> lost_flow_map, 
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
			//y = 0.104*log(e, x+1)
			byteSamplingRate = 0.104*Math.log(flowLostVolume+1);
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
			if (totalVolume <= GlobalData.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO ) {
				lossRate = 0;
				byteSamplingRate = GlobalData.DEAFULT_BYTE_SAMPLE_RATE;
			} else {
				lossRate = 1.0 * flowLostVolume / totalVolume;
				byteSamplingRate = 5.484814977 * Math.log(lossRate + 1);
			}
			
			if (GlobalData.DEBUG && packet.srcip == 805469142) {
				ithPacketForOneFlow++;
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("Log_lossRate_samplingRate.txt", true));
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
