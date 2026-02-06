package com.sazonov.utility.io.writer;

import com.sazonov.utility.config.Configuration;

public interface OutputWriterFactory {
    OutputWriter create(Configuration configuration);
}
