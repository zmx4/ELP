package org.tick.elp.Helper;

public class PathHelper {
    private static String _localFolder = "";
    private static String LocalFolder() {
        if (_localFolder.isEmpty()) {
            String userHome = System.getProperty("user.home");
            _localFolder = userHome + "/.elp/";
        }
        return _localFolder;
    }
    public static String getLocalFilePath(String fileName) {
        return LocalFolder() + fileName;
    }
}
