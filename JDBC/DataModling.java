
package datamodling;
import java.sql.*;
import oracle.jdbc.driver.OracleDriver;
public class DataModling {

    
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (ClassNotFoundException e){
            System.out.println("Could not load driver.");
        }
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery("select cno, cname, city from cust");
        while( res.next() ) {
            String cno = res.getString(1);
            String cname = res.getString(2);
            String city = res.getString(3);
            System.out.println( cno + " " + cname + " " + city  );
        }
        stmt.close();
    }
    
}
