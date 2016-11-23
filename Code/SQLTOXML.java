import java.util.Scanner;  

public class SQLTOXML {
	
	
	public static void main(String args[]) {
		
		String options[] = { 
				"Enter SQL to Convert",
				"Enter File Name",
				"Create and View a DTD File",
				"Create and View a XSD File",
				"Create and View a XML File",
				"Exit the Program"
			};
		
//		String optionsExit[] = { 
//				"Continue",
//				"Exit the Program"
//			};
		
		@SuppressWarnings("resource")
		Scanner queryInput = new Scanner(System.in);
		
		String currentInput = "";
		String query = "";
		
		System.out.println("Welcome to XML Maker");
		
		while (!currentInput.equals("Exit the Program")){
			
			DisplayMenu(options);
			currentInput = options[getData(options) - 1];
			
			if (currentInput.equals("Enter SQL to Convert")){
				System.out.println("Please enter the query you wish to execute");
				query = queryInput.nextLine();
//				Parse.checkParse(query);
			}
			else if (query.equals("")){
				System.out.println("\nSorry Please Select \n");
			}
			else if (currentInput.equals("Enter File Name")){
				System.out.println("Enter File Name\n");
			}
			else if (currentInput.equals("Create and View a DTD File")){
				System.out.println("Create and View a DTD File\n");
			}
			else if (currentInput.equals("Create and View a XSD File")){
				System.out.println("Create and View a XSD File\n");
			}
			else if (currentInput.equals("Create and View a XML File")){
				System.out.println("Create and View a XML File\n");
			}
//			DisplayMenu(optionsExit);
//			currentInput = optionsExit[getData(optionsExit) - 1];
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