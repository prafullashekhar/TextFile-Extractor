package org.example.data;

import org.example.domain.DataRepository;
import org.example.domain.MetaDataModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class DataRepositoryImpl implements DataRepository {
    
    private final Database db;
    private final ExecutorService executorService;
    
    public DataRepositoryImpl(ExecutorService executorService) {
        this.db = new Database();
        this.executorService = executorService;
        db.createTableInDatabase(db);
    }
    
    @Override
    public void createTransaction(String sourcePath, String targetPath) throws IOException {
        insertListOfTextFileInDatabase(Paths.get(sourcePath), Paths.get(targetPath));
    }
    
    @Override
    public List<MetaDataModel> getAllTransactions() {
        return db.getAllRowsOfDatabase();
    }
    
    
    /*
    this function iterates over all files in the given dirPath
    and inserts all the required text files transactions
     */
    private void insertListOfTextFileInDatabase(Path sourceFilePath, Path targetFilePath) throws IOException {
        
        try (Stream<Path> stream = Files.list(sourceFilePath)) {
            stream.forEach(path -> {
                // if the path is a directory just calling the function again to add all the txtFiles
                if (Files.isDirectory(path)) {
                    try {
                        insertListOfTextFileInDatabase(path.toAbsolutePath(), targetFilePath.resolve(path.getFileName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (path.toFile().getName().endsWith(".txt")) {
                    executorService.execute(new TransactionTaskThread(path, targetFilePath.resolve(path.toFile().getName())));
                }
            });
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}