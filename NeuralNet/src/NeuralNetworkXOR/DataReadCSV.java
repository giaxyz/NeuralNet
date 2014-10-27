package NeuralNetworkXOR;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class DataReadCSV {

	private String fileName;
	private int numOfExamples; // number of rows, minus the header
	private int numOfAttributes; // num of inputs plus the output 
	private int numOfInputs;// x1, x2 etc.  the total nuber of these
	
	
	public DataReadCSV(String fileName) throws FileNotFoundException{
		
		this.fileName = fileName;
		this.numOfExamples = numExamples();
		this.numOfAttributes = numOfColumns();
		this.numOfInputs = numOfAttributes - 1;
		
	}
	
	public int getNumOfInputs(){
		return this.numOfInputs;
	}
	
	// Gets the number of rows in the csv file, minus the header
	public int numExamples() throws FileNotFoundException{
		
		int count = 0;
		File file = new File(fileName);
		
		try{
			Scanner inputStream = new Scanner(file);
			while(inputStream.hasNext()){
				String data = inputStream.next();
				count ++;
				
			}
			inputStream.close();
			
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
		count--; // this is to minus the header attributes on the csv
		return count;
	}
	
	// Gets the number of columns, ie the number of attributes in the csv file
	public int numOfColumns() throws FileNotFoundException{
		
		int numValues = 0;
		File file = new File(fileName);
		
		try{
			Scanner inputStream = new Scanner(file);
			String data = inputStream.next();
			String[] values = data.split(",");
			numValues = values.length;
			inputStream.close();
			
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return numValues;
		
	}
	
	
	// Creates a list of all the Y output values as Floats
	public ArrayList<ArrayList<Double>> getOutputsDoubles(boolean printInfo){
		
		ArrayList<Double> columnList = StringArrayListToArrayDouble(removeFirstElement(makeNewListFromColumn((this.numOfAttributes)-1)));
		ArrayList<ArrayList<Double>> columnListAsDouble = new ArrayList<ArrayList<Double>>();
		columnListAsDouble.add(columnList);
		
		if(printInfo){
			System.out.println("Printing Y output values as Doubles....");
			System.out.println(columnListAsDouble);
		}
		
		return columnListAsDouble;
		
	}
	
	// Creates a list of lists of all Input x values as Floats
	public ArrayList<ArrayList<Double>> getInputsDoubles(boolean printInfo ){
			
			ArrayList<ArrayList<Double>> inputXs = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i< this.numOfInputs; i++){
				
				inputXs.add(StringArrayListToArrayDouble(removeFirstElement(makeNewListFromColumn(i))));
				
			}
			
			if(printInfo){
				System.out.println("Printing Input Xn values as Doubles.....");
				System.out.println(Arrays.toString(inputXs.toArray()));
			}
			
			return inputXs;
		
		}
	
	// Converts an array list of type string to an array list of type float
	public ArrayList<Double> StringArrayListToArrayDouble(ArrayList<String> inputStringList){
		
		ArrayList<Double> newDoubleList = new ArrayList<Double>();
		for (int i = 0; i<inputStringList.size(); i++){
			String tmp = inputStringList.get(i);
			double intVal = Double.parseDouble(tmp);
			newDoubleList.add(intVal);
		}
		return newDoubleList;
	}
	
	// Converts an array list of type string to an array list of type int
	public ArrayList<Integer> StringArrayListToArrayInt(ArrayList<String> inputStringList){
		
		ArrayList<Integer> newIntList = new ArrayList<Integer>();
		for (int i = 0; i<inputStringList.size(); i++){
			String tmp = inputStringList.get(i);
			int intVal = Integer.parseInt(tmp);
			newIntList.add(intVal);
		}
		return newIntList;
	}
	
	// Removes the first element of an array list, because we want to chop off the attribute from the csv
	public ArrayList<String> removeFirstElement(ArrayList<String> inputList){
		
		inputList.remove(0);
		return inputList;
		
	}
	
	// Makes a list from the specified index column of a csv. Note, pass it to another method
	// to remove the first element, which will be the title of the attribute.
	public ArrayList<String> makeNewListFromColumn(int j){
		
		ArrayList<String> intList = new ArrayList<String>();
		File file = new File(fileName);
		try{
			Scanner inputStream = new Scanner(file);
			while(inputStream.hasNext()){
				String data = inputStream.next();
				String[] values = data.split(",");
				intList.add(values[j]);
				
			}
			System.out.println("");
			
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return intList;
			
	}
	
	// Prints out the csv file
	public void printFile() throws FileNotFoundException{
		
		
		File file = new File(fileName);
		
		try{
			Scanner inputStream = new Scanner(file);
			while(inputStream.hasNext()){
				String data = inputStream.next();
				String[] values = data.split(",");
				int numValues = values.length;
				
				for(int i = 0; i< values.length; i++){
					System.out.print(values[i] + "_");
				}
				System.out.println("");
				
			}
			inputStream.close();
			
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		
	}
	
}