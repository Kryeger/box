package logic.service;

import utils.Time;

public class TimeService {

    private static final TimeService _instance = new TimeService();

    private Time _time = new Time();

    private TimeService() {}

    public static long getSeconds() {
        return _instance._time.getSeconds();
    }

    public static String getDate() {
        return _instance._time.getDate();
    }

    public static void addHours(long hours) {
        _instance._time.addHours(hours);
    }

}
