package singleswitch.main;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import singleswitch.controller.Controller;
import singleswitch.data.FixSizeHashMap;
import singleswitch.data.ResultData;
import singleswitch.fileReader.Reader;
import singleswitch.sampleModel.PacketSampleModelExponential;
import singleswitch.sampleModel.PacketSampleModelLinear;
import singleswitch.sampleModel.PacketSampleModelLog;
import singleswitch.sampleModel.PacketSampleModelPolynomial;
import singleswitch.sampleModel.PacketSampleModelTraditional;
import singleswitch.sampleModel.PacketSampleSetting;
import singleswitch.switcher.Switch;

public class MainTask {
	public static void RunIntervalsSingle() {
		// run several Intervals, and get resultData for each Interval.
		ArrayList<ResultData> listResultDatas = new ArrayList<ResultData>();
		for (int ithInterval = 0; ithInterval < GlobalSetting.SIMULATE_INVERVALS; ithInterval++) {
			System.out.println("IthInterval:" + ithInterval);
			Reader reader = new Reader();
			Switch switch1 = new Switch();
			GlobalData.Instance().clear();
			ResultData resultData = new ResultData(0, 0, 0, 0, 0);

			reader.readIthIntervalPackets(ithInterval);
			switch1.runDataInList();
			switch1.sendDataToControllerAndClearStatus();
			Switch.LIST_PACKETS.clear();
			Switch.PACKET_QUEUE.clear();
			Controller.analyze(resultData);
			FixSizeHashMap.collideTimes = 0;
			Controller.clear();
			listResultDatas.add(resultData);
		}
		DataAnalysis.analyzeListIntervalResults(listResultDatas);
	}

	public static void RunIntervalsChangeLossRatioThreshold() {
		// HashMap<Integer, ArrayList<ResultData>> listResultDataMap = new
		// HashMap<Integer, ArrayList<ResultData>>();

		HashMap<Integer, HashMap<Integer, ArrayList<ResultData>>> listMethodResultDataMap = new HashMap<Integer, HashMap<Integer, ArrayList<ResultData>>>();

		int startMethod = 0;
		int endMethod = 0;

		// run several Intervals, and get resultData for each Interval.
		for (int ithInterval = 0; ithInterval < GlobalSetting.SIMULATE_INVERVALS; ithInterval++) {
			System.out.println("IthInterval:" + ithInterval);
			Reader reader = new Reader();
			reader.readIthIntervalPackets(ithInterval);

			for (GlobalSetting.METHOD_NUMBER = startMethod; GlobalSetting.METHOD_NUMBER <= endMethod; GlobalSetting.METHOD_NUMBER++) {
				for (int i = 1; i <= 4; i++) {
					TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD = 0.1 * i;
					System.out.println("loss rate:"
							+ TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD);
					GlobalData.Instance().clear();
					Switch switch1 = new Switch();
					switch1.runDataInList();
					switch1.sendDataToControllerAndClearStatus();
					ResultData resultData = new ResultData(0, 0, 0, 0, 0);
					Controller.analyze(resultData);
					FixSizeHashMap.collideTimes = 0;

					HashMap<Integer, ArrayList<ResultData>> listResultDataMap = listMethodResultDataMap
							.get(GlobalSetting.METHOD_NUMBER);
					if (listResultDataMap == null) {
						listResultDataMap = new HashMap<Integer, ArrayList<ResultData>>();
						listMethodResultDataMap.put(GlobalSetting.METHOD_NUMBER,
								listResultDataMap);
					}

					ArrayList<ResultData> listResultDatas = listResultDataMap
							.get(i);
					if (listResultDatas == null) {
						listResultDatas = new ArrayList<ResultData>();
						listResultDataMap.put(i, listResultDatas);
					}
					listResultDatas.add(resultData);

					Controller.clearSwitchData();
				}
			}

			Controller.clear();
			Switch.LIST_PACKETS.clear();
			Switch.PACKET_QUEUE.clear();
		}

		for (GlobalSetting.METHOD_NUMBER = startMethod; GlobalSetting.METHOD_NUMBER <= endMethod; GlobalSetting.METHOD_NUMBER++) {

			GlobalSetting.RESULT_FILE_NAME = "data\\intervalResults"
					+ "Volume_threshold_"
					+ GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO
					+ "_DiffLossRatioMethod" + GlobalSetting.METHOD_NUMBER
					+ ".txt";
			HashMap<Integer, ArrayList<ResultData>> listResultDataMap = listMethodResultDataMap
					.get(GlobalSetting.METHOD_NUMBER);
			try {
				BufferedWriter writer;
				writer = new BufferedWriter(new FileWriter(
						GlobalSetting.RESULT_FILE_NAME, true));
				writer.write("method:" + GlobalSetting.METHOD_NUMBER + "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 1; i <= 4; i++) {
				ArrayList<ResultData> listResultDatas = listResultDataMap
						.get(i);

				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(
							GlobalSetting.RESULT_FILE_NAME, true));
					writer.write("loss ratio threshold:" + 0.1 * i + "\n");
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// compute the result
				DataAnalysis.analyzeListIntervalResults(listResultDatas);
			}
		}

	}

	public static void RunIntervalsChangeLossRatioThresholdChangeCalculateVolumeForMethod2() {
		// HashMap<Integer, ArrayList<ResultData>> listResultDataMap = new
		// HashMap<Integer, ArrayList<ResultData>>();
		GlobalSetting.METHOD_NUMBER = 2;

		HashMap<Long, HashMap<Integer, ArrayList<ResultData>>> listMethodResultDataMap = new HashMap<Long, HashMap<Integer, ArrayList<ResultData>>>();

		// long[] volumeThresholds= {0, 100, 1000, 10000, 100000};
		// long[] volumeThresholds= {1000000, 5000000};
		long[] volumeThresholds = { 0 };

		// run several Intervals, and get resultData for each Interval.
		for (int ithInterval = 0; ithInterval < GlobalSetting.SIMULATE_INVERVALS; ithInterval++) {
			System.out.println("IthInterval:" + ithInterval);
			Reader reader = new Reader();
			reader.readIthIntervalPackets(ithInterval);

			for (int k = 0; k < volumeThresholds.length; ++k) {
				GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO = volumeThresholds[k];
				for (int i = 1; i <= 4; i++) {
					TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD = 0.1 * i;
					System.out.println("loss rate:"
							+ TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD);
					GlobalData.Instance().clear();
					Switch switch1 = new Switch();
					switch1.runDataInList();
					switch1.sendDataToControllerAndClearStatus();
					ResultData resultData = new ResultData(0, 0, 0, 0, 0);
					Controller.analyze(resultData);
					FixSizeHashMap.collideTimes = 0;

					HashMap<Integer, ArrayList<ResultData>> listResultDataMap = listMethodResultDataMap
							.get(GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO);
					if (listResultDataMap == null) {
						listResultDataMap = new HashMap<Integer, ArrayList<ResultData>>();
						listMethodResultDataMap
								.put(GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO,
										listResultDataMap);
					}

					ArrayList<ResultData> listResultDatas = listResultDataMap
							.get(i);
					if (listResultDatas == null) {
						listResultDatas = new ArrayList<ResultData>();
						listResultDataMap.put(i, listResultDatas);
					}
					listResultDatas.add(resultData);

					Controller.clearSwitchData();
				}
			}

			Controller.clear();
			Switch.LIST_PACKETS.clear();
			Switch.PACKET_QUEUE.clear();
		}

		for (int k = 0; k < volumeThresholds.length; ++k) {
			GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO = volumeThresholds[k];

			GlobalSetting.RESULT_FILE_NAME = "data\\intervalResults"
					+ "Volume_threshold_"
					+ GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO
					+ "DiffLossRatioMethod" + GlobalSetting.METHOD_NUMBER + ".txt";
			HashMap<Integer, ArrayList<ResultData>> listResultDataMap = listMethodResultDataMap
					.get(GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO);
			try {
				BufferedWriter writer;
				writer = new BufferedWriter(new FileWriter(
						GlobalSetting.RESULT_FILE_NAME, true));
				writer.write("normal volume threshold for compute loss ratio:"
						+ GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO
						+ "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 1; i <= 4; i++) {
				ArrayList<ResultData> listResultDatas = listResultDataMap
						.get(i);

				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter(
							GlobalSetting.RESULT_FILE_NAME, true));
					writer.write("loss ratio threshold:" + 0.1 * i + "\n");
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// compute the result
				DataAnalysis.analyzeListIntervalResults(listResultDatas);
			}
		}

	}

	public static void RunIntervalsChangeHashtableSize(int methodNumber) {
		GlobalSetting.RESULT_FILE_NAME = "data\\intervalResults"
				+ "Volume_threshold_"
				+ GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO
				+ "DiffHashTableMethod" + methodNumber + ".txt";

		int[] tableSizes = { 10007, 105943, 1000003, 9999991 };
		HashMap<Integer, ArrayList<ResultData>> listResultDataMap = new HashMap<Integer, ArrayList<ResultData>>();

		// run several Intervals, and get resultData for each Interval.
		for (int ithInterval = 0; ithInterval < GlobalSetting.SIMULATE_INVERVALS; ithInterval++) {
			System.out.println("IthInterval:" + ithInterval);
			Reader reader = new Reader();
			reader.readIthIntervalPackets(ithInterval);

			for (int i = 0; i < tableSizes.length; i++) {
				FixSizeHashMap.ARRAY_SIZE = tableSizes[i];
				System.out.println("table size:" + FixSizeHashMap.ARRAY_SIZE);
				GlobalData.Instance().clear();
				Switch switch1 = new Switch();
				switch1.runDataInList();
				switch1.sendDataToControllerAndClearStatus();
				ResultData resultData = new ResultData(0, 0, 0, 0, 0);
				Controller.analyze(resultData);
				FixSizeHashMap.collideTimes = 0;

				ArrayList<ResultData> listResultDatas = listResultDataMap
						.get(i);
				if (listResultDatas == null) {
					listResultDatas = new ArrayList<ResultData>();
					listResultDataMap.put(i, listResultDatas);
				}
				listResultDatas.add(resultData);

				Controller.clearSwitchData();
			}
			Controller.clear();
			Switch.LIST_PACKETS.clear();
			Switch.PACKET_QUEUE.clear();
		}

		for (int i = 0; i < tableSizes.length; i++) {
			ArrayList<ResultData> listResultDatas = listResultDataMap.get(i);

			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(
						GlobalSetting.RESULT_FILE_NAME, true));
				writer.write("hashtable size:" + tableSizes[i] + "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// compute the result
			DataAnalysis.analyzeListIntervalResults(listResultDatas);
		}

	}

	public static void RunIntervalsChangeWaitTimeMethod3() {
		GlobalSetting.METHOD_NUMBER = 3;

		HashMap<Integer, HashMap<Integer, ArrayList<ResultData>>> listMethodResultDataMap = new HashMap<Integer, HashMap<Integer, ArrayList<ResultData>>>();

		// run several Intervals, and get resultData for each Interval.
		for (int ithInterval = 0; ithInterval < GlobalSetting.SIMULATE_INVERVALS; ithInterval++) {
			System.out.println("IthInterval:" + ithInterval);
			Reader reader = new Reader();
			reader.readIthIntervalPackets(ithInterval);

			for (int i = 2; i <= 6; i += 2) {
				GlobalSetting.NUMBER_MICSECONDS_TO_WAIT_BEFORE_DELETE = Math.pow(
						10, i);
				System.out.println("number of micseconds to wait:"
						+ GlobalSetting.NUMBER_MICSECONDS_TO_WAIT_BEFORE_DELETE);

				HashMap<Integer, ArrayList<ResultData>> listResultDataMap = listMethodResultDataMap
						.get(i);
				if (listResultDataMap == null) {
					listResultDataMap = new HashMap<Integer, ArrayList<ResultData>>();
					listMethodResultDataMap.put(i, listResultDataMap);
				}

				for (int j = 1; j <= 4; j++) {
					TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD = 0.1 * j;
					GlobalData.Instance().clear();
					Switch switch1 = new Switch();
					switch1.runDataInList();
					switch1.sendDataToControllerAndClearStatus();
					ResultData resultData = new ResultData(0, 0, 0, 0, 0);
					Controller.analyze(resultData);
					FixSizeHashMap.collideTimes = 0;

					ArrayList<ResultData> listResultDatas = listResultDataMap
							.get(j);
					if (listResultDatas == null) {
						listResultDatas = new ArrayList<ResultData>();
						listResultDataMap.put(j, listResultDatas);
					}
					listResultDatas.add(resultData);

					Controller.clearSwitchData();
				}
			}
			Controller.clear();
			Switch.LIST_PACKETS.clear();
			Switch.PACKET_QUEUE.clear();
		}

		for (int i = 2; i <= 6; i += 2) {
			GlobalSetting.RESULT_FILE_NAME = "data\\intervalResults" + "waitTime_"
					+ Math.pow(10, i) + "_" + GlobalSetting.METHOD_NUMBER + ".txt";
			try {
				BufferedWriter writer;
				writer = new BufferedWriter(new FileWriter(
						GlobalSetting.RESULT_FILE_NAME, true));
				writer.write("number_of_microseconds_to_wait:"
						+ Math.pow(10, i) + "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HashMap<Integer, ArrayList<ResultData>> listResultDataMap = listMethodResultDataMap
					.get(i);

			for (int j = 1; j <= 4; j++) {
				TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD = 0.1 * j;
				ArrayList<ResultData> listResultDatas = listResultDataMap
						.get(j);

				try {
					BufferedWriter writer;
					writer = new BufferedWriter(new FileWriter(
							GlobalSetting.RESULT_FILE_NAME, true));
					writer.write("loss_rate:"
							+ TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD + "\n");
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// compute the result
				DataAnalysis.analyzeListIntervalResults(listResultDatas);
			}
		}

	}

	public static void RunIntervalsDiffSampleModels() {
		// GlobalData.IS_USE_REPLACE_MECHANISM = 0;
		// GlobalData.OBJECT_VOLUME_OR_RATE = 1;
		// GlobalData.DEAFULT_BYTE_SAMPLE_RATE = 4e-5;
		// FixSizeHashMap.ARRAY_SIZE = 240007;

		String[] modelNamesStrings = { "exponential", "polynomial", "log",
				"linear", "traditional" };
		HashMap<Integer, ArrayList<ResultData>> listResultDataMap = new HashMap<Integer, ArrayList<ResultData>>();

		// run several Intervals, and get resultData for each Interval.
		for (int ithInterval = 0; ithInterval < GlobalSetting.SIMULATE_INVERVALS; ithInterval++) {
			System.out.println("IthInterval:" + ithInterval);
			Reader reader = new Reader();
			reader.readIthIntervalPackets(ithInterval);

			for (int i = 0; i < 1; i++) {
				System.out.println("table size:" + FixSizeHashMap.ARRAY_SIZE);
				GlobalData.Instance().clear();
				Switch switch1 = new Switch();
				if (0 == i) {
					switch1.packetSampleModel = new PacketSampleModelExponential(
							switch1.lostFlowVolumeMap,
							switch1.sampledFlowVolumeMap);
				} else if (1 == i) {
					switch1.packetSampleModel = new PacketSampleModelPolynomial(
							switch1.lostFlowVolumeMap,
							switch1.sampledFlowVolumeMap);
				} else if (2 == i) {
					switch1.packetSampleModel = new PacketSampleModelLog(
							switch1.lostFlowVolumeMap,
							switch1.sampledFlowVolumeMap);
				} else if (3 == i) {
					switch1.packetSampleModel = new PacketSampleModelLinear(
							switch1.lostFlowVolumeMap,
							switch1.sampledFlowVolumeMap);
				} else if (4 == i) {
					switch1.packetSampleModel = new PacketSampleModelTraditional(
							switch1.lostFlowVolumeMap,
							switch1.sampledFlowVolumeMap);
				}
				switch1.runDataInList();
				switch1.sendDataToControllerAndClearStatus();
				ResultData resultData = new ResultData(0, 0, 0, 0, 0);
				Controller.analyze(resultData);
				FixSizeHashMap.collideTimes = 0;

				ArrayList<ResultData> listResultDatas = listResultDataMap
						.get(i);
				if (listResultDatas == null) {
					listResultDatas = new ArrayList<ResultData>();
					listResultDataMap.put(i, listResultDatas);
				}
				listResultDatas.add(resultData);

				Controller.clearSwitchData();
			}

			Controller.clear();
			Switch.LIST_PACKETS.clear();
			Switch.PACKET_QUEUE.clear();
		}

		GlobalSetting.RESULT_FILE_NAME = "data\\intervalResults"
				+ "_DiffModel__prob_" + PacketSampleSetting.DEAFULT_BYTE_SAMPLE_RATE
				+ ".txt";
		for (int i = 0; i < 5; i++) {
			ArrayList<ResultData> listResultDatas = listResultDataMap.get(i);
			try {
				BufferedWriter writer;
				writer = new BufferedWriter(new FileWriter(
						GlobalSetting.RESULT_FILE_NAME, true));
				writer.write(modelNamesStrings[i] + "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// compute the result
			DataAnalysis.analyzeListIntervalResults(listResultDatas);
		}

	}

	public static void main(String[] args) {

		RunIntervalsSingle();
		//RunIntervalsDiffSampleModels();
		// RunIntervalsChangeLossRatioThreshold();
		// RunIntervalsChangeLossRatioThresholdChangeCalculateVolumeForMethod2();
		// RunIntervalsChangeWaitTimeMethod3();

		for (GlobalSetting.METHOD_NUMBER = 1; GlobalSetting.METHOD_NUMBER <= 3; GlobalSetting.METHOD_NUMBER++) {
			// RunIntervalsChangeHashtableSize(GlobalData.METHOD_NUMBER);
			// RunIntervalsChangeWaitTime(GlobalData.METHOD_NUMBER);
		}
		// RunIntervals();

		// FlowKey flowKey = new FlowKey(2454973305L);
		// DataAnalysis.analyzeFlowNormalLostPacketsDistribution(flowKey, 100);
		// //10ms per interval

		// DataAnalysis.analyzeFlowDataDistributionInOneInterval();

		/*
		 * Read one interval data, then calculate multiple times based on the
		 * data then, read next interval data, then calculate...
		 * 
		 * Reader reader = new Reader(); Switch switch1 = new Switch();
		 * ResultData[][] resultDatas = new
		 * ResultData[GlobalData.DEFAULT_DELTA_BYTE_SAMPLE_RATE_INCREASE_TIMES
		 * ][GlobalData.SIMULATE_INVERVALS];
		 * 
		 * for (int i = 0; i < GlobalData.SIMULATE_INVERVALS; i++) { //clear
		 * listPackets for this SIMULATE_INTERVAL Switch.listPackets.clear();
		 * 
		 * //read packets belonging to this SIMULATE_INTERVAL if
		 * (reader.getIthIntervalData(i) != 0) {
		 * System.out.print("read data for " + i + "th interval failed"); break;
		 * }
		 * 
		 * //do experiments for changing DEFAULT_DELTA_BYTE_SAMPLE_RATE for (int
		 * j = 0; j < GlobalData.DEFAULT_DELTA_BYTE_SAMPLE_RATE_INCREASE_TIMES;
		 * j++) { GlobalData.DEFAULT_DELTA_BYTE_SAMPLE_RATE *= Math.pow(10, j);
		 * switch1.runOneInterval();
		 * switch1.sendDataToControllerAndClearStatus();
		 * 
		 * //do statistics for the <ith interval, certain
		 * delta_byte_sampling_rate> pair //clear all data in Controller
		 * ResultData resultDataOneInterval = new ResultData(0, 0);
		 * Controller.analyze(resultDataOneInterval); Controller.clear();
		 * 
		 * resultDatas[i][j] = resultDataOneInterval; } }
		 */

		/*
		 * One thread read, one thread calculate *
		 * 
		 * (new Thread(new Switch())).start(); System.out.println("switch run");
		 * (new Thread(new ReadThread())).start();
		 * System.out.println("read run");
		 */

		/*
		 * read all data first, then calculate multiple times based on the data
		 * 
		 * ReadThread readThread = new ReadThread(); readThread.run();
		 * 
		 * for (GlobalData.DEFAULT_DELTA_BYTE_SAMPLE_RATE = 1.7e-10;
		 * GlobalData.DEFAULT_DELTA_BYTE_SAMPLE_RATE <=1;
		 * GlobalData.DEFAULT_DELTA_BYTE_SAMPLE_RATE*=10) {
		 * ArrayList<ResultData> listResultData = new ArrayList<ResultData>();
		 * 
		 * int tryTimes = 5;
		 * 
		 * for(int i = 0; i < tryTimes; ++i){
		 * Controller.SAMPLED_NORMAL_PACKET_MAP.clear(); ResultData resultData =
		 * new ResultData(0, 0, 0); Switch switch1 = new Switch();
		 * switch1.run2(resultData); listResultData.add(resultData); }
		 * 
		 * double totalFalsePositive = 0; double totalCoverage = 0; double
		 * totalError = 0; for (ResultData resultData : listResultData) {
		 * totalFalsePositive += resultData.falsePositive; totalCoverage +=
		 * resultData.targetFlowPacketNumCoverage; totalError +=
		 * resultData.heldFlowFalsePacketNumCoverage; }
		 * 
		 * double avgFalsePositive = totalFalsePositive / tryTimes; double
		 * avgCoverage = totalCoverage / tryTimes; double avgError = totalError
		 * / tryTimes;
		 * 
		 * System.out.println("result:"+GlobalData.DEFAULT_DELTA_BYTE_SAMPLE_RATE
		 * + "," + avgFalsePositive + "," + avgCoverage + "," + avgError); }
		 */

		/*
		 * HashMap<Packet, Integer> map = new HashMap<Packet, Integer>(); Packet
		 * pkg = new Packet(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, 0,
		 * 0, (short) 0, 0, Packet.MSG_END); map.put(pkg, 1); Packet pkg2 = new
		 * Packet(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, 0, 0, (short)
		 * 0, 0, Packet.MSG_END); System.out.println(map.get(pkg2));
		 */
	}

}
