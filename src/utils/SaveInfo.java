package utils;

import java.io.Serializable;
import java.sql.Timestamp;

public class SaveInfo implements Comparable<SaveInfo>, Serializable {

    private String _appId;
    private String _playerName;
    private long _timestamp;

    public SaveInfo(String appId, String playerName, long timestamp) {
        _appId = appId;
        _playerName = playerName;
        _timestamp = timestamp;
    }

    public String getAppId() {
        return _appId;
    }

    public String getPlayerName() {
        return _playerName;
    }

    public long getTimestamp() {
        return _timestamp;
    }

    @Override
    public int compareTo(SaveInfo o) {
        return (Long.compare(_timestamp, o._timestamp));
    }
}
