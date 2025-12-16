package org.tick.elp;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tick.elp.Service.DataBaseService;

@ExtendWith(MockitoExtension.class)
public class DataBaseServiceTest {
    private DataBaseService dbService;

    @Test
    public void connectTest() {
        dbService = DataBaseService.getInstance();
        dbService.initializeConnect();
        assert dbService.getWordDao() != null;
    }

    @Test
    public void daoTest() {
        dbService = DataBaseService.getInstance();
        var wordDao = dbService.getWordDao();
        assert wordDao != null;
    }
}
