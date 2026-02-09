package com.sazonov.utility;

import com.sazonov.utility.service.FileProcessingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("com.sazonov.utility");

        FileProcessingService fileProcessingService = context.getBean(FileProcessingService.class);

        fileProcessingService.process(args);
    }
}