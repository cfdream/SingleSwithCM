package singleswitch.data;
public class ResultData {
	public double falsePositive;
	public double falseNegative;
	public double accuracy;

	int totalFlowNumber;
	int totalFlowHeldNumber;

	public ResultData(double falsePositive, double falseNegative,
			double accuracy, int totalFlowNumber, int totalFlowHeldNumber) {
		super();
		this.falsePositive = falsePositive;
		this.falseNegative = falseNegative;
		this.accuracy = accuracy;
		this.totalFlowNumber = totalFlowNumber;
		this.totalFlowHeldNumber = totalFlowHeldNumber;
	}
}
