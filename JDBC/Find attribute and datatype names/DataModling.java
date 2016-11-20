  2 import java.sql.*;
  3 import oracle.jdbc.driver.OracleDriver;
  4 public class DataModling {
  5
  6
  7     public static void main(String[] args) throws SQLException {
  8         try {
  9             Class.forName("oracle.jdbc.driver.OracleDriver");
 10
 11         }
 12         catch (ClassNotFoundException e){
 13             System.out.println("Could not load driver.");
 14         }
 15         Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
 16         Statement stmt = conn.createStatement();
 17         ResultSet res = stmt.executeQuery("Select COLUMN_NAME, data_type from user_tab_columns where TABLE_NAME = 'S'");
 18         while( res.next() ) {
 19            	String attribute1 = res.getString(1);
 20           	String attribute2 = res.getString(2);
 21             System.out.println( attribute1 + " " +  attribute2 );
 22         }
 23         stmt.close();
 24     }
 25
 26 }
