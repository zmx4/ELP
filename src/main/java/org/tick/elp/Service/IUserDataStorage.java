package org.tick.elp.Service;

import java.util.List;

public interface IUserDataStorage {
    // 初始化数据库,如果不存在则创建
    void initializeUserDataBase();
    // 插入用户错误答案数据
    boolean insertUserTestData(List<String> falseAnswers);
    // 删除数据(仅用于测试)
    boolean clearUserTestData();
}
