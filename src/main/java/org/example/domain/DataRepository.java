package org.example.domain;

import java.io.IOException;

public interface DataRepository {
    void createTransaction(String path) throws IOException;
}
