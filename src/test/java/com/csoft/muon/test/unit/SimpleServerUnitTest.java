package com.csoft.muon.test.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.csoft.muon.SimpleServer;
import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.Repository;

public class SimpleServerUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServerUnitTest.class);

    private Repository mockRepository;
    private SimpleServer server;
    
    private Item testItem0 = new Item(0, "item0");
    private Item testItem1 = new Item(1, "item1");
    private Item testItem2 = new Item(2, "item2");
    
    @BeforeMethod
    public void setup() {
        mockRepository = mock(Repository.class);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testGetIndexWithEmpty() {
        when(mockRepository.get(0)).thenThrow(IndexOutOfBoundsException.class);
        mockRepository.get(0);
    }
    
    @Test
    public void testGetIndexWithOrderedNonEmpty() {
        when(mockRepository.get(0)).thenReturn(testItem0);
        when(mockRepository.get(1)).thenReturn(testItem1);
        when(mockRepository.get(2)).thenReturn(testItem2);
        
        server = new SimpleServer(mockRepository);
        //TODO unit test actions on SimpleServer not on Repository
        
        mockRepository.get(0);
        mockRepository.get(1);
        mockRepository.get(2);
        verify(mockRepository).get(0);
        verify(mockRepository).get(1);
        verify(mockRepository).get(2);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        when(mockRepository.get(0)).thenReturn(testItem0);
        when(mockRepository.get(1)).thenThrow(IndexOutOfBoundsException.class);
        mockRepository.append(testItem0);
        mockRepository.get(1);
        LOGGER.info("here");
        verify(mockRepository.get(1)); //how to verify?
    }
}
