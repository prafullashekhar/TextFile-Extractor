package org.example.data;

import org.example.common.Constants;
import org.example.domain.MetaDataModel;
import java.sql.*;

public class Database {
    private final String dbName = "project1";
    private final String userName = "postgres";
    private final String password = "14563";

    // getConnection function will provide the connection object for the required database
    // and it will set the connection to stop auto commit.
    public Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/"+dbName, userName, password );
            connection.setAutoCommit(false);
        }catch (Exception e){
            System.out.println("Error occurred in creating Connection");
            throw new RuntimeException(e);
        }
        return connection;
    }

    // function closes the connection after commit
    public void closeConnection(Connection connection){
        try {
            connection.commit();
            connection.close();
        } catch (Exception e) {
            System.out.println("Error occurred in closing connection");
        }
    }

    public void createTableInDatabase(Database db){
        try {
            Connection connection = db.getConnection();
            Statement statement = connection.createStatement();

            //sql command for creating the new table if not created already
            String sqlCommand = String.format(
                    "CREATE TABLE IF NOT EXISTS %s ( " +
                            "%s TEXT PRIMARY KEY, " +
                            "%s TEXT NOT NULL, " +
                            "%s TEXT NOT NULL, " +
                            "%s TIMESTAMP NOT NULL, " +
                            "%s TEXT NOT NULL )",
                    Constants.TABLE_NAME, Constants.ID, Constants.FILE_NAME, Constants.DIR_PATH,
                    Constants.LAST_MODIFIED_TIME,  Constants.STATUS
            );

            statement.executeUpdate(sqlCommand);
            statement.close();
            db.closeConnection(connection);

        } catch (SQLException e) {
            System.out.println("Error occurred");
            throw new RuntimeException(e);
        }
    }

    // the insertFile function is used to insert the detail of any text file in the database
    public boolean insertRowIntoTable(Connection connection, MetaDataModel metaDataModel){
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
                try{
                    // here the order of column in sql table is (id, fileName, dirPath, lastModifiedTime, status)
                    String sqlQuery = String.format(
                            "INSERT INTO %s VALUES(?,?,?,?,?) ON CONFLICT(%s) DO NOTHING;", Constants.TABLE_NAME, Constants.ID
                    );
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                    preparedStatement.setString(1, metaDataModel.getId());
                    preparedStatement.setString(2, metaDataModel.getFileName());
                    preparedStatement.setString(3, metaDataModel.getDirPath());
                    preparedStatement.setTimestamp(4, metaDataModel.getLastEditedTime());
                    preparedStatement.setString(5, metaDataModel.getStatus());

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    return true;
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    return false;
                }
//            }
//        });
//        t.start();
    }

}
