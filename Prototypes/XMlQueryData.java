import java.util.ArrayList;
import java.sql.*;

public class XMlQueryData {
	
	public static void main(String args[]) throws SQLException {
		
		ArrayList<ArrayList<?>> tableData = new ArrayList<ArrayList<?>>();
		tableData.add(new ArrayList());
		
		ArrayList<ArrayList<?>> fakeData = new ArrayList<ArrayList<?>>();
		fakeData.add(new ArrayList());
		
		runQuery(tableData);
		System.out.println(tableData + "\n\n");
		
		runQueryFake(fakeData);
		System.out.println(fakeData);
		
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<ArrayList<?>> runQuery(ArrayList<ArrayList<?>> tableData) throws SQLException {
		
		ArrayList<String> getAttributes = new ArrayList<String>();
		getAttributes(getAttributes);
		
		getAttributes.add("S");  //Table Name
		((ArrayList<String>)tableData.get(0)).add("S");
		tableData.add(new ArrayList());
		
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (ClassNotFoundException e){
            System.out.println("Could not load driver.");
        }
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
        
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery("select * FROM S");
        int j = 1;
        while( res.next() ) {
        	for (int i = 1; i < getAttributes.size(); i++){
        		((ArrayList<String>)tableData.get(j)).add(res.getString(i));

        	}
        	tableData.add(new ArrayList());
        	j++;
        }
        
        stmt.close();
        return tableData;
    }
	
	@SuppressWarnings("unchecked")
	private static ArrayList<ArrayList<?>> runQueryFake(ArrayList<ArrayList<?>> tableData) throws SQLException {
		
		ArrayList<String> getAttributes = new ArrayList<String>();
		getAttributes(getAttributes);
		
		getAttributes.add("S");  //Table Name
		((ArrayList<String>)tableData.get(0)).add("S");
		tableData.add(new ArrayList());
		
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
	      
//	      int i = 0;
//	      int j = 0;
//	      for(i = 0; i < tableData.size();i++){
//	          for(j = 0; j < ((ArrayList)tableData.get(i)).size(); j++){
//	             System.out.print( (String)((ArrayList)tableData.get(i)).get(j) +"  ");
//	          }
//	          System.out.println();
//	       }
        
        return tableData;
    }
	
	public static ArrayList<String> getAttributes(ArrayList<String> getAttributes) throws SQLException{
		
		try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (ClassNotFoundException e){
            System.out.println("Could not load driver.");
        }
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
        
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery("Select COLUMN_NAME from user_tab_columns where TABLE_NAME = 'S'");
        while( res.next() ) {
        	getAttributes.add(res.getString(1)); 
       }
        stmt.close();
		
		return getAttributes;
	}
	
}
