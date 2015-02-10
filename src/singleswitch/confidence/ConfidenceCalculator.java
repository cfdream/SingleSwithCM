package singleswitch.confidence;

import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import singleswitch.main.GlobalSetting;

public class ConfidenceCalculator {
	public static double calculateConfidence(ArrayList<Double> valueList) {
		if (valueList.size() == 1) {
			return 0.5;			//only one packet is lost, no enough confidence
		}
		//add all values
		SummaryStatistics stats = new SummaryStatistics();
		for (Double value : valueList) {
			stats.addValue(value);
		}
		//get delta = abs(mean-lossRateThreshold) / (standard deviation of mean)
		double delta = 0;
		if (stats.getStandardDeviation() == 0) {
			delta = Double.MAX_VALUE;
		} else {
			delta = Math.abs(GlobalSetting.TARGET_FLOW_LOST_RATE_THRESHOLD - stats.getMean())
					/ (stats.getStandardDeviation() / Math.sqrt(stats.getN()));			
		}
		
		//get CDF(x<delta)
		double prob = 0;
		if (stats.getN() >= 30) {
			//use normal distribution
			prob = calcConfidenceNormalDistribution(stats, delta);
		} else {
			//use T-distribution
			prob = calcConfidenceTDistribution(stats, delta);
		}
		
		//debug
		System.out.println("N:" + stats.getN() + ", delta:" + delta + ", CDF:" + prob);
		
		//get confidence
		if (stats.getMean() >= GlobalSetting.TARGET_FLOW_LOST_RATE_THRESHOLD) {
			//confidence = CDF(x < delta)
			return prob;
		} else {
			//confidence = 1- CDF(x < delta)
			return 1 - prob;
		}
	}
		
	private static double calcConfidenceTDistribution(SummaryStatistics stats, double delta) {
        try {
            // Create T Distribution with N-1 degrees of freedom
            TDistribution tDist = new TDistribution(stats.getN() - 1);
            
            return tDist.cumulativeProbability(delta);
        } catch (MathIllegalArgumentException e) {
            return Double.NaN;
        }
    }
	
	private static double calcConfidenceNormalDistribution(SummaryStatistics stats, double delta) {
        try {
            // Create T Distribution with N-1 degrees of freedom
        	NormalDistribution nDist = new NormalDistribution();
            
            return nDist.cumulativeProbability(delta);
        } catch (MathIllegalArgumentException e) {
            return Double.NaN;
        }
    }
}
