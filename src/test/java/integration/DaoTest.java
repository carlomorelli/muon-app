package integration;

import static com.csoft.muon.repository.datasource.DataSourceFactory.getH2DataSource;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.csoft.muon.domain.Item;
import com.csoft.muon.repository.RepositoryException;
import com.csoft.muon.repository.RepositoryImpl;

public class DaoTest {

    
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoTest.class);

    private Server server;
    
    @BeforeClass
    public void setupClass() {
        try {
            server = Server.createTcpServer("-tcpPort", "9093").start();
            LOGGER.info("Started H2 server [{}]", server.getStatus());
        } catch (SQLException e) {
            LOGGER.error("Starting H2 server failed!");
            throw new RuntimeException(e);
        }
    }
    
    @AfterClass
    public void teardownClass() {
        server.stop();
        LOGGER.info("Stopped H2 server [{}]", server.getStatus());
    }
    
    @Test
    public void testItemInsert() {
        RepositoryImpl db = new RepositoryImpl(getH2DataSource());
        db.prepareDb();
        Item myItem = new Item(1, "label");
        try {
            db.insertItem(myItem);
        } catch (RepositoryException e) {
            throw new RuntimeException("Unable to insert", e);
        }
    }
    
    
    @Test
    public void testItemFetchAll() {
        RepositoryImpl db = new RepositoryImpl(getH2DataSource());
        db.prepareDb();
        
    }
    
}
