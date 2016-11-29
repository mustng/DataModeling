
import java.sql.*;
import oracle.jdbc.driver.OracleDriver;
public class DataModling {

    private string userName = "team3";
    private string passWord = "unfpdm";
    
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
        }
        catch (ClassNotFoundException e){
            System.out.println("Could not load driver.");
        }
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", userName, passWord);
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery("select * FROM c");
        while( res.next() ) {
            String faculty_number = res.getString(1);
            String last_name = res.getString(2);
            String first_name = res.getString(3);
            System.out.println( faculty_number + " " + last_name + " " + first_name );
        }
        stmt.close();
    }
    
    public void setUserName(String userName){
    	this.userName = userName;
    }
    
    public void setpassWord(String passWord){
    	this.passWord = passWord;
    }
    
}
