package com.sazonov.utility.service.io.reader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileReaderService {
    void readLines(List<Path> inputFiles) throws IOException;
}
