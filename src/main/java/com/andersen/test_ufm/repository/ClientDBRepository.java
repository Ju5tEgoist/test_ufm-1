package com.andersen.test_ufm.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.UnknownHostException;
import java.util.List;

@Repository
public class ClientDBRepository {

    private final DBCollection mongoCollection;

    @Autowired
    public ClientDBRepository(@Value("${mongodb.database.name:test}") String mongodbDatabaseName,
                              @Value("${mongodb.collection.name:clients}") String mongodbCollectionName,
                              @Value("${mongodb.host:localhost}") String localhost,
                              @Value("${mongodb.port:27017}") int port) throws UnknownHostException {
        this(new MongoClient(localhost, port).getDB(mongodbDatabaseName)
                .getCollection(mongodbCollectionName));
    }

    public ClientDBRepository(DBCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    public List getAllClients() {
        return mongoCollection.find().toArray();
    }

    public DBObject createClient(String clientId, String spentTotal, Boolean isBig) {
        DBObject newUser = new BasicDBObject();
        newUser.put("clientId", clientId);
        newUser.put("spentTotal", spentTotal);
        newUser.put("isBig", isBig);
        mongoCollection.save(newUser);
        return mongoCollection.findOne(newUser);
    }

    public DBObject createClient(JSONObject data) {
        mongoCollection.save((DBObject) data);
        return mongoCollection.findOne(data);
    }

    public DBObject findClientByClientId(String clientId) {
        return mongoCollection.findOne(new BasicDBObject("clientId", clientId));
    }
}
