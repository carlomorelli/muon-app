package unit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.sql2o.Sql2oException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.csoft.muon.dao.Dao;
import com.csoft.muon.dao.DaoException;
import com.csoft.muon.dao.DataSourceFactory;
import com.csoft.muon.domain.Item;

public class DaoTest {

    DataSource ds;
    Dao dao;
    
    @BeforeClass
    public void setupClass() {
        // for testing purposes, only H2 database will be used
        // TODO use dependency injection
        ds = DataSourceFactory.getH2DataSource();
    }
    
    @BeforeMethod
    public void setup() {
        // create a new Dao for every test
        dao = new Dao(ds);
        dao.prepareDb();
    }

    @AfterMethod
    public void teardown() {
        dao.flushTable();
    }
    
    @Test
    public void testShouldInsertItem() throws DaoException {
        final Item item = new Item(456, "asdaiwdjawidj");
        dao.insertItem(item);
    }
    
    @Test
    public void testShouldPersistItem() throws DaoException {
        final Item item = new Item(123, "test");
        dao.insertItem(item);
        assertThat(dao.fetchAllItems(), contains(item));
    }
    
    @Test(expectedExceptions = Sql2oException.class)
    public void testShouldAvoidIndexDuplication() throws DaoException {
        final Item item1 = new Item(12345, "firstItem");
        final Item item2 = new Item(12345, "secondItem");
        dao.insertItem(item1);
        assertThat(dao.fetchAllItems(), contains(item1));
        dao.insertItem(item2);
        assertThat(dao.fetchAllItems(), not(contains(item2)));
    }
    
    @Test
    public void testShouldPersisteMultipleItems() throws DaoException {
        final int N = 100;
        IntStream.range(0, N).forEach(
                x -> {
                    try {
                        dao.insertItem(new Item(x+1, "prova" +x));
                    } catch (DaoException e) {
                        throw new RuntimeException("Error inserting item!", e);
                    }
                }
        );
        List<Item> list = dao.fetchAllItems();
        assertThat(list.size(), equalTo(N));
    }
    
    @Test(expectedExceptions = DaoException.class)
    public void testNegativeIndexNotAllowed() throws DaoException {
        final Item item = new Item(-1, "negativeTest");
        dao.insertItem(item);
        List<Item> list = dao.fetchAllItems();
        assertThat(list.size(), equalTo(0));
    }

}
