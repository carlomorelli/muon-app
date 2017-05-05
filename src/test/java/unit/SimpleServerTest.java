package unit;

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
import com.csoft.muon.repository.RepositoryException;

public class SimpleServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServerTest.class);

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
    public void testGetIndexWithEmpty() throws RepositoryException {
        when(mockRepository.fetchItemAtIndex(0)).thenThrow(IndexOutOfBoundsException.class);
        mockRepository.fetchItemAtIndex(0); //TODO change with SimpleServer action
    }
    
    @Test
    public void testGetIndexWithOrderedNonEmpty() throws RepositoryException {
        when(mockRepository.fetchItemAtIndex(0)).thenReturn(testItem0);
        when(mockRepository.fetchItemAtIndex(1)).thenReturn(testItem1);
        when(mockRepository.fetchItemAtIndex(2)).thenReturn(testItem2);
        
         //TODO unit test following actions on SimpleServer not on Repository
        //server = new SimpleServer(mockRepository);
        mockRepository.fetchItemAtIndex(0); 
        mockRepository.fetchItemAtIndex(1);
        mockRepository.fetchItemAtIndex(2);
        verify(mockRepository).fetchItemAtIndex(0);
        verify(mockRepository).fetchItemAtIndex(1);
        verify(mockRepository).fetchItemAtIndex(2);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() throws RepositoryException {
        when(mockRepository.fetchItemAtIndex(0)).thenReturn(testItem0);
        when(mockRepository.fetchItemAtIndex(1)).thenThrow(IndexOutOfBoundsException.class);
        mockRepository.insertItem(testItem0);
        mockRepository.fetchItemAtIndex(1);
        LOGGER.info("here");
        verify(mockRepository.fetchItemAtIndex(1)); //how to verify?
    }
}
