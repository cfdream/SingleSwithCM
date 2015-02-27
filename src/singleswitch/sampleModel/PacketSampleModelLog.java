package singleswitch.sampleModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;
import singleswitch.main.GlobalData;
import singleswitch.main.GlobalSetting;
import singleswitch.main.TargetFlowSetting;

public class PacketSampleModelLog extends PacketSampleModel{
	//y = 0.104*log(e, x+1)				//loss volume v1, v2
	//y = 5.484814977* log(e, x+1)		//loss rate v1
		
	public PacketSampleModelLog(HashMap<FlowKey, Long> lost_flow_map, 
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, sampledFlowVolumeMap);
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		FlowKey flowKey = new FlowKey(packet);
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
		double byteSamplingRate = PacketSampleSetting.DEAFULT_BYTE_SAMPLE_RATE;
		if (TargetFlowSetting.OBJECT_VOLUME_OR_RATE == 1 && null != flowLostVolume) {
			//y = 0.104*log(e, x+1)
			byteSamplingRate = 0.104*Math.log(flowLostVolume+1);
		}
		if (TargetFlowSetting.OBJECT_VOLUME_OR_RATE == 2) {
			double lossRate = getLossRate(flowKey); 
					
			if (lossRate == 0 ) {
				byteSamplingRate = PacketSampleSetting.DEAFULT_BYTE_SAMPLE_RATE;
			} else {
				byteSamplingRate = 5.484814977 * Math.log(lossRate + 1);
			}
			
			if (GlobalSetting.DEBUG && packet.srcip == GlobalSetting.DEBUG_SRCIP) {
				ithPacketForOneFlow++;
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("Log_lossRate_samplingRate.txt", true));
					writer.write(ithPacketForOneFlow +" "+lossRate + " " + byteSamplingRate + "\n\r");
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (3 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
			//get confidence of the loss rate of the flow > target loss rate threshold
			Double confidence = GlobalData.Instance().gFlowConfidenceMap.get(flowKey);
			if (confidence == null) {
				confidence = 0.0;
			}

			//calculate flow sampling rate based on confidence
			// y = log(a*x+1) + b
			double flowSamplingRate = Math.log(PacketSampleModelLogSetting.LOG_A * confidence + 1) 
					+ PacketSampleModelLogSetting.LOG_B;
			//calculate byte sampling rate
			byteSamplingRate = PacketSampleSetting.OVER_SAMPLING_RATIO *
					flowSamplingRate / TargetFlowSetting.TARGET_FLOW_TOTAL_VOLUME_THRESHOLD;

		}
		double packetSampleRate = packet.length * byteSamplingRate;
		
		double randDouble = random.nextDouble();
		if (randDouble < packetSampleRate) {
			return true;
		}
		return false;
	}

}
