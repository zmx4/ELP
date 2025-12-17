package org.tick.elp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tick.elp.Service.UserDataStorage;



@ExtendWith(MockitoExtension.class)
public class UserDataStorageTest {
    private UserDataStorage userDataStorage;
    
    @Test
    public void initializeUserDataBaseTest() {
        userDataStorage = UserDataStorage.getInstance();
        userDataStorage.initializeUserDataBase();
        assert userDataStorage != null;
    }

    @Test
    public void insertUserTestDataTest() {
        userDataStorage = UserDataStorage.getInstance();
        userDataStorage.initializeUserDataBase();
        var result = userDataStorage.insertUserTestData(
                java.util.Arrays.asList("test1", "test2", "test3")
        );
        assert result;
    }
    // 删除数据(仅用于测试)
    @Test
    public void clearUserTestDataTest() {
        userDataStorage = UserDataStorage.getInstance();
        userDataStorage.initializeUserDataBase();
        var result = userDataStorage.clearUserTestData();
        assert result;
    }
}
