package org.example.data;

import org.example.common.Constants;
import org.example.domain.MetaDataModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String dbName = "project1";
    private final String userName = "postgres";
    private final String password = "14563";
    
    /*
    getConnection function will provide the connection object for the required database
    It will set the connection to stop auto commit.
     */
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, userName, password);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            System.out.println("Error occurred in creating Connection");
            throw new RuntimeException(e);
        }
        return connection;
    }
    
    /*
     function closes the connection after commit
     */
    public void closeConnection(Connection connection) {
        try {
            connection.commit();
            connection.close();
        } catch (Exception e) {
            System.out.println("Error occurred in closing connection");
        }
    }
    
    
    /*
    This function is used for creating the Table in the database
    if there is no table in database with the required name
     */
    public void createTableInDatabase(Database db) {
        try {
            Connection connection = db.getConnection();
            Statement statement = connection.createStatement();
            
            //sql command for creating the new table if not created already
            String sqlCommand = String.format("CREATE TABLE IF NOT EXISTS %s ( " + "%s TEXT PRIMARY KEY, " + "%s TEXT NOT NULL, " + "%s TEXT NOT NULL, " + "%s TIMESTAMP NOT NULL, " + "%s TEXT NOT NULL )", Constants.TABLE_NAME, Constants.ID, Constants.FILE_NAME, Constants.DIR_PATH, Constants.LAST_MODIFIED_TIME, Constants.STATUS);
            
            statement.executeUpdate(sqlCommand);
            statement.close();
            db.closeConnection(connection);
            
        } catch (SQLException e) {
            System.out.println("Error occurred while creating Database");
            throw new RuntimeException(e);
        }
    }
    
    
    /*
    the insertFile function is used to insert the detail of any text file in the database
    will return true if row got inserted else will return false
    */
    public boolean insertRowIntoTable(Connection connection, MetaDataModel metaDataModel) {
        try {
            String sqlQueryToCheck = String.format("SELECT EXISTS(SELECT 1 FROM %s WHERE %s = '%s');", Constants.TABLE_NAME, Constants.ID, metaDataModel.getId());
            ResultSet rs = connection.createStatement().executeQuery(sqlQueryToCheck);
            rs.next();
            if (rs.getBoolean(1)) return false;
            
            // here the order of column in sql table is (id, fileName, dirPath, lastModifiedTime, status)
            String sqlQuery = String.format("INSERT INTO %s VALUES(?,?,?,?,?) ON CONFLICT(%s) DO NOTHING;", Constants.TABLE_NAME, Constants.ID);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, metaDataModel.getId());
            preparedStatement.setString(2, metaDataModel.getFileName());
            preparedStatement.setString(3, metaDataModel.getDirPath());
            preparedStatement.setTimestamp(4, metaDataModel.getLastEditedTime());
            preparedStatement.setString(5, metaDataModel.getStatus());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    
    /*
    this function returns the list of all the rows till now.
    */
    public List<MetaDataModel> getAllRowsOfDatabase() {
        ArrayList<MetaDataModel> rowList = new ArrayList<>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String sqlQuery = String.format("SELECT * FROM %s", Constants.TABLE_NAME);
            ResultSet rs = statement.executeQuery(sqlQuery);
            while (rs.next()) {
                MetaDataModel metaDataModel = new MetaDataModel(rs.getString(Constants.ID), rs.getString(Constants.FILE_NAME), rs.getString(Constants.DIR_PATH), rs.getTimestamp(Constants.LAST_MODIFIED_TIME), rs.getString(Constants.STATUS));
                rowList.add(metaDataModel);
            }
            
            statement.close();
            closeConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowList;
    }
    
}
