package com.sazonov.utility.utils;

import com.sazonov.utility.commandline.CliOptionsFactory;
import lombok.experimental.UtilityClass;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

@UtilityClass
public class HelpPrinter {

    public void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <jar> [options] <files...>", options);
    }
}
