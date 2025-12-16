package org.tick.elp.Service;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.j256.ormlite.dao.GenericRawResults;

public class SQLWordRandomGet implements IWordRandomGet {
    private static final DataBaseService dataBaseService = DataBaseService.getInstance();

    @Override
    public Map<String,String> getRandomWordArray(int number, String table) {
        try {
            GenericRawResults<String[]> rawResults = dataBaseService.getWordDao().queryRaw(
                    """
                            WITH RECURSIVE random_ids(id) AS (
                        -- 初始生成一个随机数
                        SELECT abs(random()) % (SELECT max(rowid) FROM my_table) + 1
                        UNION ALL
                        -- 递归生成更多随机数，直到达到 40 个
                        SELECT abs(random()) % (SELECT max(rowid) FROM my_table) + 1
                        FROM random_ids
                        LIMIT {}
                        )
                        SELECT t.*
                        FROM my_table t
                        INNER JOIN random_ids r ON t.rowid >= r.id -- 使用 >= 解决 ID 空洞问题
                        GROUP BY t.rowid -- 去重，防止正好撞上同一个 ID
                        LIMIT {}
                        """.replace("my_table", table).replace("{}", String.valueOf(number))
            );
            Map <String,String> resultMap = new ConcurrentHashMap<String,String>();
            for (String[] resultArray : rawResults) {
                resultMap.put(resultArray[0], resultArray[1]);
            }
            return resultMap;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
