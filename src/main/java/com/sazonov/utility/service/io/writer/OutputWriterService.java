package com.sazonov.utility.service.io.writer;

public interface OutputWriterService {
    boolean writeInteger(String value);

    boolean writeFloat(String value);

    boolean writeString(String value);

    void closeAll();
}
