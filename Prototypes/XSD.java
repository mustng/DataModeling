

import java.sql.SQLException;
import java.util.ArrayList;

public class XSD{

	public static void main(String args[]){
		
		ArrayList<String> tableInfo = new ArrayList<String>();
		
		tableInfo.add("S , C"); 
		tableInfo.add("S . SNO");
		tableInfo.add("AS");
		tableInfo.add("ID");
		tableInfo.add("< tagname");
		tableInfo.add("SNAME");
		tableInfo.add("tagname >");
		tableInfo.add("QUOTA");
		tableInfo.add("< tagname");
		tableInfo.add("CITY");
		tableInfo.add("tagname >");
		
		generateXSD(tableInfo);

	}
	

	public static void generateXSD(ArrayList<String> input ){
		
		String printOut = "";
		
		cleanArray(input);
		
		printOut += "<?xml version = \"1.0\"?>\n";
		printOut += "<xs:schema xmlns:xs = \"http://www.w3.org/2001/XMLSchema\">\n";
		printOut += "<xs:element name = '" + input.get(0) + "XSDnew\" elementFormDefault=\"qualified\"attributeFormDefault=\"qualified\">\n";
		printOut += "<xsd:complexType name=\"" + input.get(0) + "\">\n";
		for(int j = 1; j < input.size(); j++)
			printOut += "<xsd:element name=\"" + input.get(j) + "\" type=\"xsd:string\" maxOccurs=\"1\" minOccurs=\"1\" />\n";
		
		printOut += "</xsd:complexType >\n";
		printOut += "</schema>\n\n";
		
//		if (nameFile){
//			createFile(printOut, filename += ".xsd");
//		}
		System.out.println(printOut);
		
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
