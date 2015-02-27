package singleswitch.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import singleswitch.confidence.ConfidenceCalculator;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;

public class GlobalData {
	
	Random random;
	
	private GlobalData() {
		random = new Random(System.currentTimeMillis());
	}
	
	public static GlobalData Instance() {
		return singleInstance;
	}
	
	private static GlobalData singleInstance = new GlobalData();
	
	//
	public HashMap<FlowKey, ArrayList<Double> > gFlowLossRateListMap = 
			new HashMap<FlowKey, ArrayList<Double> >();
	
	public HashMap<FlowKey, Long> gNormalFlowVolumeMap = new HashMap<FlowKey, Long>();
	//lost volume map
	public HashMap<FlowKey, Long> gLostFlowVolumeMap = new HashMap<FlowKey, Long>();

	
	//<flow, double>: double is the confidence that flow's loss rate > target loss rate.
	public HashMap<FlowKey, Double> gFlowConfidenceMap = new HashMap<FlowKey, Double>();
	
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
		if (GlobalSetting.DEBUG && GlobalSetting.DEBUG_SRCIP == flow.srcip) {
			System.out.println("normal pkt-srcip:"+flow.srcip + ", normalVolume:" + gNormalFlowVolumeMap.get(flow));
		}
		/* in order to get real loss rate */
		
		sampleLossRate(flow);
	}
	
	public void insertIntoLostFlowVolumeMap(FlowKey flow, Packet pkg) {
		/* in order to get real loss rate */
		Long lostVolume = gLostFlowVolumeMap.get(flow);
		if (lostVolume == null) {
			gLostFlowVolumeMap.put(flow, pkg.length);
		} else {
			gLostFlowVolumeMap.put(flow, lostVolume + pkg.length);
		}
		if (GlobalSetting.DEBUG && GlobalSetting.DEBUG_SRCIP == flow.srcip) {
			System.out.println("lost pkt-srcip:"+flow.srcip + ", lostVolume:" + gLostFlowVolumeMap.get(flow));
		}
		/* in order to get real loss rate */
		
		sampleLossRate(flow);
	}
	
	public void sampleLossRate(FlowKey flow) {
		double randValue = random.nextDouble();
		if (randValue <= (1.0 / GlobalSetting.NUM_PACKETS_TO_COMPUTE_LOSS_RATE)) {
			//every 5 packet, sample once;
			insertIntoFlowLossRateSamplesMapAndUpdateConfidence(flow, getLossRateForOneFlow(flow));
		}
	}
	
	public double getLossRateForOneFlow(FlowKey flowKey) {
		double lossRate = 0;
		Long flowLostVolume = gLostFlowVolumeMap.get(flowKey);
		if (null == flowLostVolume) {
			flowLostVolume = 0L;
		}
		Long normalVolume = GlobalData.Instance().gNormalFlowVolumeMap.get(flowKey);
		if (null == normalVolume) {
			normalVolume = 0L;
		}
		Long totalVolume = flowLostVolume + normalVolume;
		if (totalVolume <= GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO ) {
			lossRate = 0;
		} else {
			lossRate = 1.0 * flowLostVolume / totalVolume;
		}
		
		if (GlobalSetting.DEBUG && GlobalSetting.DEBUG_SRCIP == flowKey.srcip) {
			System.out.println("src:"+flowKey.srcip + 
					", volume:" + normalVolume + 
					", lostVolume:" + flowLostVolume +
					", lossRate:" + lossRate);
		}
		
		return lossRate;		
	}
	
	public void insertIntoFlowLossRateSamplesMapAndUpdateConfidence(FlowKey flow, double lossRate) {
		//insert lossRate into the map
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
				//System.out.println("flow: " + flow.srcip + ", lossRateList.size() > 30, delete at head");
				lossRateList.remove(0);
			}
		}
		if (GlobalSetting.DEBUG && GlobalSetting.DEBUG_SRCIP == flow.srcip && lossRateList.size() > 20) {
			int a = 0;
			int b =a;
		}
		//update the flow's confidence
		//more than one lossRate sample, use distribution to calculate the confidence
		double confidence = ConfidenceCalculator.calculateConfidence(lossRateList);
		if (GlobalSetting.DEBUG2 && GlobalSetting.DEBUG_SRCIP == flow.srcip) {
//			System.out.println("src:"+flow.srcip + 
//					", volume:" + GlobalData.Instance().gNormalFlowVolumeMap.get(flow) 
//					+ ", confidence:" + confidence);
			//add all values
			SummaryStatistics stats = new SummaryStatistics();
			for (Double value : lossRateList) {
				stats.addValue(value);
			}
			//System.out.println(stats.getMean() + " " + confidence);
			
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(
						"src_"+ GlobalSetting.DEBUG_SRCIP+"_confidence.txt", true));
				writer.write(stats.getMean() + " " + confidence + "\n\r");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gFlowConfidenceMap.put(flow, confidence);
	}
}
