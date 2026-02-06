package com.sazonov.utility.io.writer;

public interface OutputWriter {
    boolean writeInteger(String value);

    boolean writeFloat(String value);

    boolean writeString(String value);

    void closeAll();
}
