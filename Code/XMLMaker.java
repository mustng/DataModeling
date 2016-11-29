import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class XMLMaker {
	static private String filename = "MyXML";
	static private boolean nameFile = false;
	static private String query = "";
	
	public static void generateXSD(ArrayList<String> input)
    {
		
		String printOut = "";
        String[] tables = input.get(0).replaceAll(" ", "").split(",");
        String dataType;
		
		cleanArray(input);
		
		String temp2 = filename;
        printOut += "\n\n<?xml version = \"1.0\"?>\n";
        printOut += "<xs:schema xmlns:xs = \"http://www.w3.org/2001/XMLSchema\">\n";
        printOut += "<xs:element name = \"" + temp2 + "\" elementFormDefault=\"qualified\"attributeFormDefault=\"qualified\">\n";
        for (int i = 0; i < tables.length; i++)
        {
            printOut += "<xsd:complexType name=\"" + tables[i] + "\">\n";
            for(int j = 1; j < input.size(); j++)
            {
                dataType = getDataType(tables[i], input.get(j).replaceAll(" ", ""));
                if (dataType != null)
                    printOut += "<xsd:element name=\"" + input.get(j) + "\" type=\"xsd:" + dataTypeConv(dataType) + " />\n";
                dataType = null;
            }
            printOut += "</xsd:complexType >\n";
            
        String temp = filename;
		if (nameFile){
			createFile(printOut, temp += ".xsd");
		}
        }
        printOut += "</schema>\n\n";
        
        System.out.println(printOut);
	}
	
	public static String getDataType(String table, String colName)
	{
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");

        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load driver.");
        }
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("Select COLUMN_NAME, data_type from user_tab_columns where TABLE_NAME = '" + table + "'");

            res.next();
            while(!colName.toLowerCase().matches(res.getString(1).toLowerCase()))
                res.next();

            String dataType = res.getString(2);
            stmt.close();
            return dataType;
        }
        catch (SQLException f)
        {
            return null;
        }
	}
	
	public static String dataTypeConv(String dataType)
	{
		dataType = dataType.toLowerCase();
		if (dataType.matches("(character|varchar|character varying)(.*)"))
			return "string";
		else if (dataType.matches("boolean"))
			return "boolean";
		else if (dataType.matches("(integer|smallint|bigint|decimal|numeric|number)(.*)"))
			return "decimal";
		else if (dataType.matches("(float|real)(.*)"))
			return "float";
		else if (dataType.matches("double precision"))
			return "double";
		else if (dataType.matches("interval"))
			return "duration";
		else if (dataType.matches("timestamp"))
			return "dateTime";
		else if (dataType.matches("time"))
			return "time";
		else if (dataType.matches("date"))
			return "date";
		else
			return null;
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
	   String temp = filename;
		if (nameFile){
			createFile(printOut, temp += ".dtd");
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
			sortAndRunQuery(tableData, tableInfo, query); //when doing query uses ORDER BY to organize the items
		}
		else{
			runQuery(tableData, tableInfo); //fetch sql query
		}
	} catch (SQLException e) {
		System.out.println("Issue with connection or running a bad query against the database!");
		return;
	}
	
	String temp = filename;
	String printOut = "<?xml version = \"1.0\"?>\n<!DOCTYPE " + tableInfo.get(0) + " INFORMATION \"" 
			+ temp + ".dtd\">\n\n";
	boolean plusFound = false;
	int recordIndex = 0;
	int startKIndex = 0;
	boolean recordIndexHit = true;
	int k = 0;  //index of the XMLLines array
	
	System.out.println(xmlLines);

	   for(int i = 1; i < tableData.size();i++){
		   
	        for(int j = 0; j < ((ArrayList<?>)tableData.get(i)).size(); j++){
	        	if (j == 0){
	        		printOut += xmlLines.get(0) + "\n";
//	        		System.out.println(xmlLines.get(0));
	        	}
	 		   
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
//	        		System.out.println(xmlLines.get(k) + ((ArrayList)tableData.get(i)).get(j) + xmlLines.get(k + 1));
	        		printOut += xmlLines.get(k) + ((ArrayList<?>)tableData.get(i)).get(j) + xmlLines.get(k + 1) + "\n";
	        		
	        		if(k  + 2 <xmlLines.size() && xmlLines.get(k + 2).contains("~")){
	        			if( i + 2 > tableData.size()){
//	        				System.out.println(xmlLines.get(k + 2).replace("~", "").replace("@", ""));
	        				printOut += xmlLines.get(k + 2).replace("~", "").replace("@", "") + "\n";
	        				k = 0;
	        			}
	        			else if(tableData.get(i + 1).get(recordIndex).equals(tableData.get(i).get(recordIndex))){
	        				j = recordIndex;
	        				k = startKIndex;
	        				i++;
	        			}
	        			else{
//	        				System.out.println(xmlLines.get(k + 2).replace("~", "").replace("@", ""));
	        				printOut += xmlLines.get(k + 2).replace("~", "").replace("@", "") + "\n";;
	        				recordIndexHit = true;
	        				plusFound = false;
	        				k = 0;
	        			}
	        		}
	        		
	        	}
	        	else if (xmlLines.get(k + 2).contains("@") && xmlLines.get(k + 2).contains("+")){
//	        		System.out.println(xmlLines.get(k + 2).replace("@", ""));
	        		printOut += xmlLines.get(k + 2).replace("@", "") + "\n";
	        		k++;
					j--;
					plusFound = true;
				}
	        	else if (xmlLines.get(k + 2).contains("@")){
	        		printOut += xmlLines.get(k + 2).replace("@", "") + "\n";
//	        		System.out.println(xmlLines.get(k + 2).replace("@", ""));
	        		k++;
					j--;
				}
	        	else if (xmlLines.get(k + 2).contains("~")){
	        		printOut += xmlLines.get(k + 2).replace("~", "") + "\n";;
//	        		System.out.println(xmlLines.get(k + 2).replace("~", ""));
	        		k++;
					j--;
				}
	        	else{
	        		k+=2;
	        		printOut += xmlLines.get(k) + ((ArrayList<?>)tableData.get(i)).get(j) + xmlLines.get(k + 1) + "\n";
//	        		System.out.println(xmlLines.get(k) + ((ArrayList)tableData.get(i)).get(j) + xmlLines.get(k + 1));
	        		
	        		if(k  + 2 <xmlLines.size() && xmlLines.get(k + 2).contains("~")){
	        			printOut += xmlLines.get(k + 2).replace("~", "") + "\n";;
//		        		System.out.println(xmlLines.get(k + 2).replace("~", ""));
		        		k++;
	        		}
	        		
	        	}
	        	if (j == ((ArrayList<?>)tableData.get(i)).size() - 1){
	        		printOut += xmlLines.get(1) + "\n";
//	        		System.out.println(xmlLines.get(1));
	        	}
	        }
	   }
	String temp2 = filename;
	if (nameFile){
		createFile(printOut, temp2 += ".xml");
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
	private static ArrayList<ArrayList<?>> runQuery(ArrayList<ArrayList<?>> tableData, ArrayList<String> tableInfo) throws SQLException {
		
		cleanArray(tableInfo);
		
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
        
	      for(int m = 0; m < tableData.size();m++){
	          for(int n = 0; n < ((ArrayList)tableData.get(m)).size(); n++){
	             System.out.print( (String)((ArrayList)tableData.get(m)).get(n) +"  ");
	          }
	          System.out.println();
	       }
        
        return tableData;
    }
	
	
	public static ArrayList<String> getAttributes(ArrayList<String> table)
	{
		table.remove(table.indexOf("*"));
		String[] tbls = table.get(0).replaceAll(" ", "").split(",");
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Could not load driver.");
		}
		try
		{
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
			Statement stmt = conn.createStatement();
			for (int i = 0; i < tbls.length; i++)
			{
				ResultSet res = stmt.executeQuery("Select COLUMN_NAME, data_type from user_tab_columns where TABLE_NAME = '"+ tbls[i] +"'");
				while( res.next() )
					table.add(res.getString(1));
			}
			stmt.close();


			return table;
		}
		catch (SQLException f)
		{
			return null;
		}

	}
	
//	@SuppressWarnings("unchecked")
//	private static ArrayList<ArrayList<?>> runQuery(ArrayList<ArrayList<?>> tableData, ArrayList<String> tableInfo) throws SQLException {
//		
//		tableData.add(new ArrayList());
//	      
//	      ((ArrayList<String>)tableData.get(1)).add("S1");
//	      ((ArrayList<String>)tableData.get(1)).add("Adams");
//	      ((ArrayList<String>)tableData.get(1)).add("3000");
//	      ((ArrayList<String>)tableData.get(1)).add("Dallas");
//	      tableData.add(new ArrayList());
//	      ((ArrayList<String>)tableData.get(2)).add("S2");
//	      ((ArrayList<String>)tableData.get(2)).add("Smith");
//	      ((ArrayList<String>)tableData.get(2)).add("10000");
//	      ((ArrayList<String>)tableData.get(2)).add("Chicago");
//	      tableData.add(new ArrayList());
//	      ((ArrayList<String>)tableData.get(3)).add("S3");
//	      ((ArrayList<String>)tableData.get(3)).add("Jones");
//	      ((ArrayList<String>)tableData.get(3)).add("7500");
//	      ((ArrayList<String>)tableData.get(3)).add("Phoenix");
//	      tableData.add(new ArrayList());
//	      ((ArrayList<String>)tableData.get(4)).add("S4");
//	      ((ArrayList<String>)tableData.get(4)).add("Knapp");
//	      ((ArrayList<String>)tableData.get(4)).add("13000");
//	      ((ArrayList<String>)tableData.get(4)).add("San Diego");
//	      tableData.add(new ArrayList());
//	      ((ArrayList<String>)tableData.get(5)).add("S5");
//	      ((ArrayList<String>)tableData.get(5)).add("Martin");
//	      ((ArrayList<String>)tableData.get(5)).add("25000");
//	      ((ArrayList<String>)tableData.get(5)).add("New York");
//	      ((ArrayList<String>)tableData.get(5)).add("New York");
//	      
////	      for(int i = 0; i < tableData.size();i++){
////	          for(int j = 0; j < ((ArrayList)tableData.get(i)).size(); j++){
////	             System.out.print( (String)((ArrayList)tableData.get(i)).get(j) +"  ");
////	          }
////	          System.out.println();
////	       }
//        
//        return tableData;
//    }


	public static void setQuery(String query2) {
		query += query2;
	}


	public static void resetQuery() {
		query = "";
	}
	
}
