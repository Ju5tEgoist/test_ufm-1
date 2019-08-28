package com.andersen.test_ufm.repository;

import com.andersen.test_ufm.Application;
import com.mongodb.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.UnknownHostException;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class ClientDBRepositoryTest {

    public static final String CLIENT_ID = "test_id";
    public static final String SPENT_TOTAL = "much";
    public static final Boolean IS_BIG = true;

    private static String mongodbDatabaseName = "test";
    private static String mongodbCollectionName = "clients";

    private static MongoClient mongo;
    private static DB mongoDB;
    private static DBCollection mongoCollection = null;

    @Autowired
    private ClientDBRepository clientDBRepository;


    @BeforeClass
    public static void initialize() {
        try {
            mongo = new MongoClient("localhost", 27017);
            mongoDB = mongo.getDB(mongodbDatabaseName);
            mongoCollection = mongoDB.getCollection(mongodbCollectionName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        DBObject newUser = new BasicDBObject();
        newUser.put("clientId", CLIENT_ID);
        newUser.put("spentTotal", SPENT_TOTAL);
        newUser.put("isBig", IS_BIG);
        mongoCollection.save(newUser);
    }


    @Test
    public void testCreateClient() throws Exception {
        Assert.assertNotNull(clientDBRepository.createClient(CLIENT_ID, SPENT_TOTAL, IS_BIG));
    }

    @Test
    public void testFindClientByClientId() throws Exception {
        Assert.assertNotNull(clientDBRepository.findClientByClientId(CLIENT_ID));
    }

    @Test
    public void testGetAllClients() throws Exception {
        Assert.assertTrue(clientDBRepository.getAllClients().size() > 0);
    }


    @AfterClass
    public static void close() {
        mongoCollection.remove(new BasicDBObject().append("clientId", CLIENT_ID));

        mongo.close();
    }
}
