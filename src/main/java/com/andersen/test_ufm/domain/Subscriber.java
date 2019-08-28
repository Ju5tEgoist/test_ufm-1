package com.andersen.test_ufm.domain;

import lombok.Data;
import org.json.simple.JSONObject;

@Data
public class Subscriber {
    private Long id;
    private Long spent;

    public Subscriber(JSONObject inputData) {
        this.setId((Long) inputData.get("id"));
        this.setSpent((Long) inputData.get("spent"));
    }
}
