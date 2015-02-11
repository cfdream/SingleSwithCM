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

public class PacketSampleModelLinear extends PacketSampleModel{
	
	// loss packet volume: 0, byteSamplingRate = GlobalData.DEAFULT_BYTE_SAMPLE_RATE
	// loss packet volume: 15kbyte, byteSamplingRate = 1
	//static double deltaRateForOneLostByte = 1.075e-7;
	
	//linearY=6.67e-5*x+2.2e-6;  							//loss volume	v1, change GlobalData.DEAFULT_BYTE_SAMPLE_RATE
	//static double deltaRateForOneLostByte = 6.67e-5;
	
	//linearY=0.000066664*x + 4e-5;							//loss volume	v2, change GlobalData.DEAFULT_BYTE_SAMPLE_RATE
	static double deltaRateForOneLostByte = 0.000066664;
	
	// y = 4.999*x + 2e-4									//loss rate v1, GlobalData.DEAFULT_BYTE_SAMPLE_RATE
		
	public PacketSampleModelLinear(HashMap<FlowKey, Long> lost_flow_map, 
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, sampledFlowVolumeMap);
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		FlowKey flowKey = new FlowKey(packet);
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
		double byteSamplingRate = PacketSampleSetting.DEAFULT_BYTE_SAMPLE_RATE;
		if (TargetFlowSetting.OBJECT_VOLUME_OR_RATE == 1 && null != flowLostVolume) {
			byteSamplingRate += (deltaRateForOneLostByte * flowLostVolume);
		}
		if (TargetFlowSetting.OBJECT_VOLUME_OR_RATE == 2) {
			double lossRate = getLossRate(flowKey);
			
			byteSamplingRate = 4.999 * lossRate + 2e-4;
			if (GlobalSetting.DEBUG && packet.srcip == 805469142) {
				ithPacketForOneFlow++;
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("Linear_lossRate_samplingRate.txt", true));
					writer.write(ithPacketForOneFlow + " " +" "+lossRate + " " + byteSamplingRate + "\n\r");
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
			double flowSamplingRate = PacketSampleSetting.INITIAL_FLOW_SAMPLING_RATE_FOR_SH + 
					PacketSampleModelLinearSetting.FLOW_SAMPLE_RATE_A * confidence;
			//calculate byte sampling rate
			byteSamplingRate = flowSamplingRate / TargetFlowSetting.TARGET_FLOW_TOTAL_VOLUME_THRESHOLD;
			//System.out.println("confidence:" + confidence + ", byteSamplingRate:" + byteSamplingRate);
		}
		double packetSampleRate = packet.length * byteSamplingRate;
		
		double randDouble = random.nextDouble();
		if (randDouble < packetSampleRate) {
			return true;
		}
		return false;
	}

}
