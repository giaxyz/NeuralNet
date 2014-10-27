package NeuralNetworkXOR;
import java.util.ArrayList;
import java.lang.Math;
// test1

public class Neuron {
	
	private int layerID;
	private int neuronID;
	private ArrayList<Double> inputWeightsRow;
	private ArrayList<Double> deltaRow;
	private double outputValueCalculated;
	private Double biasValue;
	private ArrayList<Double> inputs;
	private double sum;
	private String neuronDetails;
	private double betaError;
	private boolean isOutput;
	
	public Neuron(int layerID, int neuronID, double biasValue, boolean isOutput){
		
		this.isOutput = isOutput;
		this.sum = 0.0;
		this.inputs = new ArrayList<Double>();
		this.deltaRow = new ArrayList<Double>();
		this.layerID = layerID;
		this.neuronID = neuronID;
		this.neuronDetails = ("L" + layerID + "_N" + neuronID );
		this.biasValue = biasValue;
		this.inputWeightsRow = new ArrayList<Double>();
		this.betaError = Double.POSITIVE_INFINITY;
		this.outputValueCalculated = 0.0; // Change the output value here!???
	}
	
	public String getNeuronDetails(){
		return this.neuronDetails;
	}
	
	public boolean isOutput(){
		return this.isOutput;
	}
	
	public void setDeltaRow(ArrayList<Double> deltas, boolean printInfo){
		
		if(printInfo){
			
			System.out.println("-----" + neuronDetails + " deltas set to : " + deltas);
		}
		this.deltaRow = deltas;
	}
	
	public ArrayList<Double> getDeltaRow(boolean printInfo){
		
		if(printInfo){
			System.out.println("Delta for " + neuronDetails + " : " + this.deltaRow);
		}
		return this.deltaRow;
	}
	
	public double getBetaError(boolean printInfo){
		
		
		
		if(printInfo){
			System.out.println(neuronDetails + " betaError current value " + this.betaError);
		}
		
		return this.betaError;
		
	}
	
	
	public double setBetaError(double betaError, boolean printInfo){
		
		this.betaError = betaError;
		
		if(printInfo){
			System.out.println("\t" + neuronDetails + " betaError new value " + this.betaError);
		}
		
		return this.betaError;
		
	}
	

	public double activateSigmoid(double sumValue, boolean printInfo){
		
		
		double thresholdVal =  1.0 / (1 + Math.exp(-1.0 * sumValue));
		thresholdVal = NNUtilities.formatDouble(thresholdVal);
		this.outputValueCalculated = thresholdVal; // set the output value
		if(printInfo){
			System.out.println("Activating " + neuronDetails +  " " +  sumValue);
			System.out.println("Threshold : " + thresholdVal);
		}
		return thresholdVal;
		
	}

	public double computeSum(boolean printInfo) throws Exception{
		
		if(inputs.isEmpty()){
			throw new Exception("The input array for N" + neuronID + "_L" + layerID + " is Empty");
		}
		
		if(inputs.size() != inputWeightsRow.size()){
			throw new Exception("N" + neuronID + "_L" + layerID + " weights and inputs matrices do not match");
		}
		
		
		double sum = 0.0;
		
		if(printInfo){
			System.out.println("Computing sum for L_" + layerID + "_N_" + neuronID);
		}
		
		for(int i = 0; i< inputs.size(); i++){
			
			double inputVal = inputs.get(i);
			double weightVal = inputWeightsRow.get(i);
			sum += (inputVal * weightVal);
			
			if(printInfo){
				System.out.println("\t\t Input : " + inputVal + " Weight " + weightVal);
			}
			
		}
		
		if(printInfo){
			
			System.out.println("\tFinal sum " + sum);
		}
		
		this.sum = sum;
		return sum;
	}
	
	public double getSum(boolean printInfo){
		
		if(printInfo){
			System.out.println("Sum for N_" + neuronID + " L_" + layerID + " : " + this.sum);
		}
		
		return this.sum;
		
	}
	
	public double setOutputValue(Double newOutputValue){
		this.outputValueCalculated = newOutputValue;
		return newOutputValue;
	}

	public double getOutputValue(boolean printInfo){
		
		if(printInfo){
			System.out.println("Neuron " + neuronID + " Layer " + layerID + " Output = " + this.outputValueCalculated);
		}
		return this.outputValueCalculated;
	}
	
	public ArrayList<Double> getInputs(boolean printInfo){
		
		if(printInfo){
			System.out.println("Inputs for neuron L" + layerID + " N"  + neuronID + " are  " + this.inputs);
		}
		
		return this.inputs;
	}
	
	public boolean setInputs(ArrayList<Double> inInputs){
		
		ArrayList<Double> newInputs = NNUtilities.duplicateArrayList(inInputs);
		this.inputs = newInputs;
		return true;
	}
	
	public double getBiasValue(boolean printInfo){
		if(printInfo){
			System.out.println("Bias for Neuron N_" + neuronID + " in Layer L)" + layerID + " is "  + biasValue);
		}
		return this.biasValue;
	}
	
	public double setBiasValue(double bias, boolean printInfo){
		
		this.biasValue = bias;
		if(printInfo){
			System.out.println("Bias Value for Neuron N_" + neuronID + " in Layer L)" + layerID + " Set to "  + biasValue);
		}
		return this.biasValue;
	}
	
	public int getLayerID(boolean printInfo){
		
		if(printInfo){
			System.out.println("Neuron Layer ID : " + neuronDetails + " " + layerID);
		}
		return this.layerID;
	}

	public String toStringGraphic(){
	
		String printString = ("|L" + layerID + "_N" + neuronID + "|");
		
		return printString;
	}
	
	public int getNeuronID(boolean printInfo){
		
		if(printInfo){
			System.out.println(neuronDetails + " Neuron id : " + neuronID);
		}
		return this.neuronID;
	}
	
	public String toString(){
		String printString = ("L" + layerID + "_N" + neuronID);
		return printString;
	}

	public void setInputWeightsRow(ArrayList<Double> inputWeights) {
		this.inputWeightsRow = inputWeights;
		
	}
	
	public ArrayList<Double> getInputWeightsRow(boolean printInfo){
		if(printInfo){
			System.out.println("WeightsRow for Neuron " + neuronID + " in Layer " + layerID + " is : " + this.inputWeightsRow);
		}
		return this.inputWeightsRow;
	}
}
