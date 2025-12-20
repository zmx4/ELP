package org.tick.elp.Service;

import java.util.Date;

public interface IPreferenceStorage {
    void set(String key, String value);

    String get(String key, String defaultValue);

    void set(String key, boolean value);

    boolean get(String key, boolean defaultValue);

    void set(String key, int value);

    int get(String key, int defaultValue);

    void set(String key, Date value);

    Date get(String key, Date defaultValue);
}
