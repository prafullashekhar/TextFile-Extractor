package org.example.data;

import org.example.domain.DataRepository;
import org.example.domain.MetaDataModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;
import java.util.stream.Stream;

public class DataRepositoryImpl implements DataRepository {

    private final Database db;
    public DataRepositoryImpl(Database db){
        this.db = db;
        db.createTableInDatabase(db);
    }

    @Override
    public void createTransaction(String path) throws IOException {
        Connection connection = db.getConnection();
        insertListOfTextFileInDatabase(connection, path);
        db.closeConnection(connection);
    }

    // getListOfTextFile provides you the list of all the txt files in the given dirPath
    private void insertListOfTextFileInDatabase(Connection connection, String dirPath) throws IOException {

        try (Stream<Path> stream = Files.list(Paths.get(dirPath))){
            stream.forEach(path -> {
                // if the path is a directory just calling the function again to add all the txtFiles
                if(Files.isDirectory(path)){
                    try {
                        insertListOfTextFileInDatabase(connection, path.toAbsolutePath().toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else if(path.toFile().getName().endsWith(".txt")){
                    db.insertRowIntoTable(connection, getMetaDataModelFromFile(path));
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // this function will move the .txt file from srcPath to targetPath
//    private void moveFromSrcToTarget(Path srcPath, Path targetPath){
//    }


    // this function deals with defining the TextField of the table
    private MetaDataModel getMetaDataModelFromFile(Path file) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            return new MetaDataModel(
                    file.toString() + attributes.lastModifiedTime(),
                    file.toFile().getName(),
                    file.toFile().getParent(),
                    new Timestamp(attributes.lastModifiedTime().toMillis()),
                    "READ"
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}