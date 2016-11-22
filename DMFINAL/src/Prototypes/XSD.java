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
		
		tableInfo.add("S"); 
		tableInfo.add("SNO"); 
		tableInfo.add("SNAME");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
		
		generateXSD(tableInfo);

	}
	
	public static ArrayList<String> generateXSD(ArrayList<String> input ){
		
		
		System.out.println("<?xml version = \"1.0\"?>\n");
		System.out.println("<xs:schema xmlns:xs = \"http://www.w3.org/2001/XMLSchema\">");
		
		
		return input;
		
	}
	



}
