package com.andersen.test_ufm.repository;

import com.mongodb.*;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by abraxas on 11.01.2018.
 */
public class RepositoryTest {
    private DBCollection dbCollection = Mockito.mock(DBCollection.class);
    private ClientDBRepository repository = new ClientDBRepository(dbCollection);

    @Test
    public void shouldGetAllClients() {
        DBCursor cursor = Mockito.mock(DBCursor.class);

        when(dbCollection.find()).thenReturn(cursor);
        when(cursor.toArray()).thenReturn(Arrays.asList(new BasicDBObject()));

        List clients = repository.getAllClients();

        verify(dbCollection, Mockito.only()).find();
        verify(cursor, Mockito.only()).toArray();

        assertNotNull(clients);
        assertFalse(clients.isEmpty());
        assertTrue(clients.size() == 1);
    }

    @Test
    public void shouldCreateClient(){
        WriteResult result = Mockito.mock(WriteResult.class);

        when(dbCollection.save(Mockito.any(DBObject.class))).thenReturn(result);
        when(dbCollection.findOne(Mockito.any(DBObject.class))).thenReturn(new BasicDBObject());

        DBObject test = repository.createClient("shouldCreateClientId", "spent", true);

        verify(dbCollection).save(Mockito.any());
        verify(dbCollection).findOne(Mockito.any());

        assertNotNull(test);
    }

    @Test
    public void shouldFindClientById(){
        DBObject object = Mockito.mock(DBObject.class);

        when(dbCollection.findOne(Mockito.any(DBObject.class))).thenReturn(object);

        DBObject client = repository.findClientByClientId("shouldFindClientById");

        verify(dbCollection).findOne(Mockito.any());

        assertNotNull(client);

    }
}
