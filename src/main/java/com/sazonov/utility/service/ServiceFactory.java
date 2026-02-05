package com.sazonov.utility.service;

import com.sazonov.utility.commandline.ArgumentParser;
import com.sazonov.utility.commandline.CliOptionsFactory;
import com.sazonov.utility.commandline.CliParserImpl;
import org.apache.commons.cli.Options;

public class ServiceFactory {
    public static FileProcessingService createFileProcessingService() {
        Options options = CliOptionsFactory.createOptions();
        ArgumentParser argumentParser = new CliParserImpl(options);

        return new FileProcessingService(argumentParser);
    }
}
