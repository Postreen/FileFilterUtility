package com.sazonov.utility.service;

import com.sazonov.utility.config.AppModule;

public class ServiceFactory {
    public static FileProcessingService createFileProcessingService() {
        AppModule module = new AppModule();
        return module.fileProcessingService();
    }
}
