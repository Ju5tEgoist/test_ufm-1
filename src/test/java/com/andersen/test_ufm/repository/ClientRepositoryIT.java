package com.andersen.test_ufm.repository;

import com.andersen.test_ufm.Application;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.UnknownHostException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by abraxas on 10.01.2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = AnnotationConfigContextLoader.class)
public class ClientRepositoryIT {
    private static final String DB_NAME = "int_test";
    private static final String COLLECTION_NAME = "int_test_coll";
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 27017;
    private static final String SPENT_TOTAL = "amount";
    private static final Boolean BIG = true;


    private static DBCollection mongoCollection;
    private ClientDBRepository clientDBRepository;

    public ClientRepositoryIT() throws UnknownHostException {
    }

    @BeforeClass
    public static void initDb() throws UnknownHostException {
        mongoCollection = new MongoClient(HOSTNAME, PORT).getDB(DB_NAME).getCollection(COLLECTION_NAME);

    }

    @Before
    public void init() throws UnknownHostException {
        clientDBRepository = new ClientDBRepository(mongoCollection);
    }

    @After
    public void tearDown() {
        mongoCollection.drop();
    }

    @Test
    public void shouldCreateClient(){
        String clientId = "shouldCreateClientTest";
        DBObject test = clientDBRepository.createClient(clientId, SPENT_TOTAL, BIG);
        DBObject client = mongoCollection.findOne(test);
        assertEquals(client, test);
    }

    @Test
    public void shouldFindClientById(){
        String temporaryId = "shouldFindClientByIdTest";
        DBObject repeatedObject = mongoCollection.findOne(new BasicDBObject("clientId", temporaryId));
        if(repeatedObject != null){
            mongoCollection.remove(repeatedObject);
        }
        clientDBRepository.createClient(temporaryId, SPENT_TOTAL, BIG);
        DBObject client = mongoCollection.findOne(new BasicDBObject("clientId", temporaryId));
        DBObject test = clientDBRepository.findClientByClientId(temporaryId);
        assertEquals(test, client);
    }

    @Test
    public void shouldGetAllClients(){
        List test = mongoCollection.find().toArray();
        List clients = clientDBRepository.getAllClients();
        assertEquals(test, clients);
    }

}
