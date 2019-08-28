package com.andersen.test_ufm.service;

import com.andersen.test_ufm.domain.Client;
import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class MainProcessService implements IProcessService {

    @Override
    public JSONObject process(JSONObject inputData) {

        JSONObject outputData = new JSONObject();

        PackageBuilder packageBuilder = new PackageBuilder();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("rules.drl");
        Reader reader = new InputStreamReader(resourceAsStream);
        try {
            packageBuilder.addPackageFromDrl(reader);
            org.drools.core.rule.Package rulesPackage = packageBuilder.getPackage();
            RuleBase ruleBase = RuleBaseFactory.newRuleBase();
            ruleBase.addPackage(rulesPackage);

            WorkingMemory workingMemory = ruleBase.newStatefulSession();

            Client client = new Client(inputData);

            workingMemory.insert(client);
            workingMemory.fireAllRules();

            outputData = client.toJSONObject();
        } catch (DroolsParserException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error(e.toString());
        }

        return outputData;
    }

}
