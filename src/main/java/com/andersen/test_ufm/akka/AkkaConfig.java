package com.andersen.test_ufm.akka;

import akka.actor.ActorSystem;
import com.andersen.test_ufm.akka.extention.FileActorExtension;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Служебный класс для загрузки конфигурации Akka и создания систмы акторов
 */
@Configuration
@Lazy
public class AkkaConfig {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    FileActorExtension fileActorExtension;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("TEST_UFM", akkaConfiguration());
        fileActorExtension.initialize(applicationContext);
        return system;
    }

    /**
     * Загрузка конфигурации из файла
     */
    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load();
    }
}
