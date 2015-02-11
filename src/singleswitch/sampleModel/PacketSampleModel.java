package singleswitch.sampleModel;


import java.util.HashMap;
import java.util.Random;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;
import singleswitch.main.GlobalData;
import singleswitch.main.GlobalSetting;


public abstract class PacketSampleModel {
	HashMap<FlowKey, Long> lostFlowVolumeMap;
	FixSizeHashMap sampledFlowVolumeMap;
	int ithPacketForOneFlow;
	
	Random random;
	
	public PacketSampleModel(HashMap<FlowKey, Long> lost_flow_map, 
			FixSizeHashMap sampledFlowVolumeMap) {
		super();
		this.lostFlowVolumeMap = lost_flow_map;
		this.sampledFlowVolumeMap = sampledFlowVolumeMap;
		this.ithPacketForOneFlow = 0;
		random = new Random(System.currentTimeMillis());
	}

	protected double getLossRate(FlowKey flowKey) {
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
		if (null == flowLostVolume) {
			flowLostVolume = 0L;
		}
		Long normalVolume = GlobalData.Instance().gNormalFlowVolumeMap.get(flowKey);
		if (null == normalVolume) {
			normalVolume = 0L;
		}
		Long totalVolume = flowLostVolume + normalVolume;
		double lossRate = 0;
		if (totalVolume <= GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO) {
			lossRate = 0;
		} else {
			lossRate = 1.0 * flowLostVolume / totalVolume;
		}			
		return lossRate;
	}

	abstract public boolean isSampled(Packet packet);
}
