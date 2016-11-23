import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DTD {
	
	public static void main(String args[]) {
		ArrayList<String> tableInfo = new ArrayList<String>();
		ArrayList<String> dtdLines = new ArrayList<String>();
		
		
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		tableData.add(new ArrayList());
		
		tableInfo.add("S , C"); 
		tableInfo.add("S . SNO");
		tableInfo.add("AS");
		tableInfo.add("C . ID");
		tableInfo.add("< tagname");
		tableInfo.add("C . SNAME");
		tableInfo.add("tagname >");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
		
		String printOut = "";
		
		dtdSideBar(dtdLines,tableInfo);	 //Creates side bar
		for (int i  = 0; i < dtdLines.size(); i++){
			printOut += dtdLines.get(i) + "\n";
		}
	   
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
	
}
