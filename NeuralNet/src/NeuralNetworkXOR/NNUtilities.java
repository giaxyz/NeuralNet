package NeuralNetworkXOR;
import java.util.ArrayList;
import java.util.Random;
// test

public final class NNUtilities {
	
	public static ArrayList<Double> duplicateArrayList(ArrayList<Double> originalList){
		
		ArrayList<Double> newList = new ArrayList<Double>();
		for(int i = 0; i< originalList.size(); i++){
			newList.add(originalList.get(i));
		}
		
		return newList;
		
	}
	
	public static int getMaxInt(ArrayList<Integer> listOfNumbers, boolean printInfo){
		
		
		int maxValue = (int)Double.NEGATIVE_INFINITY;
		for(int i = 0; i<listOfNumbers.size(); i++){
			if(listOfNumbers.get(i) > maxValue){
				maxValue = listOfNumbers.get(i);
			}
		}
		
		if(printInfo){
			System.out.println("Max Value : " + maxValue);
		}
		return maxValue;
	}

	public static double getMaxDouble(ArrayList<Double> listOfNumbers, boolean printInfo){
		
		
		double maxValue = Double.NEGATIVE_INFINITY;
		for(int i = 0; i<listOfNumbers.size(); i++){
			if(listOfNumbers.get(i) > maxValue){
				maxValue = listOfNumbers.get(i);
			}
		}
		
		if(printInfo){
			System.out.println("Max Value : " + maxValue);
		}
		return maxValue;
	}

	public static void doSomething(){
		// test method
	}
	
	public static double randomNumber(double min, double max, boolean printInfo){
		
		    Random rand = new Random();
		    double randomValue = min + (max - min) * rand.nextDouble();
		   
		    if(printInfo){
		    	System.out.println("Random number is : " + randomValue);
		    }
		    return randomValue;
		}
	
	// Format Doubles
	public static double formatDouble(double doubleNum){
				
				double roundedNum = (double)Math.round(doubleNum * 10000) / 10000;
				return roundedNum;
			}

	public static ArrayList<Double> doubleArrayToArrayList(double[] doubleArray) {
		
		ArrayList<Double> toArray = new ArrayList<Double>();
		for(int i = 0; i< doubleArray.length; i++){
			toArray.add(doubleArray[i]);
		}
		return toArray; 
	}

		
	}

