package NeuralNetworkXOR;

import java.util.ArrayList;

public class BackPropagation {

	private Network network;
	
	public BackPropagation(Network network){
		
		this.network = network;
	}

	public void backPropagateLayer(int layerIndex, int exampleIndex) {

		ArrayList<ArrayList<Neuron>> networkLayers = network.getNetworkLayers(false);
		ArrayList<Neuron> currentNeuronLayer = networkLayers.get(layerIndex);
		
		// For every neuron in the current layer, compute its beta and deltas
		for(int i = 0; i< currentNeuronLayer.size(); i++){
			
			Neuron currentNeuron = currentNeuronLayer.get(i);
			System.out.println("\n--Backpropping " + currentNeuron + "------" );
			
			
			double betaError = computeBetaError(currentNeuron, false);
			currentNeuron.setBetaError(betaError, false);
			System.out.println("\t---" + "betaError : " + betaError);
			
		
			ArrayList<Double> deltaValues = computeDeltaValues(currentNeuron, false);
			currentNeuron.setDeltaRow(deltaValues, false);
			System.out.println("\t---" + "deltas : " + deltaValues);
			
		}
		
		
	}
	
	public void updateWeights(int layerIndex, boolean printInfo) throws Exception{
		
		
		ArrayList<ArrayList<Neuron>> networkLayers = network.getNetworkLayers(false);
		ArrayList<Neuron> currentNeuronLayer = networkLayers.get(layerIndex);
				
		for(int i = 0; i< currentNeuronLayer.size(); i++){
			
			Neuron neuron = currentNeuronLayer.get(i);
			System.out.println("\n\n------------Updating weights for : " + neuron + "\n");
			ArrayList<Double> deltaMatrix = neuron.getDeltaRow(true);
			ArrayList<Double> weightMatrix = neuron.getInputWeightsRow(true);
			ArrayList<Double> updatedWeights = new ArrayList<Double>();
			
			if((weightMatrix.size()) != deltaMatrix.size()){
				
				throw new Exception("Error, weight Array is not the same as delta array for " + neuron);
			}
			
			for(int j = 0; j< deltaMatrix.size(); j++){
				
				double deltaPlusWeight = NNUtilities.formatDouble((weightMatrix.get(j) + deltaMatrix.get(j)));
				updatedWeights.add(deltaPlusWeight);
				
			}
			
			
			neuron.setInputWeightsRow(updatedWeights);
			
			if(printInfo){
				
				System.out.println("\n" + neuron  + " Updated Weights Calculated and set : " );
				System.out.println(neuron.getInputWeightsRow(false));
				
			}
			
		}
				
	}
	
	private ArrayList<Double> computeDeltaValues(Neuron currentNeuron, boolean printInfo) {

		ArrayList<Double> deltaValues = new ArrayList<Double>();
		double learningRate = network.getLearningRate();
		double betaValue = currentNeuron.getBetaError(false);
		ArrayList<Double> previousLayerOutputs = getPreviousLayerOutput(currentNeuron, false);
		
		for(int i = 0; i< previousLayerOutputs.size(); i++){
		
			if(printInfo){
				System.out.println("\t---Computing for " + currentNeuron + "(" + learningRate + ") * (" + betaValue + ")" + " * (" + previousLayerOutputs.get(i) + ")\n");
			}
			
			
			double deltaValue = NNUtilities.formatDouble((learningRate)*(betaValue)*(previousLayerOutputs.get(i)));
			deltaValues.add(deltaValue);
		}
		
		return deltaValues;
	}

	private ArrayList<Double> getPreviousLayerOutput(Neuron currentNeuron, boolean printInfo) {
		
		ArrayList<Double> previousLayerOuts = new ArrayList<Double>();
		int currentLayerID = currentNeuron.getLayerID(false);
		currentNeuron.getOutputValue(false);
		
		if(currentLayerID == 0){
			
			int currentExample = network.getCurrentExample(false);
			ArrayList<Double> inputs = network.getExample(currentExample, false);
			for(int i = 0; i< inputs.size(); i++){
				previousLayerOuts.add(inputs.get(i));
			}
			
		}else{
			
			int previousLayerID = currentLayerID - 1;
			ArrayList<Neuron> previousLayer = network.getNetworkLayers(false).get(previousLayerID);
			for(int i = 0; i< previousLayer.size(); i++){
				
				Neuron prevLayerCurrentNeuron = previousLayer.get(i);
				double prevLayerOutput = prevLayerCurrentNeuron.getOutputValue(false);
				previousLayerOuts.add(prevLayerOutput);
				
			}
		}
		
		previousLayerOuts.add(1.0); //Set the Bias weight to 1.0
		
		if(printInfo){
			System.out.println("PreviousLayer Outs (with 1.0 for bias) is : " + previousLayerOuts);
		}
		
		return previousLayerOuts;
	}

	public double computeBetaError(Neuron neuron, boolean printInfo){
		
		double betaError = Double.NEGATIVE_INFINITY;
		double derivative = Double.NEGATIVE_INFINITY;
		double out = neuron.getOutputValue(false);
		int currentNeuronID = neuron.getNeuronID(false);
		
		if(neuron.isOutput()){
			
			ArrayList<Double> outputs = network.getOutputsY(false).get(currentNeuronID);
			double expectedY = outputs.get(network.getCurrentExample(false));
			derivative = (expectedY - out);
			
			
			if(printInfo){
				
				System.out.println(" Output Neuron weights are : " ); // this is the key, start here
				neuron.getInputWeightsRow(true);
				
			}
			
			
		}else{
			
			
			double betaSumsFromNextRow = getBetaSumsFromNextRow(neuron, false);
			double neuronWeightToNextNeuron = getCurrentNeuronWeightInputFromNextRow(neuron, false);
			derivative = betaSumsFromNextRow * neuronWeightToNextNeuron; 
			
			
			if(printInfo){
				
				System.out.println("(" + out + ") * ( 1 - " + out  + " ) * (" + derivative + ")");
				System.out.println("... where derivative is " + betaSumsFromNextRow + " + " + neuronWeightToNextNeuron);
			}
			
			
		}
		
		betaError = NNUtilities.formatDouble((out * ( 1 - out ) * derivative));
		
		if(printInfo){
			System.out.println("\t" + neuron + " beta error set to : " + betaError);
		}
		
		return betaError;
	}

	private double getCurrentNeuronWeightInputFromNextRow(Neuron neuron, boolean printInfo){
		
		double neuronWeightToNextNeuron = Double.NEGATIVE_INFINITY;
		int nextLayerIndex = (neuron.getLayerID(false)) + 1;
		ArrayList<Neuron> nextLayer = network.getLayer(nextLayerIndex, false);
	
		
		for(int i = 0; i< nextLayer.size(); i++){
			
			Neuron currentNeuronInNextLayer = nextLayer.get(i);
			ArrayList<Double> nextLayerWeights = currentNeuronInNextLayer.getInputWeightsRow(false);
			int neuronID = neuron.getNeuronID(false);
			neuronWeightToNextNeuron = nextLayerWeights.get(neuronID);
			
			if(printInfo){
				
				System.out.println(neuron + " weight into next neuron " + currentNeuronInNextLayer + " : " + neuronWeightToNextNeuron);
			}
			
			
		}
		
	
		return neuronWeightToNextNeuron;
	}
	
	private double getBetaSumsFromNextRow(Neuron neuron, boolean printInfo) {
		
		double betaSumsFromNext = 0.0;
		int nextLayerIndex = (neuron.getLayerID(false)) + 1;
		ArrayList<Neuron> nextLayer = network.getLayer(nextLayerIndex, false);
		
		for(int i = 0; i< nextLayer.size(); i++){
			
			Neuron currentNeuronInNextLayer = nextLayer.get(i);
			double currentNextBetaVal = currentNeuronInNextLayer.getBetaError(false);
			betaSumsFromNext += currentNextBetaVal;
		}
		
		if(printInfo){
			System.out.println(neuron + " Beta Sum From next layer : " + betaSumsFromNext);
		}
		
		return betaSumsFromNext;
	}
	
}
