
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
        ResultSet res = stmt.executeQuery("Select data_type from user_tab_columns where TABLE_NAME = 'S'");
        while( res.next() ) {
            String faculty_number = res.getString(1);
            System.out.println( faculty_number );
        }
        stmt.close();
    }
    
}
