package com.andersen.test_ufm.service;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

//TODO Покрыть тестами и проверить ф-ции
@Slf4j
@Service
@Scope("prototype")
public class MultithreadSpentCalc implements IProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultithreadSpentCalc.class);

    @Value("${application.config.numthreads:20}")
    private Integer numthreads;

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        this.executorService = Executors.newFixedThreadPool(numthreads);
    }

    @Override
    public Long process(List<JSONObject> listSpent) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 1000000);

        LOGGER.info("START PROCESS IN SERVICE. Random ID = " + this.hashCode());
        Integer shardSize = listSpent.size() / 20;
        List<Callable<Long>> tasks = new ArrayList<>();
        Long retVal = new Long(0l);

        for(int i = 0; i < listSpent.size(); i += shardSize) {
            tasks.add(funcTaskWrapper(listSpent.subList(i, i + shardSize))); // [start, end)
        }

        try {
            for(Future<Long> result : executorService.invokeAll(tasks)) {
                retVal += result.get();
            }
        } catch (InterruptedException e) {
            //log.error(e.getLocalizedMessage() + " " + e.toString());
        } catch (ExecutionException e) {
            //log.error(e.getLocalizedMessage() + " " + e.toString());
        }

        LOGGER.info("END PROCESS IN SERVICE");
        return retVal;
    }

    private Callable<Long> funcTaskWrapper(List listSpent) {
        return () -> {
            Long retVal = listSpent
                    .stream()
                    .mapToLong(elm -> (Long) ((JSONObject)elm).get("spent"))
                    .reduce((s1, s2) -> s1 + s2).orElse(0l);
            return retVal;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultithreadSpentCalc)) return false;
        MultithreadSpentCalc that = (MultithreadSpentCalc) o;
        return Objects.equals(numthreads, that.numthreads) &&
                Objects.equals(executorService, that.executorService);
    }

    @Override
    public int hashCode() {

        return Objects.hash(numthreads, executorService);
    }
}
