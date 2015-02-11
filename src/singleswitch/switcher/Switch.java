package singleswitch.switcher;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import singleswitch.controller.Controller;
import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.Packet;
import singleswitch.data.ResultData;
import singleswitch.main.GlobalData;
import singleswitch.main.GlobalSetting;
import singleswitch.main.TargetFlowSetting;
import singleswitch.sampleModel.PacketSampleModel;
import singleswitch.sampleModel.PacketSampleModelTraditional;

public class Switch implements Runnable {
	// Queues for normal and lost packets
	// public static LinkedBlockingQueue<Packet> PACKET_QUEUE = new
	// LinkedBlockingQueue<Packet>(10000000); //10M
	public static LinkedBlockingQueue<Packet> PACKET_QUEUE = new LinkedBlockingQueue<Packet>(
			40000000); // 10M
	public static ArrayList<Packet> LIST_PACKETS = new ArrayList<Packet>(
			40000000);

	// lost flow packet number map
	HashMap<FlowKey, Integer> lostFlowPkgNumMap = new HashMap<FlowKey, Integer>();
	// sampled normal flow map
	HashMap<FlowKey, Integer> sampledFlowPkgNumMap = new HashMap<FlowKey, Integer>();

	// lost flow volume map
	public HashMap<FlowKey, Long> lostFlowVolumeMap = new HashMap<FlowKey, Long>();
	// sampled normal flow volume map
	// HashMap<FlowKey, Long> sampledFlowVolumeMap = new HashMap<FlowKey,
	// Long>();
	public FixSizeHashMap sampledFlowVolumeMap = new FixSizeHashMap();

	Random rand = new Random(System.currentTimeMillis());

	// PacketSampleModelLinear packetSampleModelLinear = new
	// PacketSampleModelLinear(lostFlowVolumeMap);
	// public PacketSampleModel packetSampleModel = new
	// PacketSampleModelLog(lostFlowVolumeMap, sampledFlowVolumeMap);
	public PacketSampleModel packetSampleModel = new PacketSampleModelTraditional(
			lostFlowVolumeMap, sampledFlowVolumeMap);

	/*
	 * For running monitor
	 */
	Long interval_stime = Long.MIN_VALUE;
	int seconds = 0;
	int lines = 0;

	/*
	 * SAMPLE AND HOLD
	 */
	public void sampleAndHold(Packet pkg) {
		FlowKey flow = new FlowKey(pkg);
		Integer cnt = sampledFlowPkgNumMap.get(flow);

		/*
		 * if (null == cnt) { //----packet not sampled yet
		 * 
		 * boolean isHeld = packetSampleModel.isSampled(pkg); if (isHeld) {
		 * //sample success, start hold the packet
		 * sampledFlowPkgNumMap.put(flow, 1); } } else { //----packet already
		 * sampled, hold it sampledFlowPkgNumMap.put(flow, cnt + 1); }
		 */
		Long volume = sampledFlowVolumeMap.get(flow);
		if (null == volume) {
			// ----packet not sampled yet

			boolean isHeld = packetSampleModel.isSampled(pkg);
			if (isHeld) {
				// sample success, start hold the packet
				sampledFlowPkgNumMap.put(flow, 1);
				if (1 == GlobalSetting.IS_USE_REPLACE_MECHANISM) {
					sampledFlowVolumeMap.put(flow, pkg.length, pkg.microsec,
							lostFlowVolumeMap);
				} else {
					sampledFlowVolumeMap.put(flow, pkg.length);
				}
			}
		} else {
			// ----packet already sampled, hold it
			//pkt number
			sampledFlowPkgNumMap.put(flow, cnt + 1);
			
			//flow volume
			if (1 == GlobalSetting.IS_USE_REPLACE_MECHANISM) {
				sampledFlowVolumeMap.put(flow, volume += pkg.length,
						pkg.microsec, lostFlowVolumeMap);
			} else {
				sampledFlowVolumeMap.put(flow, volume += pkg.length);
			}
		}
	}

	public void sendDataToControllerAndClearStatus() {
		// store all data in sampled_flow_map to Controller
		for (Map.Entry<FlowKey, Integer> entry : sampledFlowPkgNumMap
				.entrySet()) {
			FlowKey flowKey = entry.getKey();
			Integer newCnt = entry.getValue();

			Controller.addSampledPacketsForFlow(flowKey, newCnt);
		}
		sampledFlowPkgNumMap.clear();

		// send sampledFlowVolumeMap to Controller
		for (Iterator<FixSizeHashMap.Record> iterator = sampledFlowVolumeMap
				.getAllEntries().iterator(); iterator.hasNext();) {
			FixSizeHashMap.Record entry = iterator.next();
			FlowKey flowKey = entry.flowKey;
			Long normalVolume = entry.value;

			Long lostVolume = lostFlowVolumeMap.get(flowKey);
			if (null == lostVolume) {
				lostVolume = 0L;
			}
			Long totalVolume = lostVolume + normalVolume;
			double lossRate = 1.0 * lostVolume / totalVolume;
			if (1 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
				// 1:volume > threshold
				if (lostVolume < TargetFlowSetting.TARGET_FLOW_LOST_VOLUME_THRESHOLD) {
					continue;
				}
			} else if (2 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE
					|| 3 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE){
				// 2: loss rate > threshold
				if (totalVolume < TargetFlowSetting.TARGET_FLOW_TOTAL_VOLUME_THRESHOLD
						|| lossRate < TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
					continue;
				}
			}

			// send the volume to the controller
			Controller.addSampledNormalVolumeOfFlow(flowKey, normalVolume);
		}
		/*
		 * for(Map.Entry<FlowKey, Long> entry : sampledFlowVolumeMap.entrySet())
		 * { FlowKey flowKey = entry.getKey(); Long newCnt = entry.getValue();
		 * 
		 * Controller.addSampledNormalVolumeOfFlow(flowKey, newCnt); }
		 */
		sampledFlowVolumeMap.clear();

		// clear packet loss table as well
		lostFlowPkgNumMap.clear();
		lostFlowVolumeMap.clear();
	}

	public int handleOneIncomingPacket(Packet pkg) {
		FlowKey flow = new FlowKey(pkg);
		if (pkg == null) {
			// System.out.println("Consumer: no packets");
			// ----Waiting for new incoming packets
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (pkg.isEndPacket()) {
			System.out.println("Cousume: end");
			// ----no more packets, break
			return -1;
		} else if (pkg.getType().equals(Packet.MSG_LOST)) {
			// ----lost packet
			//flow loss pkt number
			Integer cnt = lostFlowPkgNumMap.get(flow);
			if (null == cnt) {
				lostFlowPkgNumMap.put(flow, 1);
			} else {
				lostFlowPkgNumMap.put(flow, cnt + 1);
			}
			//flow loss volume
			Long volume = lostFlowVolumeMap.get(flow);
			if (null == volume) {
				lostFlowVolumeMap.put(flow, pkg.length);
			} else {
				lostFlowVolumeMap.put(flow, volume += pkg.length);
			}
			
			//when a flow losses one packet, calculate a loss rate sample, and keep it
			GlobalData.Instance().insertIntoFlowLossRateSamplesMap(flow, getLossRateForOneFlow(flow));
			
			/*debug*/
			if (GlobalSetting.DEBUG && pkg.srcip == 856351067) {
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(
							"Exp_lossRate_samplingRate.txt", true));
					writer.write(pkg.length + " " + volume + "\n\r");
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*end debug*/
		} else {
			// ----normal packet
			lines++;
			if (interval_stime == Long.MIN_VALUE) {
				interval_stime = pkg.microsec;
			}
			if ((pkg.microsec - interval_stime) > 1000000) {
				seconds++;
				System.out.println("Switch:" + seconds + "s, lines:" + lines);

				// ----send the collected data to Controller
				// sendDataToControllerAndClearStatus();
				interval_stime = pkg.microsec;
			}

			// System.out.println("interval_time:"+interval_stime+";time-intervaltime:"+(pkg.microsec-interval_stime));
			sampleAndHold(pkg);
			
			//count all normal volume for each flow. Objectives: get real volume, thus real loss rate.
			GlobalData.Instance().insertIntoNormalFlowVolumeMap(flow, pkg);
		}
		return 0;
	}

	private double getLossRateForOneFlow(FlowKey flowKey) {
		double lossRate = 0;
		Long flowLostVolume = lostFlowVolumeMap.get(flowKey);
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
		
		return lossRate;		
	}
	
	/*
	 * based in queue
	 */
	public void runDataInQueue(ResultData resultData) {

		// go through all packets, sample&hold
		while (true) {
			Packet pkg = PACKET_QUEUE.poll();
			if (handleOneIncomingPacket(pkg) < 0) {
				break;
			}
		}
	}

	/*
	 * based on listArray
	 */
	public void runDataInList() {
		// go through all packets, sample&hold
		for (Iterator<Packet> iterator = LIST_PACKETS.iterator(); iterator
				.hasNext();) {
			Packet pkg = iterator.next();
			if (handleOneIncomingPacket(pkg) < 0) {
				break;
			}
		}
	}

	/*
	 * based on listArray,
	 * 
	 * public void runOneInterval() { int lines = 0;
	 * 
	 * //go through all packets, sample&hold for (Iterator<Packet> iterator =
	 * LIST_PACKETS.iterator(); iterator.hasNext();) { Packet pkg =
	 * iterator.next(); FlowKey flow = new FlowKey(pkg); if (pkg.isEndPacket())
	 * { System.out.println("Cousumer: end"); //----no more packets, break
	 * break; } else if (pkg.getType().equals(Packet.MSG_LOST)) { //----lost
	 * packet lines++; Integer cnt = lostFlowPkgNumMap.get(pkg); if (null ==
	 * cnt) { lostFlowPkgNumMap.put(flow, 1); }else {
	 * lostFlowPkgNumMap.put(flow, cnt + 1); } } else { //----normal packet
	 * lines++;
	 * //System.out.println("interval_time:"+interval_stime+";time-intervaltime:"
	 * +(pkg.microsec-interval_stime)); sampleAndHold(pkg); } }
	 * 
	 * System.out.println("Switch interval end: lines-"+lines); }
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
