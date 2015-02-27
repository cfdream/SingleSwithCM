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

public class PacketSampleModelExponential extends PacketSampleModel{
	
	// 2.2e-6*E^(8.6847e-4*x)		//for lost volume, v1
	//4e-5*exp(6.7510874e-4*x);		//for lost volume, v2
	//2e-4*exp(42.58596596*x)		//for lost rate, v1
	
	public PacketSampleModelExponential(HashMap<FlowKey, Long> lost_flow_map, 
			FixSizeHashMap sampledFlowVolumeMap) {
		super(lost_flow_map, sampledFlowVolumeMap);
	}
	
	@Override
	public boolean isSampled(Packet packet) {
		FlowKey flowKey = new FlowKey(packet);
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
		double byteSamplingRate = PacketSampleSetting.DEAFULT_BYTE_SAMPLE_RATE;
		if (TargetFlowSetting.OBJECT_VOLUME_OR_RATE == 1 && null != flowLostVolume) {
			//byteSamplingRate = 2.2e-6*Math.pow(Math.E, 8.6847e-4*flowLostVolume);
			byteSamplingRate = 4e-5*Math.pow(Math.E, 6.7510874e-4*flowLostVolume);
		}
		if (TargetFlowSetting.OBJECT_VOLUME_OR_RATE == 2) {
			double lossRate = getLossRate(flowKey);
			byteSamplingRate = 2e-4 * Math.pow(Math.E, 42.58596596*lossRate);
		}
		if (3 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
			//get confidence of the loss rate of the flow > target loss rate threshold
			Double confidence = GlobalData.Instance().gFlowConfidenceMap.get(flowKey);
			if (confidence == null) {
				confidence = 0.0;
			}
			//TODO: calculate flow sampling rate based on confidence
			//y = a*e^(b*confidence)
			double flowSamplingRate = PacketSampleModelExponentialSetting.EXPONENTIAL_A * 
					Math.pow(Math.E, confidence * PacketSampleModelExponentialSetting.EXPONENTIAL_B);
			//TODO: calculate byte sampling rate
			byteSamplingRate = PacketSampleSetting.OVER_SAMPLING_RATIO *
					flowSamplingRate / TargetFlowSetting.TARGET_FLOW_TOTAL_VOLUME_THRESHOLD;
		}
		double packetSampleRate = packet.length * byteSamplingRate;
		double randDouble = random.nextDouble();
		
		if (GlobalSetting.DEBUG && packet.srcip == GlobalSetting.DEBUG_SRCIP) {
			ithPacketForOneFlow++;
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter("Exp_lossRate_samplingRate.txt", true));
				writer.write(packet.srcip + " " + ithPacketForOneFlow + " " + flowLostVolume + " "
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
