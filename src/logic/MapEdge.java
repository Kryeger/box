package logic;

public class MapEdge {

    private double _visibility = 0;
    private double _distance = 0;
    private MapNode _start;
    private MapNode _end;

    public MapEdge(double visibility, double distance, MapNode start, MapNode end) {
        _visibility = visibility;
        _distance = distance;
        _start = start;
        _end = end;
    }

    public double getVisibility() {
        return _visibility;
    }

    public void setVisibility(double visibility) {
        _visibility = visibility;
    }

    public double getDistance() {
        return _distance;
    }

    public void setDistance(double distance) {
        this._distance = distance;
    }
}
