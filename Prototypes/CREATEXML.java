import java.sql.SQLException;
import java.util.ArrayList;

public class CREATEXML {
	
	public static void main(String args[]) {
		
		ArrayList<String> tableInfo = new ArrayList<String>();
		ArrayList<String> xmlLines = new ArrayList<String>();
		
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		tableData.add(new ArrayList());
		
		tableInfo.add("S"); 
		tableInfo.add("SNO");
		tableInfo.add("AS");
		tableInfo.add("ID");
		tableInfo.add("< tagname");
		tableInfo.add("SNAME");
		tableInfo.add("tagname >");
		tableInfo.add("QUOTA");
		tableInfo.add("< tagname");
		tableInfo.add("CITY");
		tableInfo.add("tagname >");
		
		try {
			runQuery(tableData); //fetch sql query
		} catch (SQLException e) {
			System.out.println("Issue with connection");
		}
		
		String printOut = ""; 
		xmlLines(xmlLines , tableInfo);
		
		int k = 0;
		   for(int i = 1; i < tableData.size();i++){
			   printOut += xmlLines.get(0) + "\n";
			   System.out.println(xmlLines.get(0));
			   
		        for(int j = 0; j < ((ArrayList)tableData.get(i)).size(); j++){
		        	if (k + 2 > xmlLines.size() - 1){
		        		k = 0;
		        	}
		        	if (xmlLines.get(k + 2).contains("@")){
		        		printOut += xmlLines.get(k + 2).replace("@", "") + "\n";
		        		System.out.println(xmlLines.get(k + 2).replace("@", ""));
		        		k++;
						j--;
					}
		        	else if (xmlLines.get(k + 2).contains("~")){
		        		printOut += xmlLines.get(k + 2).replace("~", "") + "\n";;
		        		System.out.println(xmlLines.get(k + 2).replace("~", ""));
		        		k++;
						j--;
					}
		        	else{
		        		k+=2;
		        		printOut += xmlLines.get(k) + ((ArrayList)tableData.get(i)).get(j) + xmlLines.get(k + 1) + "\n";
		        		System.out.println(xmlLines.get(k) + ((ArrayList)tableData.get(i)).get(j) + xmlLines.get(k + 1));
		        		
		        		if(k  + 2 <xmlLines.size() && xmlLines.get(k + 2).contains("~")){
		        			printOut += xmlLines.get(k + 2).replace("~", "") + "\n";;
			        		System.out.println(xmlLines.get(k + 2).replace("~", ""));
			        		k++;
		        		}
		        		
		        	}
		        }
	       printOut += xmlLines.get(1) + "\n";
	       System.out.println(xmlLines.get(1));
		   }
		
		System.out.println(printOut);
	}
	
	public static ArrayList<String> xmlLines (ArrayList<String> xmlLines, ArrayList<String> tableInfo){  //This creates the lines going into the code 
																										// example <ID   table="Animals" name="ID"> and </ID>
		int tagNameDepthCount = 1;
		
		for (int i = 0; i < tableInfo.size() ; i++){	
			
			if(i == 0){
				xmlLines.add("<" + tableInfo.get(0) + ">");
				xmlLines.add("</" + tableInfo.get(0) + ">");
			}
			else if(i != tableInfo.size() - 1 && tableInfo.get(i + 1).equals("AS")){
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + tableInfo.get(i + 2) + "   table=\"" + tableInfo.get(0) + "\" name=\"" + tableInfo.get(i) + "\">");
				xmlLines.add("</" + tableInfo.get(i + 2) + ">");
				i+=2;
			}
			else if(tableInfo.get(i).contains("<")){
				xmlLines.add("@" + tabMaker(tagNameDepthCount) + tableInfo.get(i) + ">");
				tagNameDepthCount++;
			}
			else if(tableInfo.get(i).contains(">")){
				tagNameDepthCount--;
				xmlLines.add("~" + tabMaker(tagNameDepthCount) + "</" + tableInfo.get(i));
			}
			else{
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + tableInfo.get(i) + "   table=\"" + tableInfo.get(0) + "\" name=\"" + tableInfo.get(i) + "\">");
				xmlLines.add("</" + tableInfo.get(i) + ">");
			}
			
		}
		return xmlLines;
	}
	
	public static String tabMaker(int tagNameDepthCount){
		String space = "";
		for(int i = 0; i < tagNameDepthCount; i++){
			space += "\t";
		}
		
		return space;
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
