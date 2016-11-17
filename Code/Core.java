import java.util.Scanner;   //antler or javacompilercompiler

public class Core {
	
	
	public static void main(String args[]) {
		
		String options[] = { 
				"Change to XML(DTD)",
				"Change to XML(XSD)",
				"Something about compression"
			};
		
		String optionsExit[] = { 
				"Continue",
				"Exit the Program"
			};
		
		@SuppressWarnings("resource")
		Scanner queryInput = new Scanner(System.in);
		
		String currentInput = "";
		
		System.out.println("Welcome to XML Maker");
		
		while (!currentInput.equals("Exit the Program")){
			
			System.out.println("Please enter the query you wish to execute");
			String query = queryInput.nextLine();
			
			Parse.checkParse(query);
			
			DisplayMenu(options);
			currentInput = options[getData(options) - 1];
			
			if (currentInput.equals("Change to XML(DTD)")){
				System.out.println("The File has been saved as DTD\n");
			}
			else if (currentInput.equals("Change to XML(XSD)")){
				System.out.println("The File has been saved as XSD\n");
			}
			DisplayMenu(optionsExit);
			currentInput = optionsExit[getData(optionsExit) - 1];
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
