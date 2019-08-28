package com.andersen.test_ufm.akka.scheduler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import com.andersen.test_ufm.akka.extention.FileActorExtension;
import com.andersen.test_ufm.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

/**
 * Класс-шедулер для обработки файлов через акторы
 */
@Slf4j
@Component
public class Scheduler {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private FileRepository dataProvider;

    private ActorRef fileActor;

    private int countActors;

    @Scheduled(fixedDelayString = "${application.scheduler.interval:1000}")
    public void performRegularAction() {
        List<File> fileList = dataProvider.getListInputFiles();
        log.info("Count files in input folder: " + fileList.size());

        for (File item : fileList) {
            fileActor.tell(item, ActorRef.noSender());
        }
    }

    @PostConstruct
    public void postConstructMethod() {
        log.debug("Scheduler post construct");
        ActorSystem system = context.getBean(ActorSystem.class);
        FileActorExtension fileActorExtension = context.getBean(FileActorExtension.class);

        countActors = dataProvider.getListInputFiles().size();
        log.debug("Count actors: " + countActors);

        fileActor = system.actorOf(fileActorExtension
                        .props("filePerformActor")
                        .withRouter(new RoundRobinPool(countActors)));
    }
}
