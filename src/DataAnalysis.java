import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;

public class DataAnalysis {
	
	public static void analyzeListIntervalResults(ArrayList<ResultData> listResultDatas) {
		double totalFalsePositive = 0;
		double totalFalseNegative = 0;
		double totalAccuracy = 0;
		for (Iterator<ResultData> iterator = listResultDatas.iterator(); iterator.hasNext();) {
			ResultData resultData = iterator.next();
			totalFalsePositive += resultData.falsePositive;
			totalFalseNegative += resultData.falseNegative;
			totalAccuracy += resultData.accuracy;
		}
		
		int numResults = listResultDatas.size();
		double avgFalsePositive = totalFalsePositive / numResults;
		double avgFalseNegative = totalFalseNegative / numResults;
		double avgAccuracy = totalAccuracy / numResults;
		
		double standardDeviationMemoryPrecision = 0;
		double standardDeviationFalseNegative = 0;
		double standardDeviationAccuracy = 0;
		for (Iterator<ResultData> iterator = listResultDatas.iterator(); iterator.hasNext();) {
			ResultData resultData = iterator.next();
			standardDeviationMemoryPrecision += Math.pow(resultData.falsePositive - avgFalsePositive, 2);
			standardDeviationFalseNegative += Math.pow(resultData.falseNegative - avgFalseNegative, 2);
			standardDeviationAccuracy += Math.pow(resultData.accuracy - avgAccuracy, 2);
		}
		standardDeviationMemoryPrecision = Math.pow(standardDeviationMemoryPrecision/numResults, 0.5);
		standardDeviationFalseNegative = Math.pow(standardDeviationFalseNegative/numResults, 0.5);
		standardDeviationAccuracy = Math.pow(standardDeviationAccuracy/numResults, 0.5);
		
		//write results
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(GlobalData.RESULT_FILE_NAME, true));
			writer.write("memeorysize:" + FixSizeHashMap.ARRAY_SIZE +  " " + 
							"avgFalsePositive:" + avgFalsePositive + " " +
							"avgFalseNegative:" + avgFalseNegative + " " +
							"avgAccuracy:" + avgAccuracy + "\r\n");
			writer.write(standardDeviationMemoryPrecision + " " +
							standardDeviationFalseNegative + " " +
							standardDeviationAccuracy + "\r\n");
			writer.close();

			System.out.println( 
					"avgFalsePositive:" + avgFalsePositive + " " +
					"avgFalseNegative:" + avgFalseNegative + " " +
					"avgAccuracy:" + avgAccuracy + "\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * numMillseconds in one interval: 10 
	 */
	public static void analyzeFlowNormalLostPacketsDistribution(FlowKey flowKey, int numMillseconds) {
		try {
			BufferedReader lostReader = new BufferedReader(new FileReader("data\\lostPacketsInOneInterval.txt"));
			BufferedReader normalReader = new BufferedReader(new FileReader("data\\normalPacketsInOneInterval.txt"));
			String line = null;
			BufferedWriter flowLostWriter = new BufferedWriter(new FileWriter("data\\"+flowKey.srcip+"_lost.txt"));
			BufferedWriter flowNormalWriter = new BufferedWriter(new FileWriter("data\\"+flowKey.srcip+"_normal.txt"));
			
			long START = 21600000000L;
			long END = START + 30*1000000; //30seconds
			
			long intervalStart = START;
			long intervalEnd = START;
			long intervalLength = numMillseconds * 1000;  //ns
			while (intervalStart < END) {
				intervalStart = intervalEnd;
				intervalEnd = intervalStart + intervalLength;
				int lostVolume = 0;			//volume in the interval
				int normalVolume = 0;
				
				while ((line = lostReader.readLine()) != null) {
					Packet packet = Packet.parsePacket(line);
					if (null == packet) {
						continue;
					}
					if (packet.microsec < intervalStart) {
						continue;
					} else if (packet.microsec <= intervalEnd) {
						if (packet.srcip == flowKey.srcip) {
							lostVolume += packet.length;	
						}
					} else {
						break;
					}
				}
				
				while ((line = normalReader.readLine()) != null) {
					Packet packet = Packet.parsePacket(line);
					if (null == packet) {
						continue;
					}
					if (packet.microsec < intervalStart) {
						continue;
					} else if (packet.microsec <= intervalEnd) {
						if (packet.srcip == flowKey.srcip) {
							normalVolume+= packet.length;	
						}
					} else {
						break;
					}
				}
				
				flowLostWriter.write(intervalStart + " " + lostVolume + " \n\r");
				flowNormalWriter.write(intervalStart + " " + normalVolume + " \n\r");
			}
			lostReader.close();
			normalReader.close();
			flowLostWriter.close();
			flowNormalWriter.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally{
		}
	}
	
	public static void analyzeFlowDataDistributionInOneInterval() {
		
		ArrayList<Integer> listFlowAllPacketNum = new ArrayList<Integer>();
		ArrayList<Long> listFlowVolume = new ArrayList<Long>();
		ArrayList<Double> listFlowAvgPackgeLength = new ArrayList<Double>();
		ArrayList<Long> listFlowLostVolume = new ArrayList<Long>();
		ArrayList<Double> listFlowLostRate = new ArrayList<Double>();
		ArrayList<Long> listFlowDuration = new ArrayList<Long>();
		ArrayList<FlowValue> listFlowVolumeLostRate = new ArrayList<FlowValue>();
		
		Reader reader = new Reader();
		reader.readIthIntervalPackets(0);
		if (GlobalData.DEBUG){
			System.out.println("srcip=805469142, flowvolume="+Controller.FLOW_MAP.get(new FlowKey(805469142L)).volume);	
		}
		
		//get totalPacketsNum
		int totalPacketsNum = 0;
		int totalFlowVolumePkgNum1k = 0;
		int totalFlowPacketsPkgNum1k = 0;
		int totalFlowVolumeForVolume100k = 0;
		int totalFlowNumForVolume100k = 0;
		for (Entry<FlowKey, FlowValue> pair : Controller.FLOW_MAP.entrySet()) {
			FlowValue flowValue = pair.getValue();
			listFlowAllPacketNum.add(flowValue.numAllPackets);
			listFlowVolume.add(flowValue.volume);
			listFlowDuration.add(flowValue.endtime - flowValue.startime);
			listFlowVolumeLostRate.add(flowValue);
			
			totalPacketsNum += (flowValue.numAllPackets);
			
			listFlowAvgPackgeLength.add(1.0 * flowValue.volume/flowValue.numAllPackets);
			if (flowValue.numAllPackets > 1000) {
				totalFlowVolumePkgNum1k += flowValue.volume;
				totalFlowPacketsPkgNum1k += flowValue.numAllPackets;
			}
			if (flowValue.volume > 100000) {
				totalFlowVolumeForVolume100k += flowValue.volume;
				totalFlowNumForVolume100k += 1;
			}
			
			listFlowLostVolume.add(flowValue.lostVolume);
			listFlowLostRate.add(1.0 * flowValue.lostVolume / flowValue.volume);
		}//for
		System.out.println("totalPacketsNum:" + totalPacketsNum);
		System.out.println("totalVolumeForFlowWithPkgLargerThan1k:" + totalFlowVolumePkgNum1k);
		System.out.println("totalPacketNumForFlowWithPkgLargerThan1k:" + totalFlowPacketsPkgNum1k);
		System.out.println("totalFlowNumForVolume100k:" + totalFlowNumForVolume100k
				+ ", totalFlowVolumeForVolume100k:" + totalFlowVolumeForVolume100k);
		
		Collections.sort(listFlowAllPacketNum);
		Collections.sort(listFlowVolume);
		Collections.sort(listFlowAvgPackgeLength);
		Collections.sort(listFlowLostVolume);
		Collections.sort(listFlowLostRate);
		Collections.sort(listFlowDuration);
		Collections.sort(listFlowVolumeLostRate, new FlowValueComparable());
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data\\analyzeFlowPacketNumDistributionInOneInterval.txt"));
			for (double i = 0; i <= 1; i += 0.0001) {
				int percentIndex = (int)( i * listFlowAllPacketNum.size());
				if (percentIndex >= listFlowAllPacketNum.size()) {
					percentIndex -= 1;
					if (percentIndex >= listFlowAllPacketNum.size()) {
						break;
					}
				}
				int packetNumForTheFlow = listFlowAllPacketNum.get(percentIndex);
				writer.write(packetNumForTheFlow + "  " + i + "  \n\r");
			}
			writer.close();
			
			writer = new BufferedWriter(new FileWriter("data\\analyzeFlowVolumeDistributionInOneInterval.txt"));
			for (double i = 0; i <= 1; i += 0.0001) {
				int percentIndex = (int)( i * listFlowVolume.size());
				if (percentIndex >= listFlowVolume.size()) {
					percentIndex -= 1;
					if (percentIndex >= listFlowVolume.size()) {
						break;
					}
				}
				Long flowVolume = listFlowVolume.get(percentIndex);
				writer.write(flowVolume + "  " + i + "  \n\r");
			}
			writer.close();
			
			writer = new BufferedWriter(new FileWriter("data\\analyzeFlowAvgPacketLength.txt"));
			for (double i = 0; i <= 1; i += 0.0001) {
				int percentIndex = (int)( i * listFlowAvgPackgeLength.size());
				if (percentIndex >= listFlowAvgPackgeLength.size()) {
					percentIndex -= 1;
					if (percentIndex >= listFlowAvgPackgeLength.size()) {
						break;
					}
				}
				Double avgPacketLength = listFlowAvgPackgeLength.get(percentIndex);
				writer.write(avgPacketLength + "  " + i + "  \n\r");
			}
			writer.close();
			
			writer = new BufferedWriter(new FileWriter("data\\analyzeFlowLostVolume.txt"));
			for (double i = 0; i <= 1; i += 0.0001) {
				int percentIndex = (int)( i * listFlowLostVolume.size());
				if (percentIndex >= listFlowLostVolume.size()) {
					percentIndex -= 1;
					if (percentIndex >= listFlowLostVolume.size()) {
						break;
					}
				}
				Long lostVolume = listFlowLostVolume.get(percentIndex);
				writer.write(lostVolume + "  " + i + "  \n\r");
			}
			writer.close();
			
			writer = new BufferedWriter(new FileWriter("data\\analyzeFlowLostRate.txt"));
			for (double i = 0; i <= 1; i += 0.0001) {
				int percentIndex = (int)( i * listFlowLostRate.size());
				if (percentIndex >= listFlowLostRate.size()) {
					percentIndex -= 1;
					if (percentIndex >= listFlowLostRate.size()) {
						break;
					}
				}
				Double lostRate = listFlowLostRate.get(percentIndex);
				writer.write(lostRate + "  " + i + "  \n\r");
			}
			writer.close();
			
			//flow volume - loss rate
			writer = new BufferedWriter(new FileWriter("data\\analyzeFlowVolume_lossRate.txt"));
			long preVolume = 0;
			long cnt = 0;
			double sumLossRate = 0;
			for (Iterator<FlowValue> iterator = listFlowVolumeLostRate.iterator(); iterator.hasNext();) {
				FlowValue flowValue = iterator.next();
				if (flowValue.volume != preVolume) {
					//new volume value
					if (preVolume != 0) {
						writer.write(preVolume + " " + sumLossRate + " " + cnt + " " + sumLossRate / cnt + "  \n\r");
					}
					
					preVolume = flowValue.volume;
					cnt = 1;
					sumLossRate = 1.0 * flowValue.lostVolume / flowValue.volume;
				} else {
					//existing volume value
					cnt += 1;
					sumLossRate += (1.0 * flowValue.lostVolume / flowValue.volume);
				}
			}		
			writer.close();
			
			writer = new BufferedWriter(new FileWriter("data\\analyzeFlowDuration.txt"));
			for (double i = 0; i <= 1; i += 0.001) {
				int percentIndex = (int)( i * listFlowDuration.size());
				if (percentIndex >= listFlowDuration.size()) {
					percentIndex -= 1;
					if (percentIndex >= listFlowDuration.size()) {
						break;
					}
				}
				Long duration = listFlowDuration.get(percentIndex);
				writer.write(duration + "  " + i + "  \n\r");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}