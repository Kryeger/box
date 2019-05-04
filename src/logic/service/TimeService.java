package logic.service;

import utils.Time;

public class TimeService {

    private static final TimeService _instance = new TimeService();

    private Time _time = new Time();

    private TimeService() {
    }

    public static long getSeconds() {
        return _instance._time.getSeconds();
    }

    public static String getDate() {
        return _instance._time.getDate();
    }

    public static String getTime() {
        return _instance._time.getTime();
    }

    public static void addHours(long hours) {
        _instance._time.addHours(hours);
    }

    public static Time now() {
        return new Time(_instance._time);
    }

    public static void setSeconds(long seconds) {
        _instance._time.setSeconds(seconds);
    }

}
