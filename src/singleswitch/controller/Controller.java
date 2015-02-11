package singleswitch.controller;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import singleswitch.data.FixSizeHashMap;
import singleswitch.data.FlowKey;
import singleswitch.data.FlowValue;
import singleswitch.data.FlowValueComparable;
import singleswitch.data.ResultData;
import singleswitch.main.TargetFlowSetting;

public class Controller {

	// flow map
	public static long volumeInTheInterval = 0;
	public static ConcurrentHashMap<FlowKey, FlowValue> FLOW_MAP = new ConcurrentHashMap<FlowKey, FlowValue>();

	/*
	 * //normal flow map public static ConcurrentHashMap<FlowKey, Integer>
	 * NORMAL_FLOW_MAP = new ConcurrentHashMap<FlowKey, Integer>(); //sampled
	 * normal flow map public static ConcurrentHashMap<FlowKey, Integer>
	 * SAMPLED_NORMAL_FLOW_MAP = new ConcurrentHashMap<FlowKey, Integer>();
	 * //lost flow map public static ConcurrentHashMap<FlowKey, Integer>
	 * LOST_FLOW_MAP = new ConcurrentHashMap<FlowKey, Integer>();
	 */

	public static void clear() {
		volumeInTheInterval = 0;
		FLOW_MAP.clear();
	}

	public static void clearSwitchData() {
		for (Iterator<Entry<FlowKey, FlowValue>> iterator = FLOW_MAP.entrySet()
				.iterator(); iterator.hasNext();) {
			Entry<FlowKey, FlowValue> type = iterator.next();
			type.getValue().clearSwitchData();
		}
	}

	public static int getFlowSize() {
		return FLOW_MAP.size();
	}

	public static void addAllPacketsForFlow(FlowKey flowKey, int num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.numAllPackets = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.numAllPackets += num;
			// TODO: whether put or not
		}
	}

	public static void addNormalPacketsForFlow(FlowKey flowKey, int num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.numNormalPackets = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.numNormalPackets += num;
			// TODO: whether put or not
		}
	}

	public static void addLostPacketsForFlow(FlowKey flowKey, int num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.numLostPackets = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.numLostPackets += num;
			// TODO: whether put or not
		}
	}

	public static void addSampledPacketsForFlow(FlowKey flowKey, int num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.numSampledNormalPackets = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.numSampledNormalPackets += num;
			// TODO: whether put or not
		}
	}

	public static void addVolumeOfFlow(FlowKey flowKey, Long num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.volume = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.volume += num;
			// TODO: whether put or not
		}
		/*
		 * if (flowInfo.volume > 1000000) {
		 * System.out.println(">1000000bytes, srcid=" + flowKey.srcip); }
		 */
	}

	public static void addNormalVolumeOfFlow(FlowKey flowKey, Long num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.normalVolume = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.normalVolume += num;
			// TODO: whether put or not
		}
	}

	public static void addLostVolumeOfFlow(FlowKey flowKey, Long num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.lostVolume = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.lostVolume += num;
			// TODO: whether put or not
		}
	}

	public static void addSampledNormalVolumeOfFlow(FlowKey flowKey, Long num) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.sampledNormalVolume = num;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			flowInfo.sampledNormalVolume += num;
			// TODO: whether put or not
		}
	}

	public static int getAllPacketNumOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return 0;
		} else {
			return flowInfo.numAllPackets;
		}
	}

	public static int getNormalPacketNumOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return 0;
		} else {
			return flowInfo.numNormalPackets;
		}
	}

	public static int getLostPacketNumOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return 0;
		} else {
			return flowInfo.numLostPackets;
		}
	}

	public static int getSampledPacketNumOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return 0;
		} else {
			return flowInfo.numSampledNormalPackets;
		}
	}

	public static Long getVolumeOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return (long) 0;
		} else {
			return flowInfo.volume;
		}
	}

	public static Long getNormalVolumeOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return (long) 0;
		} else {
			return flowInfo.normalVolume;
		}
	}

	public static Long getLostVolumeOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return (long) 0;
		} else {
			return flowInfo.lostVolume;
		}
	}

	public static Long getSampledNormalVolumeOfFlow(FlowKey flowKey) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			return (long) 0;
		} else {
			return flowInfo.sampledNormalVolume;
		}
	}

	public static double getFlowLossRate(FlowKey flowKey) {
		Long volume = getVolumeOfFlow(flowKey);
		Long lostVolume = getLostVolumeOfFlow(flowKey);
		if (volume == 0) {
			return 0;
		}
		return 1.0 * lostVolume / volume;
	}

	public static void updateStartEndTimeForFlow(FlowKey flowKey, long timestamp) {
		FlowValue flowInfo = FLOW_MAP.get(flowKey);
		if (null == flowInfo) {
			flowInfo = new FlowValue();
			flowInfo.startime = timestamp;
			flowInfo.endtime = timestamp;
			FLOW_MAP.put(flowKey, flowInfo);
		} else {
			if (flowInfo.startime > timestamp) {
				flowInfo.startime = timestamp;
			}
			if (flowInfo.endtime < timestamp) {
				flowInfo.endtime = timestamp;
			}
		}
	}

	public static void analyze(ResultData resultData) {
		// 1. get target flows,
		// 2. get all held flow number
		// 3. get should held but not held flow number
		// 4.
		try {
			BufferedWriter flowMonitorWriter = new BufferedWriter(
					new FileWriter("data\\flowsToMonitor.txt"));

			int targetFlowNum = 0;
			int totalHeldFlowNum = 0;
			int targetFlowNotHeldNum = 0;
			int targetFlowHeldNum = 0;
			double totalAccuracy = 0;
			ArrayList<FlowValue> listFlowVolumeLostRate = new ArrayList<FlowValue>();
			for (Map.Entry<FlowKey, FlowValue> entry : FLOW_MAP.entrySet()) {
				FlowValue flowValue = entry.getValue();
				if (flowValue.sampledNormalVolume > 0) {
					totalHeldFlowNum += 1;
				}
				if (1 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
					// lost volume
					if (flowValue.lostVolume < TargetFlowSetting.TARGET_FLOW_LOST_VOLUME_THRESHOLD) {
						continue;
					}
				} else if (2 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE 
						|| 3 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
					// lost rate
					double rate = 1.0 * flowValue.lostVolume
							/ (flowValue.lostVolume + flowValue.normalVolume);
					if ((flowValue.lostVolume + flowValue.normalVolume) < TargetFlowSetting.TARGET_FLOW_TOTAL_VOLUME_THRESHOLD
							|| rate < TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
						continue;
					}
				}

				// target flows
				targetFlowNum += 1;

				if (flowValue.sampledNormalVolume == 0) {
					// not held
					targetFlowNotHeldNum += 1;

					// record for monitoring
					flowMonitorWriter.write("false negative:"
							+ entry.getKey().srcip + "\r\n");
				} else {
					// held
					targetFlowHeldNum += 1;
					double accuracy = (1.0 * flowValue.sampledNormalVolume / flowValue.normalVolume);
					totalAccuracy += accuracy;

					// record for monitoring
					if (accuracy > 0.9) {
						flowMonitorWriter.write("high accuracy:" + accuracy
								+ ", flow srcip:" + entry.getKey().srcip
								+ "\r\n");
					}
					if (accuracy < 0.1) {
						flowMonitorWriter.write("low accuracy:" + accuracy
								+ ", flow srcip:" + entry.getKey().srcip
								+ "\r\n");
					}
					// add into list
					listFlowVolumeLostRate.add(flowValue);
				}
			}
			flowMonitorWriter.close();

			// memory inflation
			double memoryInflation = 1.0
					* (totalHeldFlowNum - targetFlowHeldNum) / totalHeldFlowNum;

			// false negative
			double falseNegative = 1.0 * targetFlowNotHeldNum / targetFlowNum;

			// accuracy
			double accuracy = totalAccuracy / targetFlowHeldNum;

			resultData.falsePositive = memoryInflation;
			resultData.falseNegative = falseNegative;
			resultData.accuracy = accuracy;

			System.out.println("normal table size: "
					+ FixSizeHashMap.ARRAY_SIZE);
			System.out.println("collide times:" + FixSizeHashMap.collideTimes);
			System.out.println("targetFlowNum:" + targetFlowNum);
			System.out.println("totalHeldFlowNum:" + totalHeldFlowNum);
			System.out.println("targetFlowNotHeldNum:" + targetFlowNotHeldNum);
			System.out.println("targetFlowHeldNum:" + targetFlowHeldNum);

			// flow volume ~ accuracy
			Collections.sort(listFlowVolumeLostRate, new FlowValueComparable());
			
			// flow volume - loss rate
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"data\\analyzeFlowVolume_accuracy.txt"));
			long preVolume = 0;
			long cnt = 0;
			double sumAccuracy = 0;
			for (Iterator<FlowValue> iterator = listFlowVolumeLostRate
					.iterator(); iterator.hasNext();) {
				FlowValue flowValue = iterator.next();
				if (flowValue.volume != preVolume) {
					// new volume value
					if (preVolume != 0) {
						writer.write(preVolume + " " + sumAccuracy + " " + cnt
								+ " " + sumAccuracy / cnt + "  \n\r");
					}

					preVolume = flowValue.volume;
					cnt = 1;
					sumAccuracy = 1.0 * flowValue.sampledNormalVolume
							/ flowValue.normalVolume;
				} else {
					// existing volume value
					cnt += 1;
					sumAccuracy += (1.0 * flowValue.sampledNormalVolume / flowValue.normalVolume);
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public static void analyze(ResultData resultData) { //get ground truth
	 * target flows int totalPacketLost = 0; for(Map.Entry<FlowKey, Integer>
	 * entry : LOST_PACKET_MAP.entrySet()) { totalPacketLost +=
	 * entry.getValue(); } //average packet lost for lost flows double
	 * avgPacketLost = 1.0 * totalPacketLost / LOST_PACKET_MAP.size();
	 * 
	 * HashSet<Flow> targetFlows = new HashSet<Flow>(); int
	 * totalTargetFlowsPacketNum = 0; for(Map.Entry<FlowKey, Integer> entry :
	 * LOST_PACKET_MAP.entrySet()) { Flow pkg = entry.getKey(); Integer lossCnt
	 * = entry.getValue(); if (lossCnt <
	 * GlobalData.LEAST_LOSS_PACKET_IN_ONE_SECOND) { continue; }
	 * 
	 * targetFlows.add(pkg); totalTargetFlowsPacketNum +=
	 * NORMAL_PACKET_MAP.get(pkg); }
	 * 
	 * //----false positive int numFalsePosFlows = 0; int
	 * totalFalsePosFlowsPacketNum = 0; int totalHeldFlowPacketNum = 0;
	 * for(Map.Entry<FlowKey, Integer> entry :
	 * SAMPLED_NORMAL_PACKET_MAP.entrySet()){ totalHeldFlowPacketNum +=
	 * entry.getValue(); if (!targetFlows.contains(entry.getKey())) { //held
	 * flows not exist in targetFlows ++numFalsePosFlows;
	 * totalFalsePosFlowsPacketNum += entry.getValue(); } }
	 * 
	 * double falsePosRate = 1.0 * numFalsePosFlows /
	 * SAMPLED_NORMAL_PACKET_MAP.size();
	 * 
	 * int totalSuccHeldFlowPacketNum = totalHeldFlowPacketNum -
	 * totalFalsePosFlowsPacketNum;
	 * 
	 * //----false negative int numCorrectSampledFlows =
	 * SAMPLED_NORMAL_PACKET_MAP.size() - numFalsePosFlows; int numFalseNegFlows
	 * = targetFlows.size() - numCorrectSampledFlows; double falseNegRate = 1.0
	 * * numFalseNegFlows / targetFlows.size();
	 * 
	 * //----accuracy for (Iterator iterator = targetFlows.iterator();
	 * iterator.hasNext();) { Packet packet = (Packet) iterator.next();
	 * 
	 * }
	 * 
	 * //----CDF of #packetLossOfOneFlow in one time interval
	 * 
	 * 
	 * //----average packet held rate for the held flows double totalRate = 0;
	 * double numFlows = 0; ArrayList<Double> listRate = new
	 * ArrayList<Double>(); int totalHeldCnt = 0; int totalTruthCnt = 0;
	 * for(Map.Entry<Packet, Integer> entry :
	 * SAMPLED_NORMAL_PACKET_MAP.entrySet()){ Packet pkg = entry.getKey(); if
	 * (!targetFlows.contains(pkg)){ //sampled, but not targeted flows continue;
	 * }
	 * 
	 * int heldCnt = entry.getValue(); int groundTruthCnt =
	 * NORMAL_PACKET_MAP.get(pkg); double rate = 1.0 * heldCnt / groundTruthCnt;
	 * totalRate += rate; ++numFlows; listRate.add(rate); totalHeldCnt +=
	 * heldCnt; totalTruthCnt += groundTruthCnt; } double avgHeldRate =
	 * totalRate / numFlows;
	 * 
	 * double totalError = 0; for (Iterator<Double> iterator =
	 * listRate.iterator(); iterator.hasNext();) { double rate =
	 * (Double)iterator.next(); totalError += Math.pow(rate - avgHeldRate, 2); }
	 * double standardDeviation = Math.pow(totalError / numFlows, 0.5);
	 * 
	 * System.out.println("#flow:"+NORMAL_PACKET_MAP.size());
	 * System.out.println("#flow with packet loss:" + LOST_PACKET_MAP.size());
	 * System.out.println("#total lost packets:"+totalPacketLost);
	 * System.out.println("#average lost packet among lost flows:" +
	 * avgPacketLost); System.out.println("#packet lost of target flows:" +
	 * GlobalData.LEAST_LOSS_PACKET_IN_ONE_SECOND);
	 * System.out.println("#target flow:" + targetFlows.size());
	 * System.out.println("#successful targetted flow:" +
	 * (SAMPLED_NORMAL_PACKET_MAP.size() - numFalsePosFlows));
	 * System.out.println("#flow sampled:" + SAMPLED_NORMAL_PACKET_MAP.size());
	 * System.out.println("#flows false positive:" + numFalsePosFlows +
	 * ",false positive rate:" + falsePosRate);
	 * System.out.println("#flows false positive:" + numFalseNegFlows +
	 * ",false negative rate:"+falseNegRate);
	 * System.out.println("totalSuccHeldPacketCnt:" + totalHeldCnt +
	 * ", totalGroundTruthCntForPreFlows:" + totalTruthCnt);
	 * System.out.println("average packet held rate for the held flows:" +
	 * avgHeldRate + ", standard deviation:" + standardDeviation);
	 * System.out.println("#SuccHeldTargetFlowPacketNum/#TargetFlowPacketNum:" +
	 * 1.0*totalSuccHeldFlowPacketNum/totalTargetFlowsPacketNum);
	 * System.out.println("#FalsePositiveFlowPacketNum/#HeldFlowPacketNum:" +
	 * 1.0*totalFalsePosFlowsPacketNum/totalHeldFlowPacketNum);
	 * 
	 * resultData.falsePositive = falsePosRate;
	 * resultData.targetFlowPacketNumCoverage =
	 * 1.0*totalSuccHeldFlowPacketNum/totalTargetFlowsPacketNum;
	 * resultData.heldFlowFalsePacketNumCoverage =
	 * 1.0*totalFalsePosFlowsPacketNum/totalHeldFlowPacketNum; }
	 */
}
