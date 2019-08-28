package com.andersen.test_ufm.akka.extention;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Кастомный класс-расширение для интеграции со Spring
 */
@Component
public class FileActorExtension implements Extension {
    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorBeanName) {
        return Props.create(FileActorProducer.class, applicationContext, actorBeanName);
    }
}
