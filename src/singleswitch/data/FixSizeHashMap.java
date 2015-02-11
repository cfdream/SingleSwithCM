package singleswitch.data;
import java.util.ArrayList;
import java.util.HashMap;

import singleswitch.main.GlobalSetting;
import singleswitch.main.TargetFlowSetting;

public class FixSizeHashMap {
	public static int ARRAY_SIZE = 1200007; // 10007 105943, 1000003,
											// 1200007(1.2M) 13567(13k)
											// 240007(240k)

	public static int collideTimes = 0;
	Record[] entries;

	public FixSizeHashMap() {
		super();
		entries = new Record[ARRAY_SIZE];
		clear();
		collideTimes = 0;
	}

	public void clear() {
		for (int i = 0; i < entries.length; i++) {
			entries[i] = null;
		}
	}

	public int getKey(FlowKey flowKey) {
		int idx = (int) (flowKey.srcip % ARRAY_SIZE);
		return idx;
	}

	public Long get(FlowKey flowKey) {
		int idx = getKey(flowKey);
		if (null == entries[idx]) {
			return null;
		} else if (entries[idx].flowKey.srcip != flowKey.srcip) {
			/*
			 * if (++collideTimes % 10000 == 0) {
			 * System.out.println(collideTimes); }
			 */
			return null;
		} else {
			return entries[idx].value;
		}
	}

	public void put(FlowKey flowKey, long value) {
		int idx = getKey(flowKey);
		Record newEntry = new Record(flowKey, value);
		if (entries[idx] != null && entries[idx].flowKey.srcip != flowKey.srcip) {
			if (++collideTimes % 1000 == 0) {
				System.out.println("collideTime:" + collideTimes);
			}
		}
		entries[idx] = newEntry;
	}

	public void put(FlowKey flowKey, long value, Long timestamp,
			HashMap<FlowKey, Long> lostFlowVolumeMap) {
		if (GlobalSetting.METHOD_NUMBER == 0) {
			put(flowKey, value);
			return;
		}

		int idx = getKey(flowKey);
		if (null == entries[idx]) {
			// 1. first time to use the entry, starttime= timestamp
			Record newEntry = new Record(flowKey, value, timestamp);
			entries[idx] = newEntry;
			return;
		}
		// there is already the same entry in the bucket
		if (entries[idx].flowKey.srcip == flowKey.srcip) {
			// 2. flowKey already in the bucket, starttime should not be changed
			Record newEntry = new Record(flowKey, value, entries[idx].starttime);
			entries[idx] = newEntry;
			return;
		}

		Record newEntry = new Record(flowKey, value, timestamp);

		// 3. the bucket is already occupied by another flow
		// -----------replace mechanism---------------
		Long starttime = entries[idx].starttime;
		Long normalVolume = entries[idx].value;
		Long lostVolume = lostFlowVolumeMap.get(entries[idx].flowKey);
		if (null == lostVolume) {
			lostVolume = 0L;
		}
		Long totalVolume = lostVolume + normalVolume;
		double lossRate = 1.0 * lostVolume / totalVolume;

		if (1 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
			// ------target is volume
			double avgLossSpeed = TargetFlowSetting.TARGET_FLOW_LOST_VOLUME_THRESHOLD
					/ GlobalSetting.INTERVAL_SECONDS;
			double lossSpeed = lostVolume
					/ (1.0 * (timestamp - starttime) / GlobalSetting.SECOND_2_USECOND);
			if (lostVolume > TargetFlowSetting.TARGET_FLOW_LOST_VOLUME_THRESHOLD
					|| lossSpeed >= avgLossSpeed) {
				// 3.1 lossVolume(existing flow) > threshold
				// keep the existing flow, skip the new flow
				return;
			}
			entries[idx] = newEntry;
			return;
		} else if (2 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
			// ------target is loss rate
			if (GlobalSetting.METHOD_NUMBER == 1) {
				// method1
				if (lossRate < TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
					// 3.1 lossRate(existing flow) < threshold
					// keep the existing flow, skip the new flow
					entries[idx] = newEntry;
					return;
				}
			}

			// method2
			if (GlobalSetting.METHOD_NUMBER == 2) {
				if (totalVolume > GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO
						&& lossRate < TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
					// 3.1 lossRate(existing flow) < threshold
					// keep the existing flow, skip the new flow
					entries[idx] = newEntry;
					return;
				}
			}
			if (GlobalSetting.METHOD_NUMBER == 3) {
				if (timestamp - starttime > GlobalSetting.NUMBER_MICSECONDS_TO_WAIT_BEFORE_DELETE
						&& lossRate < TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
					// 3.3 totalVolume for existing flow is not big enough
					// the flow occupy the bucket larger than
					// NUMBER_SECONDS_TO_WAIT_BEFORE_DELETE, discard it.
					entries[idx] = newEntry;
					return;
				}
			}
			if (GlobalSetting.METHOD_NUMBER == 4) {
				if (totalVolume > GlobalSetting.NORMAL_VOLUME_THRESHOLD_FOR_COMPUTE_LOSS_RATIO
						&& lossRate < TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
					// 3.1 lossRate(existing flow) < threshold
					// keep the existing flow, skip the new flow
					entries[idx] = newEntry;
					return;
				}
				if (timestamp - starttime > GlobalSetting.SECOND_2_USECOND
						* GlobalSetting.NUMBER_MICSECONDS_TO_WAIT_BEFORE_DELETE
						&& lossRate < TargetFlowSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
					// 3.3 totalVolume for existing flow is not big enough
					// the flow occupy the bucket larger than
					// NUMBER_SECONDS_TO_WAIT_BEFORE_DELETE, discard it.
					entries[idx] = newEntry;
					return;
				}
			}
		}// end else if (2 == GlobalData.OBJECT_VOLUME_OR_RATE)
		else if (3 == TargetFlowSetting.OBJECT_VOLUME_OR_RATE) {
			//TODO:
		}
	}

	public ArrayList<Record> getAllEntries() {
		ArrayList<Record> records = new ArrayList<Record>();
		for (int i = 0; i < entries.length; i++) {
			if (entries[i] == null) {
				continue;
			}
			records.add(entries[i]);
		}
		return records;
	}

	public class Record {
		public FlowKey flowKey;
		public Long value;
		public Long starttime;

		public Record(FlowKey flowKeyPara, long valuePara) {
			this.flowKey = flowKeyPara;
			this.value = valuePara;
		}

		public Record(FlowKey flowKeyPara, long valuePara, long starttimePara) {
			this.flowKey = flowKeyPara;
			this.value = valuePara;
			this.starttime = starttimePara;
		}
	}
}
