import java.sql.*;
import java.util.ArrayList;
import oracle.jdbc.driver.OracleDriver;


/**
 * Created by logan on 11/28/2016.
 */
public class Star
{
    public static ArrayList<String> getAttributes(ArrayList<String> table) throws SQLException
    {
        ArrayList<String> atts = new ArrayList<>();
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e){
            System.out.println("Could not load driver.");
        }
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "team3", "unfpdm");
        Statement stmt = conn.createStatement();
        for (int i = 0; i < table.size(); i++)
        {
            ResultSet res = stmt.executeQuery("Select COLUMN_NAME, data_type from user_tab_columns where TABLE_NAME = '"+ table.get(i) +"'");
            while( res.next() )
                atts.add(res.getString(1));
        }
        stmt.close();

        return atts;
    }

    public static void main(String args[]) throws SQLException
    {
        ArrayList<String> in = new ArrayList<String>();
        in.add("P");
        in.add("S");
        ArrayList<String> qe = getAttributes(in);
        for (int i = 0; i < qe.size(); i++)
            System.out.println(qe.get(i));
    }
}
