package com.sazonov.utility.commandline;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class HelpPrinter {
    public void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <jar> [options] <files...>", options);
    }
}
