package com.andersen.test_ufm.service;

import com.andersen.test_ufm.Application;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class MainProcessServiceTest {

    @Autowired
    @Qualifier("mainProcessService")
    private IProcessService processService;

    @Test
    public void testProcess() throws Exception {
        JSONObject inputData = new JSONObject();
        inputData.put("clientId", 1l);
        JSONArray subscribers = new JSONArray();
        JSONObject sub1 = new JSONObject();
        sub1.put("id", 1l);
        sub1.put("spent", 100l);
        subscribers.add(sub1);
        JSONObject sub2 = new JSONObject();
        sub2.put("id", 2l);
        sub2.put("spent", 99l);
        subscribers.add(sub2);
        inputData.put("subscribers", subscribers);

        log.debug("========================================== INPUT: \n" + inputData.toJSONString());
        JSONObject res = processService.process(inputData);
        log.debug("========================================== RESULT: " + res.toJSONString());

        Assert.assertEquals(199l, res.get("spentTotal"));
    }
}