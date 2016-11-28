import java.util.ArrayList;
import java.util.Scanner;  

public class SQLTOXML {
	
	
	private static ArrayList<String> tableInfo = new ArrayList<String>();
	
	
	public static void main(String args[]) {
		
		tableInfo.add("S , C"); 
		tableInfo.add("S . SNO");
		tableInfo.add("ID");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
//		SELECT SNO, SNAME, QUOTA, CITY FROM S;
//		SELECT DISTINCT s.sname as Salesperson_Name, orders.cno as Customer_No, orders.totqty FROM s, orders WHERE s.sno = orders.sno
		
		String options[] = { 
				"Create and View a DTD File",
				"Create and View a XSD File",
				"Create and View a XML File",
				"Run Another Query",
				"Exit the Program"
			};
		String yesOrNo[] = {    //options
				"Yes",
				"No"
		};
		String badOptionsTryAgain[] = { 
				"Run Another Query",
				"Exit the Program"
			};
		
		boolean runAnotherQuery = false;
		
//		@SuppressWarnings("resource")
//		Scanner queryInput = new Scanner(System.in);
		@SuppressWarnings("resource")
		Scanner file = new Scanner(System.in);
		
		String currentInput = "";
		String query = "";
		String fileName ="";
		
		System.out.println("Welcome to the XML Maker");
		
		while (!currentInput.equals("Exit the Program")){
			
//			System.out.println("Please enter the query you wish to execute");
//			query = queryInput.nextLine();
			tableInfo = Compiler.compiler(tableInfo);
			
			if (tableInfo.size() == 0){
				System.out.println("Please enter your option.");
				DisplayMenu(badOptionsTryAgain);
				currentInput = badOptionsTryAgain[getData(badOptionsTryAgain) - 1];
				
				//do nothing reset
//				System.out.println(tableInfo);
			}
			else{
				query = tableInfo.get(0).replace(";", ""); 
				tableInfo.remove(0);
				
				System.out.println(query + "\n" + tableInfo);
				XMLMaker.setQuery(query);
				
				System.out.println("Would you like to save the file to the hard disk?");
				DisplayMenu(yesOrNo);
				
				currentInput = yesOrNo[getData(yesOrNo) - 1];
				if (currentInput.equals("Yes")){
					System.out.println("Please enter the name you wish to call your file");
					fileName = file.nextLine();
					XMLMaker.setFileName(fileName);
					System.out.println("The file will be saved as \"" + fileName + "\" in the same directory of this file");
				}
				else if (currentInput.equals("No")){
					fileName = "MyXML";
					System.out.println("No file will be written just displayed");
				} 
				
				while(!currentInput.equals("Exit the Program")){
					DisplayMenu(options);
					currentInput = options[getData(options) - 1];
					
					if (currentInput.equals("Create and View a DTD File")){
						XMLMaker.DTD(tableInfo);
					}
					else if (currentInput.equals("Create and View a XSD File")){
						XMLMaker.generateXSD(tableInfo);
					}
					else if (currentInput.equals("Create and View a XML File")){
						XMLMaker.XML(tableInfo);
					}
					else if (currentInput.equals("Run Another Query")){
						currentInput = "Exit the Program";
						runAnotherQuery = true;
					}
				}
			}
			if (runAnotherQuery){
				currentInput = "Not going to exit and run another query";
				runAnotherQuery = false;
			}
			XMLMaker.resetFileName();
			Compiler.reset();
			Compiler.resetTableName();
			tableInfo.clear();
			XMLMaker.resetQuery();
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
