package com.andersen.test_ufm.akka.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.andersen.test_ufm.service.IProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Класс-актор для обработки файла. Для каждого файла будет создаваться свой класс-актор
 */
//TODO Cleanup the code of commented parts
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FilePerformActor extends AbstractActor {

    @Autowired
    private IProcessService processService;

    @Autowired
    private ActorUtil actorUtil;

    //private JSONObject outputData;

    public FilePerformActor() {
    }

    public FilePerformActor(IProcessService processService) {
        this.processService = processService;
    }

    public static Props props(IProcessService processService) {
        return Props.create(FilePerformActor.class, processService);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("Custom actor started");
    }

    @Override
    public void postStop() throws Exception {
        log.info("Custom actor stopped");
        super.postStop();
    }

    /*
     .match(Integer.class, i -> {
        getSender().tell(i + magicNumber, getSelf());
      })
     */

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(File.class, this::process).build();

        //return receiveBuilder().match(File.class, file -> {
        //    new ActorUtil().process(file);
        //}).build();

        //return receiveBuilder().match(File.class, file -> {
        //    ActorUtil actorUtil = new ActorUtil();
        //    actorUtil.process(file);
        //}).build();
    }

    private void process(File inputFile) {
        actorUtil.process(inputFile);
    }

/*
    private void process(File inputFile) {
        LOGGER.info("Start processing file ...");
        JSONObject inputData = parseFileToJSON(inputFile);
        if(inputData != null){
            outputData = processService.process(inputData);
            LOGGER.info("Result processing file: " + outputData.toString());
        }
        File dest = new File("c:/Users/anduser/Other/processed/"+inputFile.getName());
        fileUtil.copyFile(inputFile, dest);
        fileUtil.deleteFile(inputFile);
        fileUtil.createFile(inputFile.getName(),outputData);
    }
*/

/*
    private JSONObject parseFileToJSON(File inputFile) {
        String content = null;
        JSONObject json = null;

        try {
            content = new String(Files.readAllBytes(inputFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        try {
            json = (JSONObject) parser.parse(content);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return json;
    }
*/
}
