package com.sazonov.utility.manager;

import com.sazonov.utility.config.Configuration;
import com.sazonov.utility.io.writer.OutputWriter;
import com.sazonov.utility.io.writer.OutputWriterFactory;

public class FileOutputManagerFactory implements OutputWriterFactory {
    @Override
    public OutputWriter create(Configuration configuration) {
        return new FileOutputManager(configuration);
    }
}
