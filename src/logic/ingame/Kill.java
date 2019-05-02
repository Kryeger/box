package logic.ingame;

public class Kill {

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
