package org.tick.elp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tick.elp.Service.DataBaseService;
import org.tick.elp.Service.SQLWordRandomGet;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SQLWordRandomGetTest {

    @BeforeAll
    public static void setup() {
        // Ensure database connection is initialized
        DataBaseService.getInstance().initializeConnect();
    }

    @Test
    public void testGetRandomWordArray() {
        SQLWordRandomGet randomGet = new SQLWordRandomGet();
        int count = 5;
        String tableName = "dictionary";

        Map<String, String> result = randomGet.getRandomWordArray(count, tableName);

        assertNotNull(result, "Result map should not be null");
        assertFalse(result.isEmpty(), "Result map should not be empty");
        // Note: The actual size might be less than 'count' if the table is small or due to the random logic/deduplication
        assertTrue(result.size() <= count, "Result size should be at most " + count);
        
        System.out.println("Random words retrieved: " + result);
        
        for (Map.Entry<String, String> entry : result.entrySet()) {
            assertNotNull(entry.getKey(), "Word (key) should not be null");
            assertNotNull(entry.getValue(), "Translation (value) should not be null");
        }
    }
}
