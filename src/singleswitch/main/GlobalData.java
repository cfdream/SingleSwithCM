package singleswitch.main;

import java.util.ArrayList;
import java.util.HashMap;

import singleswitch.data.FlowKey;
import singleswitch.data.Packet;

public class GlobalData {
	
	private GlobalData() {}
	
	public static GlobalData Instance() {
		return singleInstance;
	}
	
	private static GlobalData singleInstance = new GlobalData();
	
	//
	public HashMap<FlowKey, ArrayList<Double> > gFlowLossRateListMap = 
			new HashMap<FlowKey, ArrayList<Double> >();
	
	public HashMap<FlowKey, Long> gNormalFlowVolumeMap = new HashMap<FlowKey, Long>();
	
	//At the start of each interval, this function should be called
	public void clear() {
		gFlowLossRateListMap.clear();
		gNormalFlowVolumeMap.clear();
	}
	
	public void insertIntoNormalFlowVolumeMap(FlowKey flow, Packet pkg) {
		/* in order to get real loss rate */
		Long normalVolume = gNormalFlowVolumeMap.get(flow);
		if (normalVolume == null) {
			gNormalFlowVolumeMap.put(flow, pkg.length);
		} else {
			gNormalFlowVolumeMap.put(flow, normalVolume + pkg.length);
		}
		/* in order to get real loss rate */
	}
	
	public void insertIntoFlowLossRateSamplesMap(FlowKey flow, double lossRate) {
		ArrayList<Double> lossRateList = gFlowLossRateListMap.get(flow);
		if (lossRateList == null) {
			lossRateList = new ArrayList<Double>();
			lossRateList.add(lossRate);
			gFlowLossRateListMap.put(flow, lossRateList);
		} else {
			lossRateList.add(lossRate);
			if (lossRateList.size() > 30) {
				//only keep the latest samples
				//TODO: should debug to check how often this happens
				System.out.println("flow: " + flow.srcip + ", lossRateList.size() > 30, delete at head");
				lossRateList.remove(0);
			}
		}
	}
}
