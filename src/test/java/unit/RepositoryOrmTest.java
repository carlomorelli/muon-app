package unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.RepositoryException;
import com.csoft.muon.repository.RepositoryImpl;
import com.csoft.muon.repository.RepositoryOrmImpl;
import com.csoft.muon.repository.datasource.DataSourceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class RepositoryOrmTest {

    DataSource ds;
    RepositoryOrmImpl repo;

    @BeforeClass
    public void setupClass() {

        // force usage of H2 for all Repository unit tests
        ds = DataSourceFactory.getH2DataSource();
    }

//    @BeforeMethod
//    public void setup() {
//        // create a new Dao for every test
//        repo = new RepositoryOrmImpl(ds);
//        repo.prepareDb();
//    }
//
//    @AfterMethod
//    public void teardown() {
//        repo.flushTable();
//    }

    @Test
    public void testShouldInsertItem() throws RepositoryException {
        final Item item = new Item(456, "asdaiwdjawidj");
        repo.insertItem(item);
    }

    @Test
    public void testShouldPersistItem() throws RepositoryException {
        final Item item = new Item(123, "test");
        repo.insertItem(item);
        assertThat(repo.fetchAllItems(), contains(item));
    }

    @Test(expectedExceptions = RepositoryException.class)
    public void testShouldNotPersistItem_whenIndexIsAlreadyUsed() throws RepositoryException {
        final Item item1 = new Item(12345, "firstItem");
        final Item item2 = new Item(12345, "secondItem");
        repo.insertItem(item1);
        assertThat(repo.fetchAllItems(), contains(item1));
        repo.insertItem(item2);
        assertThat(repo.fetchAllItems(), not(contains(item2)));
    }

    @DataProvider(name = "invalidIndexData")
    public Object[][] invalidIndexData() {
        return new Object[][] {
            {
                new Item(null, "negativeTest1")
            },
            {
                new Item(-1, "negativeTest2")
            }
        };
    }

    @Test(expectedExceptions = RepositoryException.class, dataProvider = "invalidIndexData")
    public void testShouldNotPersistItem_whenInvalidIndex(final Item item) throws RepositoryException {
        repo.insertItem(item);
        List<Item> list = repo.fetchAllItems();
        assertThat(list.size(), equalTo(0));
    }

    @Test
    public void testShouldPersistMultipleItems() {
        final int N = 100;
        IntStream.range(0, N).forEach(x -> {
            try {
                repo.insertItem(new Item(x + 1, "prova" + x));
            } catch (RepositoryException e) {
                throw new RuntimeException("Error inserting item!", e);
            }
        });
        List<Item> list = repo.fetchAllItems();
        assertThat(list.size(), equalTo(N));
    }

    @Test
    public void testShouldFetchItem() throws RepositoryException {
        final Item item = new Item(123, "test");
        repo.insertItem(item);
        Item retrieved = repo.fetchItemAtIndex(123);
        assertThat(retrieved, not(nullValue()));
        assertThat(retrieved.getIndex(), equalTo(123));
        assertThat(retrieved.getLabel(), equalTo("test"));
    }

    @Test(expectedExceptions = RepositoryException.class)
    public void testShouldNotFetchItem_whenIndexIsNotUsed() throws RepositoryException {
        Item retrieved = repo.fetchItemAtIndex(2342);
        assertThat(retrieved, nullValue());
    }

    @Test
    public void testShouldUpdateItem() throws RepositoryException {
        final Item item1 = new Item(123, "originalData");
        final Item item2 = new Item(123, "nowIChangedSomeData");
        repo.insertItem(item1);
        assertThat(repo.fetchAllItems(), contains(item1));
        assertThat(repo.fetchAllItems(), not(contains(item2)));
        repo.updateItem(item2);
        assertThat(repo.fetchAllItems(), not(contains(item1)));
        assertThat(repo.fetchAllItems(), contains(item2));
    }

    @Test(expectedExceptions = RepositoryException.class)
    public void testShouldNotUpdateItem_whenIndexIsNotUsed() throws RepositoryException {
        final Item item1 = new Item(123, "originalData");
        final Item item2 = new Item(124, "nowIChangedSomeData");
        repo.insertItem(item1);
        assertThat(repo.fetchAllItems(), contains(item1));
        assertThat(repo.fetchAllItems(), not(contains(item2)));
        repo.updateItem(item2);
        assertThat(repo.fetchAllItems(), contains(item1));
        assertThat(repo.fetchAllItems(), not(contains(item2)));
    }

    @Test
    public void testShouldDeleteItem() throws RepositoryException {
        final Item item = new Item(7589, "data");
        repo.insertItem(item);
        assertThat(repo.fetchAllItems(), contains(item));
        repo.deleteItemAtIndex(item.getIndex());
        assertThat(repo.fetchAllItems(), not(contains(item)));
    }

    @Test(expectedExceptions = RepositoryException.class)
    public void testShouldNotDeleteItem_whenIndexIsNotUsed() throws RepositoryException {
        final Item item = new Item(7589, "data");
        repo.insertItem(item);
        assertThat(repo.fetchAllItems(), contains(item));
        repo.deleteItemAtIndex(item.getIndex() + 1);
        assertThat(repo.fetchAllItems(), contains(item));
    }

}
