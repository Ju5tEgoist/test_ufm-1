package com.andersen.test_ufm.akka.actors;

import com.andersen.test_ufm.repository.ClientDBRepository;
import com.andersen.test_ufm.repository.FileRepository;
import com.andersen.test_ufm.service.IProcessService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Component
public class ActorUtil {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ClientDBRepository clientDBRepository;

    @Autowired
    private IProcessService processService;

    @Value("${application.processedFilesDir:processed}")
    private String processedFilesDir;

    public void process(File inputFile) {
        log.debug("Start processing file ...");
        JSONObject inputData = parseFileToJSON(inputFile);
        JSONObject outputData = new JSONObject();
        if (inputData != null){
            outputData = processService.process(inputData);
            log.debug("Result processing file: " + outputData.toString());
        }
        File dest = new File(processedFilesDir + "/" + inputFile.getName());
        fileRepository.copyFile(inputFile, dest);
        fileRepository.deleteFile(inputFile);
        fileRepository.createOutputFile(inputFile.getName(), outputData);

        clientDBRepository.createClient(outputData);
    }


    public JSONObject parseFileToJSON(File inputFile) {
        String content = null;
        JSONObject json = null;

        try {
            content = new String(Files.readAllBytes(inputFile.toPath()));
        } catch (IOException e) {
            log.error(e.toString());
        }

        JSONParser parser = new JSONParser();
        try {
            json = (JSONObject) parser.parse(content);
        } catch (ParseException e) {
            log.error(e.toString());
        }

        return json;
    }
}
