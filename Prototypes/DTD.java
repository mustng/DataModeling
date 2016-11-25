import java.util.ArrayList;

public class DTD {
	
	public static void main(String args[]) {
		ArrayList<String> tableInfo = new ArrayList<String>();
		
		tableInfo.add("S , C"); 
		tableInfo.add("S . SNO");
		tableInfo.add("AS");
		tableInfo.add("C . ID");
		tableInfo.add("< tagname");
		tableInfo.add("< tagname");
		tableInfo.add("C . SNAME");
		tableInfo.add("tagname >");
		tableInfo.add("tagname >");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
		
		DTD(tableInfo);
	}
	
	
	public static void DTD(ArrayList<String> tableInfo ){
		ArrayList<String> dtdLines = new ArrayList<String>();
		
		String printOut = "";
		
		
		cleanArray(tableInfo);
		dtdSideBar(dtdLines,tableInfo);	 //Creates side bar
		for (int i  = 0; i < dtdLines.size(); i++){
			printOut += dtdLines.get(i) + "\n";
		}
	   
//		if (nameFile){
//			createFile(printOut, filename += ".dtd");
//		}
		System.out.println(printOut);
	}
	
	
	public static ArrayList<String> dtdSideBar(ArrayList<String> dtdLines, ArrayList<String> tableInfo){
		dtdLines.add("<?xml ve rsion=\"1.0\"?>\n");
		dtdLines.add("<!DOCTYPE " + tableInfo.get(0) + " [");
		dtdLines.add("<!ELEMENT " + tableInfo.get(0) + "(");
		for (int i = 1; i < tableInfo.size(); i++){
			if(i + 1 >= tableInfo.size()){
				dtdLines.set(2, dtdLines.get(2) + " " + tableInfo.get(i));
			}
			else{
				dtdLines.set(2, dtdLines.get(2) + " " + tableInfo.get(i) + ",");
			}
			dtdLines.add("<!ELEMENT " + tableInfo.get(i) + " (#PCDATA)>");
		}
		dtdLines.add("]>");
		dtdLines.set(2,  dtdLines.get(2) + ")>");
		dtdLines.add(" ");
		
		return dtdLines;
	}
	
public static ArrayList<String> cleanArray(ArrayList<String> input){
		
		if (input.get(0).contains(",")){						//check for the main table
			
			String[] splitTableName = input.get(0).split(",");
			input.set(0, splitTableName[0].replace(" ", ""));
			
	
		}
		for(int i = 1; i < input.size(); i++){
			
			if(input.get(i).contains("<") || input.get(i).contains(">")){
				
				input.remove(i);
				i--;
				
			} 
			else if (input.get(i).toUpperCase().contains("AS")){
				input.remove(i + 1);
				i--;
				input.remove(i + 1);
				i--;
			}
			else if(input.get(i).contains(".")){
				
				String[] splitWord = input.get(i).split("\\.");
				input.set(i, splitWord[splitWord.length - 1]);
			}
			
		}
//		System.out.println(input);
		return input;
	}
	
	
}
