package unit;

import static com.csoft.muon.utils.RandomFunctions.randomItem;
import static com.csoft.muon.utils.JsonFunctions.dumpJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.csoft.muon.domain.Item;
import com.csoft.muon.handler.GetHandler;
import com.csoft.muon.handler.GetListHandler;
import com.csoft.muon.handler.HealthHandler;
import com.csoft.muon.handler.PostHandler;
import com.csoft.muon.handler.Result;
import com.csoft.muon.handler.VersionHandler;
import com.csoft.muon.repository.Repository;
import com.csoft.muon.repository.RepositoryException;

public class HandlerTest {

    private Repository repo;

    private Item testItem0 = randomItem(0);
    private Item testItem1 = randomItem(1);
    private Item testItem2 = randomItem(2);
    private Item testItem3 = randomItem(3);

    @BeforeMethod
    public void setup() {
        repo = mock(Repository.class);
    }

    @Test
    public void testGetHandler_cannotAcceptBody() throws RepositoryException, IOException {
        GetHandler handler = new GetHandler(repo);
        Result result = handler.process(dumpJson(testItem2), Collections.singletonMap(":index", "1"));
        assertThat(result.getStatus(), equalTo(400));
        verifyZeroInteractions(repo);
    }

    @Test
    public void testGetHandler_canFetch() throws RepositoryException {
        when(repo.fetchItemAtIndex(1)).thenReturn(testItem1);
        GetHandler handler = new GetHandler(repo);
        Result result = handler.process(null, Collections.singletonMap(":index", "1"));
        assertThat(result.getStatus(), equalTo(200));
        verify(repo).fetchItemAtIndex(1);
    }

    @Test
    public void testGetHandler_cannotFetchUnexistingItem() throws RepositoryException {
        when(repo.fetchItemAtIndex(1)).thenThrow(RepositoryException.class);
        GetHandler handler = new GetHandler(repo);
        Result result = handler.process(null, Collections.singletonMap(":index", "1"));
        assertThat(result.getStatus(), equalTo(404));
        verify(repo).fetchItemAtIndex(1);
    }

    @Test
    public void testGetListHandler_cannotAcceptBody() throws RepositoryException, IOException {
        GetHandler handler = new GetHandler(repo);
        Result result = handler.process(dumpJson(testItem1), Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(400));
        verifyZeroInteractions(repo);
    }

    @Test
    public void testGetListHandler_canFetchAll_withEmptyRepo() {
        when(repo.fetchAllItems()).thenReturn(Collections.emptyList());
        GetListHandler handler = new GetListHandler(repo);
        Result result = handler.process(null, Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(200));
        verify(repo).fetchAllItems();
    }

    @Test
    public void testGetListHandler_canFetchAll() {
        when(repo.fetchAllItems()).thenReturn(Arrays.asList(testItem1, testItem2, testItem3));
        GetListHandler handler = new GetListHandler(repo);
        Result result = handler.process(null, Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(200));
        // assertThat(result.getBody(), dumpJson(Arrays.asList(testItem1, testItem2,
        // testItem3)))
        verify(repo).fetchAllItems();
    }

    @Test
    public void testPostHandler_canAppend() throws RepositoryException, IOException {
        PostHandler handler = new PostHandler(repo);
        Result result = handler.process(dumpJson(testItem2), Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(200));
        verify(repo).insertItem(testItem2);
    }

    @Test
    public void testPostHandler_cannotAppend_withInvalidIndex() throws RepositoryException, IOException {
        doThrow(RepositoryException.class).when(repo).insertItem(testItem0);
        PostHandler handler = new PostHandler(repo);
        Result result = handler.process(dumpJson(testItem0), Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(403));
        verify(repo).insertItem(testItem0);
    }

    @Test
    public void testVersionHandler_canFetchVersion() throws IOException {
        VersionHandler handler = new VersionHandler();
        Result result = handler.process(null, Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(200));
        verifyZeroInteractions(repo); //version page does not interact with Repository
    }

    @Test
    public void testVersionHandler_connotAcceptBody() throws IOException {
        VersionHandler handler = new VersionHandler();
        Result result = handler.process(dumpJson(testItem1), Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(400));
        verifyZeroInteractions(repo);
    }

    @Test
    public void testHealthHandler_canFetchStatus() throws IOException {
        HealthHandler handler = new HealthHandler(repo);
        Result result = handler.process(null, Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(200));
        verify(repo).isHealthy();
    }

    @Test
    public void testHealthHandler_connotAcceptBody() throws IOException {
        HealthHandler handler = new HealthHandler(repo);
        Result result = handler.process(dumpJson(testItem1), Collections.emptyMap());
        assertThat(result.getStatus(), equalTo(400));
        verifyZeroInteractions(repo);
    }
}
