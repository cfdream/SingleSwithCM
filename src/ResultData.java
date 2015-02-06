
public class ResultData {
	double falsePositive;
	double falseNegative;
	double accuracy;
	
	int totalFlowNumber;
	int totalFlowHeldNumber;
	
	public ResultData(double falsePositive, double falseNegative, double accuracy,
			int totalFlowNumber, int totalFlowHeldNumber) {
		super();
		this.falsePositive = falsePositive;
		this.falseNegative = falseNegative;
		this.accuracy = accuracy;
		this.totalFlowNumber = totalFlowNumber;
		this.totalFlowHeldNumber = totalFlowHeldNumber;
	}
}
