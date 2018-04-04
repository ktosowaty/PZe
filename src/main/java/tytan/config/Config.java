package tytan.config;

public class Config {

    private static Config mInstance;

    public static Config getInstance() {
        if (mInstance == null) {
            mInstance = new Config();
        }
        return mInstance;
    }

    public String getApiUrl() {
        return "ws://localhost:8181";
//        return "ws://skyesoftware.pl:8181";
    }
}
