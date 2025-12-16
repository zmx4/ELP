package org.tick.elp.Service;

import java.util.List;

public interface IWordQueryService {
    String queryTranslation(String word);
    List<String> queryWordByTranslation(String translation);
}