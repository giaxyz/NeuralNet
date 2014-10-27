package NeuralNetworkXOR;
//test11

import java.util.ArrayList;

public class Network {

	private ArrayList<ArrayList<Double>> inputsX;
	private ArrayList<Double> currentInputsX;
	private ArrayList<ArrayList<Double>> outputsY;
	private ArrayList<Integer> numNeuronsHidden;
	private ArrayList<Neuron> networkNeurons;
	private ArrayList<ArrayList<Neuron>> networkLayers;
	private double initialBiasValue;
	private int currentExample;
	public double learningRate;
	
	
	
	public Network (ArrayList<ArrayList<Double>> inputsX, ArrayList<ArrayList<Double>> outputsY,  ArrayList<Integer> numNeuronsHidden, double biasValue, double learningRate) throws Exception{
		
		this.learningRate = learningRate;
		networkNeurons = new ArrayList<Neuron>();
		networkLayers = new ArrayList<ArrayList<Neuron>>() ;
		this.inputsX = inputsX;
		this.outputsY = outputsY; // this reads the original outputs
		this.numNeuronsHidden = numNeuronsHidden;
		this.initialBiasValue = biasValue;
		this.currentExample = 0;
		this.currentInputsX = getExample(currentExample, false);
		//this.initializedInputsX = getExample(currentExample, false);
		createNetworkNeurons();
		createWeightMatrices(networkLayers);
		
	}
	
	public double getLearningRate(){
		return this.learningRate;
	}
	
	public int getCurrentExample(boolean printInfo){
		
		if(printInfo){
			System.out.println("Current Example is : " + this.currentExample);
		}
		return this.currentExample;
	}
	
	public ArrayList<ArrayList<Neuron>> getNetworkLayers(boolean printInfo){
		
		if(printInfo){
			System.out.println("Network layers are : " + this.networkLayers);
		}
		
		return this.networkLayers;
	}
	
	public void feedForwardOut(int exampleIndex) throws Exception{
		
		for(int i = 0; i<networkLayers.size(); i++){
			feedForwardLayer(i, exampleIndex);
		}
	
	}
	
	public void setLayerNeuronInputs(int layerIndex, int exampleIndex){
		
		this.currentInputsX = getExample(exampleIndex, false);
		
		if(layerIndex == 0){
			
			ArrayList<Neuron> inputLayer = networkLayers.get(layerIndex);
			ArrayList<Double> inputLayerInputs = NNUtilities.duplicateArrayList(currentInputsX);
			
			for(int j = 0; j< inputLayer.size(); j++){
				
				Neuron neuron = inputLayer.get(j);
				double currentBiasValue = neuron.getBiasValue(false);
				inputLayerInputs.add(currentBiasValue);
				neuron.setInputs(inputLayerInputs);
				inputLayerInputs.remove(inputLayerInputs.size() - 1);
				
			}
			
		}else{
			
			ArrayList<Neuron> currentLayer = networkLayers.get(layerIndex);
			ArrayList<Neuron> previousLayer = networkLayers.get(layerIndex - 1);
			
			
			for(int j = 0; j<currentLayer.size(); j++){
				
				ArrayList<Double> inputsForCurrentLayer = new ArrayList<Double>();
				Neuron neuron = currentLayer.get(j);
				
				for(int k = 0; k< previousLayer.size(); k++){
					double currentNeuronOutput = previousLayer.get(k).getOutputValue(false);
					inputsForCurrentLayer.add(currentNeuronOutput);
				}
				
				double currentBiasValue = neuron.getBiasValue(false);
				inputsForCurrentLayer.add(currentBiasValue);
				neuron.setInputs(inputsForCurrentLayer);
				neuron.getInputs(false);
				
			
			}
		
		}
		
	
		
		
		
		
		
	}
	
	public void feedForwardLayer(int layerIndex, int exampleIndex) throws Exception{
		
		this.currentExample = exampleIndex;
		this.currentInputsX = getExample(exampleIndex, false);
		setLayerNeuronInputs(layerIndex, exampleIndex);
		computeSumsinLayer(layerIndex);
				
	}
	
	public void computeSumsinLayer(int layerIndex) throws Exception{
		
		ArrayList<Neuron> layerNeurons = getLayer(layerIndex, false);
		
		for(int i = 0; i< layerNeurons.size(); i++){
			
			Neuron neuron = layerNeurons.get(i);
			neuron.computeSum(false);
			double sum = neuron.getSum(false);
			neuron.activateSigmoid(sum, false);
			//neuron.getOutputValue(true);
			
		}
	
	}
	
	public ArrayList<Neuron> getLayer(int layerIndex, boolean printInfo){
		
		ArrayList<Neuron> layerNeurons = networkLayers.get(layerIndex);
		if(printInfo){
			System.out.println("Getting Layer : " + layerIndex + " at " + layerNeurons);
		}
		return layerNeurons;
	}

	public void printNetwork(){
		
		// Print the Inputs
		System.out.print("\n\nInput" + "\t\t\t");
		for(int i = 0; i< networkLayers.size(); i++){
			System.out.print("Layer" + i + "\t\t\t");
		}
		
		System.out.print("\n\n\n" + currentInputsX);
		
		// Print out the main network
		for(int i = 0; i< networkLayers.size(); i++){
			System.out.print("\t\t" + networkLayers.get(i));
		}
		
		System.out.println("\n");
		System.out.println("--------------------");
		// Print out the weights
		for(int i = 0; i< networkLayers.size(); i++){
			
			ArrayList<Neuron> currentLayer = networkLayers.get(i);
			for(int j = 0; j<currentLayer.size(); j++ ){
				Neuron currentNeuron = currentLayer.get(j);
				System.out.println(currentNeuron + ":" + currentNeuron.getInputWeightsRow(false) + " Bias " + currentNeuron.getBiasValue(false) + "     Output : " + currentNeuron.getOutputValue(false));
			}
			System.out.println("\n");
			
		}
		
		System.out.println("--------------------");
	}
	
	// Create weight matrices
	public void createWeightMatrices(ArrayList<ArrayList<Neuron>> nLayers){
		
		
		for(int i = 0; i<networkLayers.size(); i++){
			
			for(int j = 0; j< networkLayers.get(i).size(); j++ ){
				
				
				Neuron currentNeuron = networkLayers.get(i).get(j);
				int currentLayer = currentNeuron.getLayerID(false);
				//System.out.println("	Creating individual weight matrix for neuron : L" + currentLayer + " N" + currentNeuronID);
				
				ArrayList<Double> inputWeights = new ArrayList<Double>();
				int weightRowSize = 0;
				
				// if first layer, set to num inputs and bias
				if(currentLayer == 0){
					
					weightRowSize = currentInputsX.size() + 1; // + 1 for Bias weight at end
					
				}else{
					
					ArrayList<Neuron> previousLayer = networkLayers.get(i-1);
					weightRowSize = previousLayer.size() + 1 ; // + 1 for Bias weight at end
					
				}
				
				
					for(int k = 0; k< weightRowSize; k++){
						
						double randNum = (NNUtilities.randomNumber(0.0, 1.0, false));
						randNum = (NNUtilities.formatDouble(randNum));
						inputWeights.add(randNum);
					}
					
					currentNeuron.setInputWeightsRow(inputWeights);
					currentNeuron.getInputWeightsRow(false);
				
				
			}
		}
		
		
		
	}

	// Create all the neurons for each layer and the output layer
	public void createNetworkNeurons(){
		
		
		int layerID = 0; // Initialising the layer IDs
		
		// Create the Hidden Layers
		for(int i =0; i< numNeuronsHidden.size(); i++){
			createNeuronLayer(numNeuronsHidden.get(i), layerID, false);
			layerID++;
		}
		
		// Create the Output Layer
		int numOfOutputNeurons = outputsY.size();
		createNeuronLayer(numOfOutputNeurons, layerID, true);
		
	}
	
	//Create Neurons in a specified layer
	public ArrayList<Neuron> createNeuronLayer(int numOfNeurons, int layerID, boolean isOutput){
		
		ArrayList<Neuron> layer = new ArrayList<Neuron>();
		int neuronID = 0;
		for(int i = 0; i<numOfNeurons; i++){
			Neuron neuron = new Neuron(layerID, neuronID, initialBiasValue, isOutput);
			layer.add(neuron);
			networkNeurons.add(neuron);
			neuronID++;
		}
		networkLayers.add(layer);
		return layer;
	}
	
	// Get the original outputs from the data file
	public ArrayList<ArrayList<Double>> getOutputsY(boolean printInfo){
		
		if(printInfo){
			System.out.println("Outputs Y are : " +  this.outputsY);
		}
		
		return this.outputsY;
	}

	// Get the input of a particular example, by index
	public ArrayList<Double> getExample(int index, boolean printInfo){
		
		ArrayList<Double> exampleInputs = new ArrayList<Double>();
		for(int i = 0; i < inputsX.size(); i++){
			ArrayList<Double> currentArray = inputsX.get(i);
			Double value = currentArray.get(index);
			exampleInputs.add(value);
		}
		if(printInfo){
			System.out.println("Example at Index " + index + " Inputs : " + exampleInputs);
		}
		return exampleInputs;
		
	}
}


