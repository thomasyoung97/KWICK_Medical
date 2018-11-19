import java.sql.*;
import java.util.ArrayList;


public class DBAccsess {

    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;


    //-Construct DB class and load drivers
    public static void DBAccsess() {

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


    //-QUERY DB
    public ArrayList<ArrayList<String>> queryDb(String Query)
    {

        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery(Query);


            if (resultSet.next())
            {
                for(int i = 0; i < 3; i++)
                {
                    ArrayList<String> inner = new ArrayList<String>();
                    inner.add(resultSet.getString(1));
                    inner.add(resultSet.getString(2));
                    inner.add(resultSet.getString(3));
                    inner.add(resultSet.getString(4));
                    inner.add(resultSet.getString(5));
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
