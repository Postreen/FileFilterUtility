package com.sazonov.utility;

import com.sazonov.utility.service.FileProcessingService;
import com.sazonov.utility.service.ServiceFactory;

public class Main {
    public static void main(String[] args) {
        FileProcessingService fileProcessingService = ServiceFactory.createFileProcessingService(args);

        if (fileProcessingService != null) {
            fileProcessingService.process(args);
        } else {
            System.out.println("Failed to create FileProcessingService.");
        }
    }
}