package com.sazonov.utility;

import com.sazonov.utility.service.FileProcessingCoordinatorService;

public class Main {
    public static void main(String[] args) {
        FileProcessingCoordinatorService fileProcessingCoordinatorService = new FileProcessingCoordinatorService();
        fileProcessingCoordinatorService.process(args);
    }
}