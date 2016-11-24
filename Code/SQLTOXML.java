import java.util.ArrayList;
import java.util.Scanner;  

public class SQLTOXML {
	
	
	private static ArrayList<String> tableInfo = new ArrayList<String>();
	
	
	public static void main(String args[]) {
		
		tableInfo.add("S , C"); 
		tableInfo.add("S . SNO");
		tableInfo.add("AS");
		tableInfo.add("C . ID");
		tableInfo.add("< + tagname");
		tableInfo.add("< tagname");
		tableInfo.add("C . SNAME");
		tableInfo.add("tagname >");
		tableInfo.add("tagname >");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
		
		String tryAgain[] = { 
				"Run Another Query",
				"Exit the Program"
			};
		
		String yesOrNo[] = { 
				"Yes",
				"No"
			};
		
		String options[] = { 
				"Create and View a DTD File",
				"Create and View a XSD File",
				"Create and View a XML File",
				"Exit the Displayer"
			};
		
		@SuppressWarnings("resource")
		Scanner queryInput = new Scanner(System.in);
		Scanner file = new Scanner(System.in);
		
		String currentInput = "";
		String query = "";
		String fileName ="";
		
		System.out.println("Welcome to XML Maker");
		
		while (!currentInput.equals("Exit the Program")){
			
			System.out.println("Please enter the query you wish to execute");
			query = queryInput.nextLine();
//			Parse.checkParse(query);
			if (tableInfo.size() == 0){
				//do nothing reset
			}
			else{
				System.out.println("Would you like to save the file to the hard disk?");
				DisplayMenu(yesOrNo);
				
				currentInput = yesOrNo[getData(yesOrNo) - 1];
				if (currentInput.equals("Yes")){
					System.out.println("Please enter the name you wish to call your file");
					fileName = file.nextLine();
					System.out.println("The file will be saved as \"" + fileName + "\" in the same directory of this file");
				}
				else if (currentInput.equals("No")){
					fileName = "MyXML";
					System.out.println("No file will be written just displayed");
				} 
				
				while(!currentInput.equals("Exit the Displayer")){
					DisplayMenu(options);
					currentInput = options[getData(options) - 1];
					
					if (currentInput.equals("Create and View a DTD File")){
						System.out.println("Create and View a DTD File\n");
					}
					else if (currentInput.equals("Create and View a XSD File")){
						System.out.println("Create and View a XSD File\n");
					}
					else if (currentInput.equals("Create and View a XML File")){
						System.out.println("Create and View a XML File\n");
					}
				
				}
			}
			DisplayMenu(tryAgain);
			currentInput = tryAgain[getData(tryAgain) - 1];
		}
	}
	
	static int getData(String[] options){
		@SuppressWarnings("resource")
		Scanner dataInput = new Scanner(System.in);
		int input = 0;
		
		if (dataInput.hasNextInt()) {						
			input = dataInput.nextInt();
			if (input > 0 && input <= options.length){
				System.out.println("User picked " + options[input - 1]);
			}
			else{
				System.out.println("\nNot a recognized option. Try again. Use a number between 1 - " + options.length);
				DisplayMenu(options);
				input = getData(options);
				
			}
		}
		else{
			System.out.println("\nNot a recognized option. Try again. Use a number between 1 - " + options.length);
			DisplayMenu(options);
			input = getData(options);
		}
		return input;
	}
	
	static void DisplayMenu(String[] options) {							//Menu options 
		System.out.println("Enter your choice");
		for (int index = 0; index < options.length; index ++) {
			String uiNumber = Integer.toString(index + 1);
			System.out.println(uiNumber + " - " + options[index]);
		}
	}
}
