package org.example.data;

import org.example.domain.DataRepository;
import org.example.domain.MetaDataModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

public class DataRepositoryImpl implements DataRepository {
    
    private final Database db;
    private final FileCopying fileCopy;
    
    public DataRepositoryImpl(Database db, FileCopying fileCopy) {
        this.db = db;
        this.fileCopy = fileCopy;
        db.createTableInDatabase(db);
    }
    
    @Override
    public void createTransaction(String sourcePath, String targetPath) throws IOException {
        Connection connection = db.getConnection();
        insertListOfTextFileInDatabase(connection, sourcePath, Paths.get(targetPath));
        db.closeConnection(connection);
    }
    
    @Override
    public List<MetaDataModel> getAllTransactions() {
        return db.getAllRowsOfDatabase();
    }
    
    
    /*
    this function iterates over all files in the given dirPath
    and inserts all the required text files transactions
     */
    private void insertListOfTextFileInDatabase(Connection connection, String sourcePath, Path targetFilePath) throws IOException {
        
        try (Stream<Path> stream = Files.list(Paths.get(sourcePath))) {
            stream.forEach(path -> {
                // if the path is a directory just calling the function again to add all the txtFiles
                if (Files.isDirectory(path)) {
                    try {
                        insertListOfTextFileInDatabase(connection, path.toAbsolutePath().toString(), targetFilePath.resolve(path.getFileName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (path.toFile().getName().endsWith(".txt")) {
                    if (db.insertRowIntoTable(connection, getMetaDataModelFromFile(path))) {
//                        System.out.println("Hi prafull insert files");
                        moveFromSrcToTarget(path, targetFilePath.resolve(path.toFile().getName()));
                    }
                }
            });
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     this function will move the .txt file from srcPath to targetPath
     */
    private void moveFromSrcToTarget(Path sourceFilePath, Path targetFilePath) {
        fileCopy.createTextFile(sourceFilePath, targetFilePath);
    }
    
    
    /*
     this function deals with defining the TextField of the table
     */
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
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
            throw new RuntimeException(ex);
        }
    }
    
}