package com.sazonov.utility.commandline;

import com.sazonov.utility.config.Configuration;

import java.util.Optional;

public interface ArgumentParser {
    Optional<Configuration> parse(String[] args);
    void printHelp();
}
