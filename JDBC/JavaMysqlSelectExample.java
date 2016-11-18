      
      
      import java.sql.*;

      /**
       * A Java MySQL SELECT statement example.
       * Demonstrates the use of a SQL SELECT statement against a
       * MySQL database, called from a Java program.
       * 
       * Created by Alvin Alexander, http://devdaily.com
       */
      public class JavaMysqlSelectExample
      {

        public static void main(String[] args)
        {
          try
          {
            // create our mysql database connection
            String myDriver = "oracle.jdbc.driver.OracleDriver";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworc1", "hughesd@olympia", "n14425");
            
            // our SQL SELECT query. 
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT faculty_number, last_name, first_name, address, gender, birth_date, area_of_expertise, salary FROM Faculty";

            // create the java statement
            Statement st = conn.createStatement();
            
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
              String faculty_number = rs.getString("faculty_number");
              String last_name = rs.getString("last_name");
              String first_name = rs.getString("first_name");
              String address = rs.getString("address");
              String gender = rs.getString("gender");
              Date birth_date = rs.getDate("birth_date");
              String area_of_expertise = rs.getString("area_of_expertise");
              String salary = rs.getString("salary");
              
              // print the results
              System.out.format("%s, %s, %s, %s, %s, %s, %s, %s\n",faculty_number, last_name, first_name, address, gender, birth_date, area_of_expertise, salary);
            }
            st.close();
          }
          catch (Exception e)
          {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
          }
        }
      }