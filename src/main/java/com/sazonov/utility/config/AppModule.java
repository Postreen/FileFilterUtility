package com.sazonov.utility.config;

import com.sazonov.utility.commandline.ArgumentParser;
import com.sazonov.utility.commandline.CliOptionsFactory;
import com.sazonov.utility.commandline.CliParserImpl;
import com.sazonov.utility.io.reader.DefaultFileReaderService;
import com.sazonov.utility.io.FileReaderService;
import com.sazonov.utility.io.writer.OutputWriterFactory;
import com.sazonov.utility.manager.FileOutputManagerFactory;
import com.sazonov.utility.service.FileProcessingService;
import com.sazonov.utility.stats.DefaultStatsPresenterFactory;
import com.sazonov.utility.stats.StatsPresenterFactory;
import org.apache.commons.cli.Options;

public class AppModule {
    private final ArgumentParser argumentParser;
    private final FileReaderService fileReaderService;
    private final OutputWriterFactory outputWriterFactory;
    private final StatsPresenterFactory statsPresenterFactory;

    public AppModule() {
        Options options = CliOptionsFactory.createOptions();
        this.argumentParser = new CliParserImpl(options);
        this.fileReaderService = new DefaultFileReaderService();
        this.outputWriterFactory = new FileOutputManagerFactory();
        this.statsPresenterFactory = new DefaultStatsPresenterFactory();
    }

    public FileProcessingService fileProcessingService() {
        return new FileProcessingService(
                argumentParser,
                fileReaderService,
                outputWriterFactory,
                statsPresenterFactory
        );
    }
}