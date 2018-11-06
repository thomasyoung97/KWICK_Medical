import java.sql.*;


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

            connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/40201953/IdeaProjects/Kwick_Medical/PDB.accdb");
            System.out.println("Patient Database Connection Confimed");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("error connecting to DB");
        }

    }


    //-QUERY DB
    public String[] queryDb(String Query)
    {
        String[] temp;
        temp = new String[3];
        try {

            statement = connection.createStatement();
            resultSet = statement.executeQuery(Query);


            if (resultSet.next())
            {
                temp[0]= resultSet.getString(1);
                temp[1]= resultSet.getString(2);
                temp[2]= resultSet.getString(3);
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
        return temp;
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
