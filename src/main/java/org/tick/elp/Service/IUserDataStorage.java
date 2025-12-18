package org.tick.elp.Service;

import java.util.List;

public interface IUserDataStorage {
    // 初始化数据库,如果不存在则创建
    void initializeUserDataBase();
    // 插入用户错误答案数据
    boolean insertUserTestData(List<String> falseAnswers);
    
    // 添加收藏
    boolean addCollection(String word);
    // 移除收藏
    boolean removeCollection(String word);
    // 获取所有收藏
    List<String> getCollections();
    // 检查是否已收藏
    boolean isCollected(String word);

    // 删除数据(仅用于测试)
    boolean clearUserTestData();
}
