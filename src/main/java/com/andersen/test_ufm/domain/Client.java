package com.andersen.test_ufm.domain;

import lombok.Data;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Data
public class Client {
    private Long clientId;
    private List<Subscriber> subscribers;
    private Long spentTotal;
    private Boolean isBig;

    public Client(JSONObject inputData) {
        this.setClientId((Long) inputData.get("clientId"));
        this.setSubscribers(new ArrayList<>());
        for (JSONObject subscriber : (List<JSONObject>) inputData.get("subscribers")) {
            this.getSubscribers().add(new Subscriber(subscriber));
        }
    }

    public JSONObject toJSONObject() {
        JSONObject retVal = new JSONObject();
        retVal.put("clientId", clientId);
        retVal.put("spentTotal", spentTotal);
        retVal.put("isBig", isBig);
        return retVal;
    }
}
