package com.sazonov.utility;

import com.sazonov.utility.service.FileProcessingCoordinatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.sazonov.utility");

        FileProcessingCoordinatorService fileProcessingCoordinatorService = context.getBean(FileProcessingCoordinatorService.class);

        fileProcessingCoordinatorService.process(args);
    }
}