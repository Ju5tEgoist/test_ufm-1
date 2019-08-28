package com.andersen.test_ufm.akka.extention;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

/**
 * Класс, который позволяет создавать инстансы акторов
 */
public class FileActorProducer implements IndirectActorProducer {
    private final ApplicationContext applicationContext;
    private final String actorBeanName;

    public FileActorProducer(ApplicationContext applicationContext, String actorBeanName) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
    }

    public Actor produce() {
        return (Actor) applicationContext.getBean(actorBeanName);
    }

    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
    }
}
