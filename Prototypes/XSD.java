import java.sql.*;
import java.util.ArrayList;
import oracle.jdbc.driver.OracleDriver;

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
        tableInfo.add("< tagname");
        tableInfo.add("CNAME");
        tableInfo.add("tagname >");
        tableInfo.add("< tagname");
        tableInfo.add("CNO");
        tableInfo.add("tagname >");

		generateXSD(tableInfo);
	}
	

	public static void generateXSD(ArrayList<String> input)
    {
		
		String printOut = "";
        String[] tables = input.get(0).replaceAll(" ", "").split(",");
        String dataType;
		
		cleanArray(input);

        printOut += "<?xml version = \"1.0\"?>\n";
        printOut += "<xs:schema xmlns:xs = \"http://www.w3.org/2001/XMLSchema\">\n";
        printOut += "<xs:element name = 'XSDnew\" elementFormDefault=\"qualified\"attributeFormDefault=\"qualified\">\n";
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

//		if (nameFile){
//			createFile(printOut, filename += ".xsd");
//		}
        }
        printOut += "</schema>\n\n";
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
}
