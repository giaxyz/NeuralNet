package NeuralNetworkXOR;
import java.util.ArrayList;
import java.util.Arrays;


public class XOR_NN {

	public static void main(String[] args) throws Exception {
	
		
		//----------------User must set these variables-----------------------------
		
				//-----The number of neurons in each hidden layer, as an int array. 
				//		ArraySize is the number of hidden layers
				// 		Pay attention to neuron setup if manually setting weights, else error
			double initialBias = 1;
			double learningRate = 1.0;		
			ArrayList<Integer> numNeuronsPerLayer = new ArrayList<Integer>(Arrays.asList(2)); 
			boolean manuallySettingWeights = true;
				
				
				//-----  Read in the  data ---------------------------
				DataReadCSV csvRead = new DataReadCSV("C:/Users/Gia/GoldJava/NeuralNet/src/NeuralNetworkXOR/XORData_Nik.csv");
				ArrayList<ArrayList<Double>> inputsX = csvRead.getInputsDoubles(false);
				ArrayList<ArrayList<Double>> outputsY = csvRead.getOutputsDoubles(false);
				
				//----------    Generate the network
				
				Network network = new Network(inputsX, outputsY, numNeuronsPerLayer, initialBias, learningRate);
				
				
				// ----- Manually set the input weights on the network
				
				if(manuallySettingWeights){
					//printInfoOnWeights(network); // Use this to see how to set up the input weights. Comment out later
					double[] newNetworkWeightsN0L0x = {0.2,0.25,-0.1};
					double[] newNetworkWeightsN1L0x = {0.3,-0.1,-0.15};
					double[] newNetworkWeightsN0L1x = {-0.1,0.2,0.05};
					ArrayList<Double> newNetworkWeightsN0L0 = NNUtilities.doubleArrayToArrayList(newNetworkWeightsN0L0x);
					ArrayList<Double> newNetworkWeightsN1L0 = NNUtilities.doubleArrayToArrayList(newNetworkWeightsN1L0x);
					ArrayList<Double> newNetworkWeightsN0L1 = NNUtilities.doubleArrayToArrayList(newNetworkWeightsN0L1x);
					ArrayList<ArrayList<Double>> newNetworkWeights = new ArrayList<ArrayList<Double>>();
					newNetworkWeights.add(newNetworkWeightsN0L0);
					newNetworkWeights.add(newNetworkWeightsN1L0);
					newNetworkWeights.add(newNetworkWeightsN0L1);
					setNetworkWeights(network, newNetworkWeights, false);
				}
				
				
				//network.feedForwardOut(0); 
				// YOU NEED BOTH THESE LINES TO FEED FORWARD EVERY LAYER TO THE OUTPUT
				// make sure each layer is feed forward first, all the way across the network
				network.feedForwardLayer(0,0); // Arguments are:  LayerIndex, Example Index
				network.feedForwardLayer(1,0);
				if(!manuallySettingWeights){
					network.feedForwardLayer(2,0);
				}
				
				network.printNetwork();
				//network.getCurrentExample(true);
				
			
				BackPropagation backpropagate = new BackPropagation(network);
				if(!manuallySettingWeights){
					backpropagate.backPropagateLayer(2,0);
				}
				
				backpropagate.backPropagateLayer(1,0); // Arguments are:  LayerIndex, Example Index
				backpropagate.backPropagateLayer(0,0);
				
				backpropagate.updateWeights(1, true); // Arguments : LayerIndex, printInfo
				backpropagate.updateWeights(0, true); // Arguments : LayerIndex, printInfo

				
	}
	
	private static void setNetworkWeights(Network network, ArrayList<ArrayList<Double>> manuallySetInputWeights, boolean printInfo){
		
		ArrayList<ArrayList<Neuron>> neuronLayers = network.getNetworkLayers(printInfo);
		
		int wtIndex = 0;
		if(printInfo){
			System.out.println(manuallySetInputWeights);
		}
		
		
		for(int i = 0; i< neuronLayers.size(); i++){
			ArrayList<Neuron> currentLayer = neuronLayers.get(i);
			for(int j = 0; j< currentLayer.size(); j++){
				Neuron currentNeuron = currentLayer.get(j);
				currentNeuron.getInputWeightsRow(printInfo);
				currentNeuron.setInputWeightsRow(manuallySetInputWeights.get(wtIndex));
				currentNeuron.getInputWeightsRow(printInfo);
				wtIndex++;
				
			}
		}
	}
	
	private static void printInfoOnWeights(Network network){
		
		ArrayList<ArrayList<Neuron>> neuronLayers = network.getNetworkLayers(true);
		
		for(int i = 0; i< neuronLayers.size(); i++){
			ArrayList<Neuron> currentLayer = neuronLayers.get(i);
			for(int j = 0; j< currentLayer.size(); j++){
				Neuron currentNeuron = currentLayer.get(j);
				currentNeuron.getInputWeightsRow(true);
				
			}
		}
	}
	
}
