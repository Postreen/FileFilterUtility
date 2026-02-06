package com.sazonov.utility.processor;

import com.sazonov.utility.io.FileReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public final class FileFilterProcessor {
    private final FileReaderService fileReaderService;
    private final LineHandler lineHandler;

    public void process(List<Path> inputFiles) {
        fileReaderService.readLines(inputFiles, lineHandler::handle);
    }
}