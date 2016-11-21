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
		ArrayList<String> dtdSideLines = new ArrayList<String>();
		
		
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		tableData.add(new ArrayList());
		
		tableInfo.add("S"); 
		tableInfo.add("SNO"); 
		tableInfo.add("SNAME");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
		
		try {
			runQuery(tableData);
		} catch (SQLException e) {
			System.out.println("Issue with connection");
		}
		
		
		String printOut = "<?xml version = \"1.0\"?>\n<!DOCTYPE " + tableInfo.get(0) + " INFORMATION \"" + tableInfo.get(0) + ".dtd\">\n"; 
		
		dtdLines(dtdLines, tableInfo);       //Creates lines by line everything
		dtdSideBar(dtdSideLines,tableInfo);	 //Creates side bar

		int k = 0;
	   for(int i = 1; i < tableData.size();i++){
		   printOut += dtdLines.get(0) + "\n";
        for(int j = 0; j < ((ArrayList)tableData.get(i)).size(); j++){
        	if (k + 2 > dtdLines.size() - 1){
        		k = 0;
        	}
        	k+=2;
        	printOut += dtdLines.get(k) + ((ArrayList)tableData.get(i)).get(j) + dtdLines.get(k + 1) + dtdSideLines.get(0) + "\n";
        	if(dtdSideLines.size() > 1){
        		dtdSideLines.remove(0);
        	}
        	}
           printOut += dtdLines.get(1) + "\n";
	   }
	   
		System.out.println(printOut);
	}
	
	public static ArrayList<String> dtdSideBar(ArrayList<String> dtdSideLines, ArrayList<String> tableInfo){
		dtdSideLines.add("\t\t\t<?xml ve rsion=\"1.0\"?>");
		dtdSideLines.add("\t\t\t<!DOCTYPE " + tableInfo.get(0) + " [");
		dtdSideLines.add("\t\t\t<!ELEMENT " + tableInfo.get(0) + "(");
		for (int i = 1; i < tableInfo.size(); i++){
			if(i + 1 >= tableInfo.size()){
				dtdSideLines.set(2, dtdSideLines.get(2) + " " + tableInfo.get(i));
			}
			else{
				dtdSideLines.set(2, dtdSideLines.get(2) + " " + tableInfo.get(i) + ",");
			}
			dtdSideLines.add("\t\t\t<!ELEMENT " + tableInfo.get(i) + " (#PCDATA)>");
		}
		dtdSideLines.add("\t\t\t]>");
		dtdSideLines.set(2,  dtdSideLines.get(2) + ")>");
		dtdSideLines.add(" ");
		
		return dtdSideLines;
	}
	
	public static ArrayList<String> dtdLines (ArrayList<String> dtdLines, ArrayList<String> tableInfo){  //This creates the lines going into the code 
																										// example <ID   table="Animals" name="ID"> and </ID>
		int tagNameDepthCount = 0;
		
		for (int i = 0; i < tableInfo.size() ; i++){	
			
			if(i == 0){
				dtdLines.add("<" + tableInfo.get(0) + ">");
				dtdLines.add("</" + tableInfo.get(0) + ">");
			}
			else if(i != tableInfo.size() - 1 && tableInfo.get(i + 1).equals("AS")){
				dtdLines.add("<" + tableInfo.get(0) + ">");
				dtdLines.add("</" + tableInfo.get(i + 2) + ">");
				i+=2;
			}
			else if(tableInfo.get(i).contains("<")){
				dtdLines.add(tableInfo.get(i) + ">");
				tagNameDepthCount++;
			}
			else if(tableInfo.get(i).contains(">")){
				tagNameDepthCount--;
				dtdLines.add("</" + tableInfo.get(i));
			}
			else{
				dtdLines.add("<" + tableInfo.get(i)  + ">");
				dtdLines.add("</" + tableInfo.get(i) + ">");
			}
			
		}
		return dtdLines;
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<ArrayList<?>> runQuery(ArrayList<ArrayList<?>> tableData) throws SQLException {
		
		tableData.add(new ArrayList());
	      
	      ((ArrayList)tableData.get(1)).add("S1");
	      ((ArrayList)tableData.get(1)).add("Adams");
	      ((ArrayList)tableData.get(1)).add("3000");
	      ((ArrayList)tableData.get(1)).add("Dallas");
	      tableData.add(new ArrayList());
	      ((ArrayList)tableData.get(2)).add("S2");
	      ((ArrayList)tableData.get(2)).add("Smith");
	      ((ArrayList)tableData.get(2)).add("10000");
	      ((ArrayList)tableData.get(2)).add("Chicago");
	      tableData.add(new ArrayList());
	      ((ArrayList)tableData.get(3)).add("S3");
	      ((ArrayList)tableData.get(3)).add("Jones");
	      ((ArrayList)tableData.get(3)).add("7500");
	      ((ArrayList)tableData.get(3)).add("Phoenix");
	      tableData.add(new ArrayList());
	      ((ArrayList)tableData.get(4)).add("S4");
	      ((ArrayList)tableData.get(4)).add("Knapp");
	      ((ArrayList)tableData.get(4)).add("13000");
	      ((ArrayList)tableData.get(4)).add("San Diego");
	      tableData.add(new ArrayList());
	      ((ArrayList)tableData.get(5)).add("S5");
	      ((ArrayList)tableData.get(5)).add("Martin");
	      ((ArrayList)tableData.get(5)).add("25000");
	      ((ArrayList)tableData.get(5)).add("New York");
	      
//	      for(int i = 0; i < tableData.size();i++){
//	          for(int j = 0; j < ((ArrayList)tableData.get(i)).size(); j++){
//	             System.out.print( (String)((ArrayList)tableData.get(i)).get(j) +"  ");
//	          }
//	          System.out.println();
//	       }
        
        return tableData;
    }
}
