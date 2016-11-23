package Prototypes;

import java.sql.SQLException;
import java.util.ArrayList;

public class XSD{

	public static void main(String args[]){
		
		ArrayList<String> tableInfo = new ArrayList<String>();
		ArrayList<String> dtdLines = new ArrayList<String>();
		ArrayList<String> dtdSideLines = new ArrayList<String>();
		
		
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		tableData.add(new ArrayList());
		
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
		cleanArray(tableInfo);
		generateXSD(tableInfo);

	}
	public static String tabMaker(int tagNameDepthCount){
		String space = "";
		for(int i = 0; i < tagNameDepthCount; i++){
			space += "\t";
		}
		
		return space;
	}
	
	
	public static ArrayList<String> generateXSD(ArrayList<String> input ){
		
		
		System.out.println("<?xml version = \"1.0\"?>\n");
		System.out.println("<xs:schema xmlns:xs = \"http://www.w3.org/2001/XMLSchema\">");
		System.out.println("<xs:element name = '" + input.get(0) + "XSDnew\" elementFormDefault=\"qualified\"attributeFormDefault=\"qualified\">");
		System.out.println("<xsd:complexType name=\"" + input.get(0) + "\">");
		for(int j = 1; j < input.size(); j++)
		System.out.println("<xsd:element name=\"" + input.get(j) + "\" type=\"xsd:string\" maxOccurs=\"1\" minOccurs=\"1\" />");
		
		System.out.println("</xsd:complexType >");
		System.out.println("</schema>");
		System.out.println("");
		return input;
		
	}
	
	public static ArrayList<String> cleanArray(ArrayList<String> input){
		
		if (input.get(0).contains(",")){						//check for the main table
			
			String[] splitTableName = input.get(0).split(",");
			input.set(0, splitTableName[0].replace(" ", ""));
			
	
		}
		for(int i = 1; i < input.size(); i++){
			
			if(input.get(i).contains("<") || input.get(i).contains(">") || input.get(i).toUpperCase().contains("AS")){
				
				input.remove(i);
				
			} else if(input.get(i).contains(".")){
				
				String[] splitWord = input.get(i).split("\\.");
				input.set(i, splitWord[splitWord.length - 1]);
			}
			
		}
		System.out.println(input);
		return input;
	}
	



}
