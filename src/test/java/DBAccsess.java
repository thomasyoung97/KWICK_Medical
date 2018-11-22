import java.sql.*;
import java.util.ArrayList;


public class DBAccsess {

    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    //-Construct DB class and load drivers
    public DBAccsess()
    {

    }

    //--CONNECT TO DB
    public void dbConnect()
    {
        //open database connection
        try {

            connection = DriverManager.getConnection("jdbc:ucanaccess://PDB.accdb");
            System.out.println("Patient Database Connection Confimed");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("error connecting to DB");
        }

    }

    /**
     * For some reason ResultSet dose not have a method for get rows
     * and when you try and count them it moves the row pointer causing issues
     * so ive had to call a separate identical query and to get the size of the one in use.
     * not ideal but it works.
     */
    private int getResultSize(String Query)
    {
        Connection connection;
        ResultSet rs = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:ucanaccess://PDB.accdb");
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(Query);
        }
        catch (SQLException e)
        {

        }

        int count = 0;
        try
        {

            while (rs.next())
            {
                count++;
            }
        }
        catch(SQLException e)
        {

        }
      return count;
    }



    public void appendRecord(String[] patientRecord,String Existing_ap, String Callout_ap)
    {
        try
        {
           String sql = "UPDATE PDB SET ExistingConditions = ? , PreviousCallouts = ? WHERE NhsNumber = ?";
           PreparedStatement psmt = connection.prepareStatement(sql);

           if(patientRecord[6] == "None" || patientRecord == null)
           {
               psmt.setString(1,Existing_ap);
           }
           else
           {
               psmt.setString(1,patientRecord[5]+" / "+Existing_ap);
           }
            psmt.setString(2 , Callout_ap);
            psmt.setInt(3,Integer.valueOf(patientRecord[6]));
           int nrows = psmt.executeUpdate();
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }

    }



    //-QUERY DB
    public ArrayList<ArrayList<String>> queryDb(String Query)
    {

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery(Query);
            System.out.println(getResultSize(Query));

            if (resultSet.next())
            {

                for(int i = 0; i < getResultSize(Query); i++)
                {
                    ArrayList<String> inner = new ArrayList<String>();

                    inner.add(resultSet.getString(1));
                    inner.add(resultSet.getString(2));
                    inner.add(resultSet.getString(3));
                    inner.add(resultSet.getString(4));
                    inner.add(resultSet.getString(5));
                    inner.add(resultSet.getString(6));
                    inner.add(resultSet.getString(7));
                    list.add(inner);
                    resultSet.next();
                }


            }
            else
            {
                System.out.println("result set empty");
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.print("error executing SQL statement");
        }
        cleanup();
        return list;
    }



    // Cleanup Resources
    private void cleanup() {
        //Closing database connection
        try {
            if (null != connection) {

                // cleanup resources, once after processing
                resultSet.close();
                statement.close();

                // and then finally close connection
                connection.close();
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
}
