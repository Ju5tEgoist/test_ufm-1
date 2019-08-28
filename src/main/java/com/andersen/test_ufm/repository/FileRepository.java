package com.andersen.test_ufm.repository;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс реализующией методы доступа к данным
 */
@Slf4j
@Repository
public class FileRepository {

    @Value("${application.inputFilesDir:input}")
    private String inputFilesDir;

    @Value("${application.processedFilesDir:processed}")
    private String processedFilesDir;

    @Value("${application.outputFilesDir:output}")
    private String outputFilesDir;

    private File fileInputFilesDir;
    private File fileProcessedFilesDir;
    private File fileOutputFilesDir;

    @PostConstruct
    public void init() {
        fileInputFilesDir = new File(inputFilesDir);
        fileProcessedFilesDir = new File(processedFilesDir);
        fileOutputFilesDir = new File(outputFilesDir);

        if (!fileInputFilesDir.exists()) {
            fileInputFilesDir.mkdir();
        }
        if (!fileProcessedFilesDir.exists()) {
            fileProcessedFilesDir.mkdir();
        }
        if (!fileOutputFilesDir.exists()) {
            fileOutputFilesDir.mkdir();
        }
    }

    public List<File> getListInputFiles(){
        File[] files = fileInputFilesDir.listFiles();
        List<File> list;
        if (files != null) {
            list = Arrays.asList(files);
        } else {
            list = new ArrayList<>();
        }

        return list;
    }

    public void copyFile(File source, File dest){
        try {
            if (!dest.exists()){
                Files.copy(source.toPath(), dest.toPath());
            } else {
                log.error("File " + source.getName() + " already exists. Copying file stopped.");
            }
        } catch (IOException e) {
            log.error("Error while coping the file: " + source.getName() + ". Error: " + e.getMessage());
        }
    }

    public void deleteFile(File file){
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            log.error("Error while deleting the file: " + file.getName() + ". Error: " + e.getMessage());
        }
    }

    public void createOutputFile(String fileName, JSONObject obj){
        try (FileWriter file = new FileWriter(outputFilesDir + "/" + fileName)) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            log.error("Error while creating file. Error: " + e.getMessage());
        }
    }
}
