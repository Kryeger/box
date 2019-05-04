package logic.ingame;

import java.io.Serializable;

public class Kill implements Serializable {

    private String _killerId;
    private String _killedId;

    public Kill(String killerId, String killedId) {
        _killerId = killerId;
        _killedId = killedId;
    }

    public String getKillerId() {
        return _killerId;
    }

    public String getKilledId() {
        return _killedId;
    }
}
