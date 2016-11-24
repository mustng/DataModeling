import java.sql.SQLException;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

public class XMLMaker {
	static private String filename = "MyXML";
	static boolean nameFile = false;

	public XMLMaker(){
		
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
		
		if (nameFile){
			createFile(printOut, filename += ".xsd");
		}
		System.out.println(printOut);
		
	}
	
	
	public static void DTD(ArrayList<String> tableInfo ){
		ArrayList<String> dtdLines = new ArrayList<String>();
		
		
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		tableData.add(new ArrayList());
		
		String printOut = "";
		
		
		cleanArray(tableInfo);
		dtdSideBar(dtdLines,tableInfo);	 //Creates side bar
		for (int i  = 0; i < dtdLines.size(); i++){
			printOut += dtdLines.get(i) + "\n";
		}
	   
		if (nameFile){
			createFile(printOut, filename += ".dtd");
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
	
	public static ArrayList<String> cleanArray(ArrayList<String> input){
		
		if (input.get(0).contains(",")){						//check for the main table
			
			String[] splitTableName = input.get(0).split(",");
			input.set(0, splitTableName[0].replace(" ", ""));
			
	
		}
		for(int i = 1; i < input.size(); i++){
			
			if(input.get(i).contains("<") || input.get(i).contains(">") || input.get(i).toUpperCase().contains("AS")){
				
				input.remove(i);
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
	
	public static void XML(ArrayList<String> tableInfo ){
		ArrayList<String> xmlLines = new ArrayList<String>();
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		
		tableData.add(new ArrayList());   //must have for it to load array
		
		try {
			runQuery(tableData); //fetch sql query
		} catch (SQLException e) {
			System.out.println("Issue with connection");
		}
		
		if (tableInfo.get(0).contains(",")){						//check for the main table
			String[] splitTableName = tableInfo.get(0).split(",");
			tableInfo.set(0, splitTableName[0].replace(" ", ""));
		}
		
		String printOut = "<?xml ve rsion = \"1.0\"?>\n<!DOCTYPE " + tableInfo.get(0) + " INFORMATION \"" 
				+ filename + ".dtd\">\n\n"; 
		xmlLines(xmlLines , tableInfo);
		
		int k = 0;
		   for(int i = 1; i < tableData.size();i++){
			   printOut += xmlLines.get(0) + "\n";
//			   System.out.println(xmlLines.get(0));
			   
		        for(int j = 0; j < ((ArrayList<?>)tableData.get(i)).size(); j++){
		        	if (k + 2 > xmlLines.size() - 1){
		        		k = 0;
		        	}
		        	if (xmlLines.get(k + 2).contains("@")){
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
		
		if (nameFile){
			createFile(printOut, filename += ".xml");
		}
		System.out.println(printOut);
	}

	public static void setFileName(String file){
		filename = "";
		nameFile = true;
		filename += file;
	}
	

	public static void resetFileName(){
		nameFile = false;
	}
	
	private static void createFile(String info, String name) {
		Charset utf8 = StandardCharsets.UTF_8;
		try {
		    
		    Files.write(Paths.get(name), info.getBytes(utf8));
	
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	
	public static ArrayList<String> xmlLines (ArrayList<String> xmlLines, ArrayList<String> tableInfo){  //This creates the lines going into the code 
																										// example <ID   table="Animals" name="ID"> and </ID>
		int tagNameDepthCount = 1;
		
		for (int i = 0; i < tableInfo.size() ; i++){	
			
			if(i == 0){
				xmlLines.add("<" + tableInfo.get(0) + ">");
				xmlLines.add("</" + tableInfo.get(0) + ">");
			}
			else if(i != tableInfo.size() - 1 && tableInfo.get(i + 1).toUpperCase().contains("AS")){
				String[] leftOfAS = new String[10];
				String[] rightOfAs = new String[10];
				if (tableInfo.get(i).contains(".")){
					leftOfAS = tableInfo.get(i).split("\\.");
				}
				else{
					leftOfAS[1] = tableInfo.get(i);
				}
				if(tableInfo.get(i + 2).contains(".")){
					rightOfAs = tableInfo.get(i + 2).split("\\.");
				}
				else{
					rightOfAs[1] = tableInfo.get(i + 2);;
				}
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + rightOfAs[1].replace(" ", "") + "   table=\"" + tableInfo.get(0) + "\" name=\"" + leftOfAS[1].replace(" ", "") + "\">");
				xmlLines.add("</" + rightOfAs[1].replace(" ", "") + ">");
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
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + table[1] + "   table=\"" + table[0] + "\" name=\"" + table[1] + "\">");
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
	private static ArrayList<ArrayList<?>> runQuery(ArrayList<ArrayList<?>> tableData) throws SQLException {
		
		tableData.add(new ArrayList());
	      
	      ((ArrayList<String>)tableData.get(1)).add("S1");
	      ((ArrayList<String>)tableData.get(1)).add("Adams");
	      ((ArrayList<String>)tableData.get(1)).add("3000");
	      ((ArrayList<String>)tableData.get(1)).add("Dallas");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(2)).add("S2");
	      ((ArrayList<String>)tableData.get(2)).add("Smith");
	      ((ArrayList<String>)tableData.get(2)).add("10000");
	      ((ArrayList<String>)tableData.get(2)).add("Chicago");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(3)).add("S3");
	      ((ArrayList<String>)tableData.get(3)).add("Jones");
	      ((ArrayList<String>)tableData.get(3)).add("7500");
	      ((ArrayList<String>)tableData.get(3)).add("Phoenix");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(4)).add("S4");
	      ((ArrayList<String>)tableData.get(4)).add("Knapp");
	      ((ArrayList<String>)tableData.get(4)).add("13000");
	      ((ArrayList<String>)tableData.get(4)).add("San Diego");
	      tableData.add(new ArrayList());
	      ((ArrayList<String>)tableData.get(5)).add("S5");
	      ((ArrayList<String>)tableData.get(5)).add("Martin");
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
	
}
