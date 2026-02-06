package com.sazonov.utility.io;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public interface FileReaderService {
    void readLines(List<Path> inputFiles, Consumer<String> lineConsumer);
}
