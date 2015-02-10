package singleswitch.dropModel;


import java.util.HashMap;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;


public abstract class PacketSampleModel {
	HashMap<FlowKey, Long> lostFlowVolumeMap;
	HashMap<FlowKey, Long> normalFlowVolumeMap;
	FixSizeHashMap sampledFlowVolumeMap;
	int ithPacketForOneFlow;
	
	public PacketSampleModel(HashMap<FlowKey, Long> lost_flow_map, 
			HashMap<FlowKey, Long> normal_flow_map, 
			FixSizeHashMap sampledFlowVolumeMap) {
		super();
		this.lostFlowVolumeMap = lost_flow_map;
		this.normalFlowVolumeMap = normal_flow_map;;
		this.sampledFlowVolumeMap = sampledFlowVolumeMap;
		this.ithPacketForOneFlow = 0;
	}


	abstract public boolean isSampled(Packet packet);
}
