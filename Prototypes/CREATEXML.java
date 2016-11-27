import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CREATEXML {
	
	static private String filename = "HelloWorld";
	static private String query = "Select SNO, ID FROM TABLE";
	
	public static void main(String args[]) {
		ArrayList<String> tableInfo = new ArrayList<String>();
		
		tableInfo.add("S"); 
		tableInfo.add("SNAME");
		tableInfo.add("< + tagname");
		tableInfo.add("SNO");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
		tableInfo.add("tagname >");
		
		XML(tableInfo);
		
	}
	
	public static void XML(ArrayList<String> tableInfo ){
		
		
		Boolean doesItContainAPlus = false;
		
		ArrayList<String> xmlLines = new ArrayList<String>();
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		
		tableData.add(new ArrayList());   //must have for it to load array
		
		if (tableInfo.get(0).contains(",")){						//check for the main table
			String[] splitTableName = tableInfo.get(0).split(",");
			tableInfo.set(0, splitTableName[0].replace(" ", ""));
		}

		xmlLines(xmlLines , tableInfo);
		for(int i = 0; i < tableInfo.size(); i++){
			if (tableInfo.get(i).contains("+")){
				doesItContainAPlus = true;
			}
		}
		
		try {
			if(doesItContainAPlus){
				sortAndRunQuery(tableData, tableInfo, query);
//				runQuery(tableData, tableInfo); //temp until fixed
			}
			else{
				runQuery(tableData, tableInfo); //fetch sql query
			}
		} catch (SQLException e) {
			System.out.println("Issue with connection");
			return;
		}
		
		
		String printOut = "<?xml version = \"1.0\"?>\n<!DOCTYPE " + tableInfo.get(0) + " INFORMATION \"" 
				+ filename + ".dtd\">\n\n";
		boolean plusFound = false;
		int recordIndex = 0;
		int startKIndex = 0;
		boolean recordIndexHit = true;
		int k = 0;  //index of the XMLLines array
		   for(int i = 1; i < tableData.size();i++){
			   printOut += xmlLines.get(0) + "\n";
//			   System.out.println(xmlLines.get(0));
			   
		        for(int j = 0; j < ((ArrayList<?>)tableData.get(i)).size(); j++){
		        	if (k + 2 > xmlLines.size() - 1){
		        		k = 0;
		        	}
		        	if (plusFound){
		        		if(recordIndexHit){
		        			recordIndex = j - 1;
		        			startKIndex = k;
		        			recordIndexHit = false;
		        		}
		        		k += 2;
		        		System.out.println(xmlLines.get(k) + ((ArrayList)tableData.get(i)).get(j) + xmlLines.get(k + 1));
		        		printOut += xmlLines.get(k) + ((ArrayList<?>)tableData.get(i)).get(j) + xmlLines.get(k + 1) + "\n";
		        		
		        		if(k  + 2 <xmlLines.size() && xmlLines.get(k + 2).contains("~")){
		        			if( i + 2 > tableData.size()){
		        				System.out.println(xmlLines.get(k + 2).replace("~", "").replace("@", ""));
		        				printOut += xmlLines.get(k + 2).replace("~", "").replace("@", "") + "\n";
		        				k = 0;
		        			}
		        			else if(tableData.get(i + 1).get(recordIndex).equals(tableData.get(i).get(recordIndex))){
		        				j = recordIndex;
		        				k = startKIndex;
		        				i++;
		        			}
		        			else{
		        				System.out.println(xmlLines.get(k + 2).replace("~", "").replace("@", ""));
		        				printOut += xmlLines.get(k + 2).replace("~", "").replace("@", "") + "\n";;
		        				recordIndexHit = true;
		        				plusFound = false;
		        				k = 0;
		        			}
		        		}
		        		
		        	}
		        	else if (xmlLines.get(k + 2).contains("@") && xmlLines.get(k + 2).contains("+")){
		        		System.out.println(xmlLines.get(k + 2).replace("@", ""));
		        		printOut += xmlLines.get(k + 2).replace("@", "") + "\n";
		        		k++;
						j--;
						plusFound = true;
					}
		        	else if (xmlLines.get(k + 2).contains("@")){
		        		printOut += xmlLines.get(k + 2).replace("@", "") + "\n";
//		        		System.out.println(xmlLines.get(k + 2).replace("@", ""));
		        		k++;
						j--;
					}
		        	else if (xmlLines.get(k + 2).contains("~")){
		        		printOut += xmlLines.get(k + 2).replace("~", "") + "\n";;
//		        		System.out.println(xmlLines.get(k + 2).replace("~", ""));
		        		k++;
						j--;
					}
		        	else{
		        		k+=2;
		        		printOut += xmlLines.get(k) + ((ArrayList<?>)tableData.get(i)).get(j) + xmlLines.get(k + 1) + "\n";
//		        		System.out.println(xmlLines.get(k) + ((ArrayList)tableData.get(i)).get(j) + xmlLines.get(k + 1));
		        		
		        		if(k  + 2 <xmlLines.size() && xmlLines.get(k + 2).contains("~")){
		        			printOut += xmlLines.get(k + 2).replace("~", "") + "\n";;
//			        		System.out.println(xmlLines.get(k + 2).replace("~", ""));
			        		k++;
		        		}
		        		
		        	}
		        }
	       printOut += xmlLines.get(1) + "\n";
//	       System.out.println(xmlLines.get(1));
		   }
		
//		if (nameFile){
//			createFile(printOut, filename += ".xml");
//		}
		System.out.println(printOut);
	}
	
	
		@SuppressWarnings("unchecked")
		private static ArrayList<ArrayList<?>> sortAndRunQuery(ArrayList<ArrayList<?>> tableData, ArrayList<String> tableInfo, String query) throws SQLException {
			ArrayList<String> arrayAfter = new ArrayList<String>();
			///add item after + to queue to see if contains later
			for(int i = 0; i < tableInfo.size(); i++){
				if(tableInfo.get(i).contains("+")){
					arrayAfter.add(tableInfo.get(i + 1));   //get index after < to capture clean then grab the element before
				}											//this way it can be added to the ORDER BY statement
			}
			cleanArray(arrayAfter);
			cleanArray(tableInfo);
			arrayAfter.add(tableInfo.get(tableInfo.indexOf(arrayAfter.get(arrayAfter.size() - 1)) - 1));
			query += " ORDER BY " + arrayAfter.get(arrayAfter.size() - 1);				//create ORDER BY Statement
			
			
			((ArrayList<String>)tableData.get(0)).add(tableInfo.get(0));
			tableData.add(new ArrayList());
			
	        try {
	            Class.forName("oracle.jdbc.driver.OracleDriver");
	            
	        }
	        catch (ClassNotFoundException e){
	            System.out.println("Could not load driver.");
	        }
	        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
	        
	        Statement stmt = conn.createStatement();
	        ResultSet res = stmt.executeQuery(query);
	        int j = 1;
	        while( res.next() ) {
	        	for (int i = 1; i < tableInfo.size(); i++){
	        		((ArrayList<String>)tableData.get(j)).add(res.getString(i));

	        	}
	        	tableData.add(new ArrayList());
	        	j++;
	        }
	        
	        stmt.close();
	        return tableData;
		
	}

	public static ArrayList<String> xmlLines (ArrayList<String> xmlLines, ArrayList<String> tableInfo){  //This creates the lines going into the code 
																										// example <ID   table="Animals" name="ID"> and </ID>
		int tagNameDepthCount = 1;
		
		for (int i = 0; i < tableInfo.size() ; i++){	
			
			if(i == 0){
				xmlLines.add("<" + tableInfo.get(0) + ">");
				xmlLines.add("</" + tableInfo.get(0) + ">");
			}
			else if(i != tableInfo.size() - 1 && tableInfo.get(i + 1).toUpperCase().contains("AS")){    //need to clean this up to an arrayList this is bad!!!
				String[] leftOfAS = new String[10];
				String[] rightOfAs = new String[10];
				if (tableInfo.get(i).contains(".")){
					leftOfAS = tableInfo.get(i).split("\\.");
				}
				else{
					leftOfAS[1] = tableInfo.get(i);
				}
				if(tableInfo.get(i + 2).contains(".")){						//see if it contains tablename
					rightOfAs = tableInfo.get(i + 2).split("\\.");
				}
				else{
					rightOfAs[1] = tableInfo.get(i + 2);;
				}
				if(rightOfAs[1].contains(" ")){								//strip spaces on both left and right
					rightOfAs[1] = rightOfAs[1].replace(" ", "");
				}
				if(leftOfAS[1].contains(" ")){
					leftOfAS[1] = leftOfAS[1].replace(" ", "");
				}
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + rightOfAs[1] + "   table=\"" + tableInfo.get(0) + "\" name=\"" + leftOfAS[1] + "\">");
				xmlLines.add("</" + rightOfAs[1] + ">");
				i+=2;
			}
			else if(tableInfo.get(i).contains("<")){
				xmlLines.add("@" + tabMaker(tagNameDepthCount) + tableInfo.get(i).replace(" ", "") + ">");
				tagNameDepthCount++;
			}
			else if(tableInfo.get(i).contains(">")){
				tagNameDepthCount--;
				xmlLines.add("~" + tabMaker(tagNameDepthCount) + "</" + tableInfo.get(i).replace(" ", ""));
			}
			else if(tableInfo.get(i).contains(".")){
				String[] table = tableInfo.get(i).split("\\."); 
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + table[1].replace(" ", "") + "   table=\"" + table[0] + "\" name=\"" + table[1] + "\">");
				xmlLines.add("</" + table[1] + ">");
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
	private static ArrayList<ArrayList<?>> runQuery(ArrayList<ArrayList<?>> tableData, ArrayList<String> tableInfo) throws SQLException {
		
		tableData.add(new ArrayList());
	      
		((ArrayList<String>)tableData.get(1)).add("Adams");
	      ((ArrayList<String>)tableData.get(1)).add("S1");
	      ((ArrayList<String>)tableData.get(1)).add("3000");
	      ((ArrayList<String>)tableData.get(1)).add("Dallas");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(2)).add("Adams");
	      ((ArrayList<String>)tableData.get(2)).add("S2");
	      ((ArrayList<String>)tableData.get(2)).add("10000");
	      ((ArrayList<String>)tableData.get(2)).add("Chicago");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(3)).add("Knapp");
	      ((ArrayList<String>)tableData.get(3)).add("S3");
	      ((ArrayList<String>)tableData.get(3)).add("7500");
	      ((ArrayList<String>)tableData.get(3)).add("Phoenix");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(4)).add("Lime");
	      ((ArrayList<String>)tableData.get(4)).add("S4");
	      ((ArrayList<String>)tableData.get(4)).add("13000");
	      ((ArrayList<String>)tableData.get(4)).add("San Diego");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(5)).add("Martin");
	      ((ArrayList<String>)tableData.get(5)).add("S5");
	      ((ArrayList<String>)tableData.get(5)).add("25000");
	      ((ArrayList<String>)tableData.get(5)).add("New York");
	      
//	      for(int i = 0; i < tableData.size();i++){
//	          for(int j = 0; j < ((ArrayList)tableData.get(i)).size(); j++){
//	             System.out.print( (String)((ArrayList)tableData.get(i)).get(j) +"  ");
//	          }
//	          System.out.println();
//	       }
        
        return tableData;
    }
	
public static ArrayList<String> cleanArray(ArrayList<String> input){
		
		for(int i = 0; i < input.size(); i++){
			
			if (input.get(i).contains(",")){						//check for the main table
				String[] splitTableName = input.get(0).split(",");
				input.set(0, splitTableName[0].replace(" ", ""));
			}
			
			else if(input.get(i).contains("<") || input.get(i).contains(">")){
				
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
