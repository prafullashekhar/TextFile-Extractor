package org.example.domain;

import java.io.IOException;
import java.util.List;

public interface DataRepository {
    
    /*
    This function is used for
    inserting the metadata of all the text file of the given source dir path
    and will move the text file to the targetPath if it gets inserted in database.
   */
    void createTransaction(String sourcePath, String targetPath) throws IOException;
    
    
    /*
    This function is used for getting the list of all the transactions till now
     */
    List<MetaDataModel> getAllTransactions();
    
}
